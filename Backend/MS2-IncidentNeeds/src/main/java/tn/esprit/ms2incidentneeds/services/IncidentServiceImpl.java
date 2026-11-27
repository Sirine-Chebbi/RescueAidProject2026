package tn.esprit.ms2incidentneeds.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2incidentneeds.entities.Besoin;
import tn.esprit.ms2incidentneeds.entities.Evaluation;
import tn.esprit.ms2incidentneeds.entities.Incident;
import tn.esprit.ms2incidentneeds.enums.Priorite;
import tn.esprit.ms2incidentneeds.enums.StatutIncident;
import tn.esprit.ms2incidentneeds.enums.TypeIncident;
import tn.esprit.ms2incidentneeds.exceptions.ResourceNotFoundException;
import tn.esprit.ms2incidentneeds.repositories.BesoinRepository;
import tn.esprit.ms2incidentneeds.repositories.IncidentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IncidentServiceImpl implements IIncidentService {

    private final IncidentRepository incidentRepository;
    private final BesoinRepository besoinRepository;

    @Override
    public Incident createIncident(Incident incident) {
        incident.setId(null);
        if (incident.getStatut() == null) {
            incident.setStatut(StatutIncident.NOUVEAU);
        }
        if (incident.getDateSignalement() == null) {
            incident.setDateSignalement(LocalDateTime.now());
        }

        if (incident.getBesoins() != null) {
            List<Besoin> incomingBesoins = new ArrayList<>(incident.getBesoins());
            incident.getBesoins().clear();
            for (Besoin b : incomingBesoins) {
                b.setId(null);
                if (b.getPriorite() == null) {
                    b.setPriorite(Priorite.MOYENNE);
                }
                incident.ajouterBesoin(b);
            }
        }

        if (incident.getEvaluation() != null) {
            Evaluation eval = incident.getEvaluation();
            eval.setId(null);
            eval.setIncident(incident);
            if (eval.getScore() == null) {
                eval.calculerScore();
            }
            if (eval.getNiveau() == null) {
                eval.determinerNiveau();
            }
            if (eval.getDateEvaluation() == null) {
                eval.setDateEvaluation(LocalDateTime.now());
            }
        }

        return initializeRelations(incidentRepository.save(incident));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incident> getAllIncidents() {
        List<Incident> incidents = incidentRepository.findAll();
        incidents.forEach(this::initializeRelations);
        return incidents;
    }

    @Override
    @Transactional(readOnly = true)
    public Incident getIncidentById(Long id) {
        return initializeRelations(findIncidentEntity(id));
    }

    @Override
    public Incident updateIncident(Long id, Incident request) {
        Incident incident = findIncidentEntity(id);
        incident.setTitre(request.getTitre());
        incident.setDescription(request.getDescription());
        incident.setType(request.getType());
        incident.setLocalisation(request.getLocalisation());
        if (request.getStatut() != null) {
            incident.mettreAJourStatut(request.getStatut());
        }
        incident.setCitoyenId(request.getCitoyenId());

        return initializeRelations(incidentRepository.save(incident));
    }

    @Override
    public void deleteIncident(Long id) {
        Incident incident = findIncidentEntity(id);
        incidentRepository.delete(incident);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incident> getIncidentsByStatut(StatutIncident statut) {
        List<Incident> incidents = incidentRepository.findByStatut(statut);
        incidents.forEach(this::initializeRelations);
        return incidents;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incident> getIncidentsByType(TypeIncident type) {
        List<Incident> incidents = incidentRepository.findByType(type);
        incidents.forEach(this::initializeRelations);
        return incidents;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Incident> getIncidentsByCitoyen(Long citoyenId) {
        List<Incident> incidents = incidentRepository.findByCitoyenId(citoyenId);
        incidents.forEach(this::initializeRelations);
        return incidents;
    }

    @Override
    public Incident mettreAJourStatut(Long id, StatutIncident statut) {
        Incident incident = findIncidentEntity(id);
        incident.mettreAJourStatut(statut);
        return initializeRelations(incidentRepository.save(incident));
    }

    @Override
    public Besoin ajouterBesoin(Long incidentId, Besoin besoin) {
        Incident incident = findIncidentEntity(incidentId);
        besoin.setId(null);
        if (besoin.getPriorite() == null) {
            besoin.setPriorite(Priorite.MOYENNE);
        }
        incident.ajouterBesoin(besoin);
        return besoinRepository.save(besoin);
    }

    @Override
    public Incident evaluerGravite(Long id) {
        Incident incident = findIncidentEntity(id);
        incident.evaluerGravite();
        return initializeRelations(incidentRepository.save(incident));
    }

    private Incident findIncidentEntity(Long id) {
        return incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident introuvable avec l'id : " + id));
    }

    private Incident initializeRelations(Incident incident) {
        if (incident.getBesoins() != null) {
            incident.getBesoins().size();
        }
        if (incident.getEvaluation() != null) {
            incident.getEvaluation().getId();
        }
        return incident;
    }
}
