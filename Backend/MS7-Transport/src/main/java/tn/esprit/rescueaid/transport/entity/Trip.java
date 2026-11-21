package tn.esprit.rescueaid.transport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.rescueaid.transport.enums.StatutTrip;

import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long vehicleId;

    @NotNull
    @Column(nullable = false)
    private Long driverId;

    private Long incidentId;

    private Long distributionId;

    @NotBlank
    @Column(nullable = false)
    private String pointDepart;

    @NotBlank
    @Column(nullable = false)
    private String destination;

    @NotBlank
    @Column(nullable = false)
    private String typeTrajet;

    private Double chargeKg;

    private Integer nombrePersonnes;

    private LocalDateTime dateDepart;

    private LocalDateTime dateArrivee;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutTrip statut;

    public void planifier() {
        this.statut = StatutTrip.PLANIFIE;
        if (this.dateDepart == null) {
            this.dateDepart = LocalDateTime.now();
        }
    }

    public void mettreAJourStatut(StatutTrip nouveauStatut) {
        this.statut = nouveauStatut;
        if (nouveauStatut == StatutTrip.TERMINE && this.dateArrivee == null) {
            this.dateArrivee = LocalDateTime.now();
        }
    }
}