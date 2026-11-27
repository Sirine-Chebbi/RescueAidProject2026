package tn.esprit.ms2incidentneeds.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.ms2incidentneeds.enums.Priorite;
import tn.esprit.ms2incidentneeds.enums.TypeBesoin;

@Entity
@Table(name = "besoins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Besoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le type de besoin est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeBesoin typeBesoin;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 0, message = "La quantité doit être supérieure ou égale à 0")
    @Column(nullable = false)
    private Integer quantite;

    @Column(length = 1000)
    private String description;

    @Column(name = "incident_id", insertable = false, updatable = false)
    private Long incidentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Priorite priorite = Priorite.MOYENNE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false)
    @JsonIgnore
    private Incident incident;

    @PrePersist
    protected void onCreate() {
        if (this.priorite == null) {
            this.priorite = Priorite.MOYENNE;
        }
    }

    public void mettreAJourQuantite(Integer qte) {
        if (qte == null || qte < 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure ou égale à 0");
        }
        this.quantite = qte;
    }

    public void changerPriorite(Priorite p) {
        if (p == null) {
            throw new IllegalArgumentException("La priorité ne peut pas être nulle");
        }
        this.priorite = p;
    }
}
