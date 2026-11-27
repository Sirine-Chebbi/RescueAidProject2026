package tn.esprit.ms2incidentneeds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tn.esprit.ms2incidentneeds.entities.Besoin;
import tn.esprit.ms2incidentneeds.entities.Evaluation;
import tn.esprit.ms2incidentneeds.entities.Incident;
import tn.esprit.ms2incidentneeds.enums.NiveauGravite;
import tn.esprit.ms2incidentneeds.enums.Priorite;
import tn.esprit.ms2incidentneeds.enums.StatutIncident;
import tn.esprit.ms2incidentneeds.enums.TypeBesoin;
import tn.esprit.ms2incidentneeds.enums.TypeIncident;
import tn.esprit.ms2incidentneeds.repositories.BesoinRepository;
import tn.esprit.ms2incidentneeds.repositories.EvaluationRepository;
import tn.esprit.ms2incidentneeds.repositories.IncidentRepository;
import tn.esprit.ms2incidentneeds.services.IBesoinService;
import tn.esprit.ms2incidentneeds.services.IEvaluationService;
import tn.esprit.ms2incidentneeds.services.IIncidentService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "eureka.client.enabled=false")
class Ms2IncidentNeedsApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IIncidentService incidentService;

    @Autowired
    private IEvaluationService evaluationService;

    @Autowired
    private IBesoinService besoinService;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private BesoinRepository besoinRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        besoinRepository.deleteAll();
        evaluationRepository.deleteAll();
        incidentRepository.deleteAll();
    }

    @Test
    void contextLoadsAndSwaggerEndpointsRespond() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").exists())
                .andExpect(jsonPath("$.paths['/api/incidents']").exists())
                .andExpect(jsonPath("$.paths['/api/evaluations']").exists())
                .andExpect(jsonPath("$.paths['/api/besoins']").exists());

        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    @Test
    void crudIncidentBesoinEvaluationAndDomainMethodsWork() {
        // 1. Create Incident
        Incident incident = incidentService.createIncident(Incident.builder()
                .titre("Inondation majeure")
                .description("Quartier Nord submergé")
                .type(TypeIncident.INONDATION)
                .localisation("Tunis Nord")
                .citoyenId(101L)
                .build());

        assertThat(incident.getId()).isNotNull();
        assertThat(incident.getStatut()).isEqualTo(StatutIncident.NOUVEAU);

        // 2. Add Besoin via IncidentService (ajouterBesoin)
        Besoin eau = incidentService.ajouterBesoin(incident.getId(), Besoin.builder()
                .typeBesoin(TypeBesoin.EAU)
                .quantite(200)
                .description("Packs d'eau potable")
                .priorite(Priorite.HAUTE)
                .build());

        assertThat(eau.getId()).isNotNull();
        assertThat(eau.getIncidentId()).isEqualTo(incident.getId());

        // 3. Update Besoin quantity & priority
        Besoin updatedQty = besoinService.mettreAJourQuantite(eau.getId(), 350);
        assertThat(updatedQty.getQuantite()).isEqualTo(350);

        Besoin updatedPrio = besoinService.changerPriorite(eau.getId(), Priorite.CRITIQUE);
        assertThat(updatedPrio.getPriorite()).isEqualTo(Priorite.CRITIQUE);

        // 4. Evaluate Incident gravity (evaluerGravite -> calculerScore & determinerNiveau)
        Incident evaluated = incidentService.evaluerGravite(incident.getId());
        assertThat(evaluated.getEvaluation()).isNotNull();
        assertThat(evaluated.getEvaluation().getScore()).isEqualTo(50.0);
        assertThat(evaluated.getEvaluation().getNiveau()).isEqualTo(NiveauGravite.ELEVE);

        // 5. Update Incident status (mettreAJourStatut)
        Incident inProgress = incidentService.mettreAJourStatut(incident.getId(), StatutIncident.EN_COURS);
        assertThat(inProgress.getStatut()).isEqualTo(StatutIncident.EN_COURS);

        // 6. Read Evaluation & delete it, then recreate via EvaluationService
        Evaluation evalByIncident = evaluationService.getEvaluationByIncidentId(incident.getId());
        assertThat(evalByIncident.getId()).isEqualTo(evaluated.getEvaluation().getId());

        evaluationService.deleteEvaluation(evalByIncident.getId());
        assertThat(evaluationRepository.findByIncidentId(incident.getId())).isEmpty();

        Evaluation manualEval = evaluationService.createEvaluation(Evaluation.builder()
                .incidentId(incident.getId())
                .score(85.0)
                .build());
        assertThat(manualEval.getNiveau()).isEqualTo(NiveauGravite.CRITIQUE);

        // 7. Delete Besoin and Incident
        besoinService.deleteBesoin(eau.getId());
        assertThat(besoinService.getBesoinsByIncidentId(incident.getId())).isEmpty();

        incidentService.deleteIncident(incident.getId());
        assertThat(incidentService.getAllIncidents()).isEmpty();
    }

    @Test
    void restControllerCreatesAndListsIncident() throws Exception {
        String payload = """
                {
                  "titre": "Incendie forêt",
                  "description": "Départ de feu rapide",
                  "type": "INCENDIE",
                  "localisation": "Ain Draham",
                  "citoyenId": 55
                }
                """;

        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.statut").value("NOUVEAU"))
                .andExpect(jsonPath("$.type").value("INCENDIE"));

        mockMvc.perform(get("/api/incidents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titre").value("Incendie forêt"));
    }
}
