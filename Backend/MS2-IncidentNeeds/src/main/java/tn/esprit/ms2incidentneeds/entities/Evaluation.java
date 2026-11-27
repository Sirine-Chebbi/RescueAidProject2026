package tn.esprit.ms2incidentneeds.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import tn.esprit.ms2incidentneeds.enums.NiveauGravite;

import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DecimalMin(value = "0.0", message = "Le score doit être >= 0")
    @DecimalMax(value = "100.0", message = "Le score doit être <= 100")
    @Column(nullable = false)
    private Double score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NiveauGravite niveau;

    @Column(nullable = false)
    private LocalDateTime dateEvaluation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false, unique = true)
    @JsonIgnore
    private Incident incident;

    @Column(name = "incident_id", insertable = false, updatable = false)
    private Long incidentId;

    @PrePersist
    protected void onCreate() {
        if (this.dateEvaluation == null) {
            this.dateEvaluation = LocalDateTime.now();
        }
        if (this.score == null) {
            calculerScore();
        }
        if (this.niveau == null) {
            determinerNiveau();
        }
    }

    public Double calculerScore() {
        double baseScore = 25.0;
        if (this.incident != null && this.incident.getType() != null) {
            baseScore = switch (this.incident.getType()) {
                case SEISME -> 55.0;
                case INCENDIE -> 45.0;
                case INONDATION -> 40.0;
                case ACCIDENT -> 30.0;
                case AUTRE -> 20.0;
            };

            if (this.incident.getBesoins() != null && !this.incident.getBesoins().isEmpty()) {
                double needsBonus = Math.min(45.0, this.incident.getBesoins().size() * 10.0);
                baseScore += needsBonus;
            }
        }
        this.score = Math.min(100.0, Math.max(0.0, baseScore));
        return this.score;
    }

    public NiveauGravite determinerNiveau() {
        if (this.score == null) {
            calculerScore();
        }
        if (this.score >= 75.0) {
            this.niveau = NiveauGravite.CRITIQUE;
        } else if (this.score >= 50.0) {
            this.niveau = NiveauGravite.ELEVE;
        } else if (this.score >= 25.0) {
            this.niveau = NiveauGravite.MOYEN;
        } else {
            this.niveau = NiveauGravite.FAIBLE;
        }
        return this.niveau;
    }
}
