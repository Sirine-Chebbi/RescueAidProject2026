package tn.esprit.ms2incidentneeds.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.ms2incidentneeds.enums.StatutIncident;
import tn.esprit.ms2incidentneeds.enums.TypeIncident;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "incidents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false)
    private String titre;

    @Column(length = 2000)
    private String description;

    @NotNull(message = "Le type d'incident est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeIncident type;

    @NotBlank(message = "La localisation est obligatoire")
    @Column(nullable = false)
    private String localisation;

    @Column(nullable = false)
    private LocalDateTime dateSignalement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatutIncident statut = StatutIncident.NOUVEAU;

    @NotNull(message = "L'identifiant du citoyen est obligatoire")
    @Column(nullable = false)
    private Long citoyenId;

    @OneToOne(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("incident")
    private Evaluation evaluation;

    @OneToMany(mappedBy = "incident", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("incident")
    @Builder.Default
    private List<Besoin> besoins = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.dateSignalement == null) {
            this.dateSignalement = LocalDateTime.now();
        }
        if (this.statut == null) {
            this.statut = StatutIncident.NOUVEAU;
        }
    }

    public void ajouterBesoin(Besoin besoin) {
        if (besoin == null) {
            return;
        }
        if (this.besoins == null) {
            this.besoins = new ArrayList<>();
        }
        if (!this.besoins.contains(besoin)) {
            this.besoins.add(besoin);
        }
        besoin.setIncident(this);
        if (this.id != null) {
            besoin.setIncidentId(this.id);
        }
    }

    public void evaluerGravite() {
        if (this.evaluation == null) {
            this.evaluation = new Evaluation();
        }
        this.evaluation.setIncident(this);
        if (this.id != null) {
            this.evaluation.setIncidentId(this.id);
        }
        this.evaluation.calculerScore();
        this.evaluation.determinerNiveau();
        this.evaluation.setDateEvaluation(LocalDateTime.now());
    }

    public void mettreAJourStatut(StatutIncident statut) {
        if (statut != null) {
            this.statut = statut;
        }
    }
}
