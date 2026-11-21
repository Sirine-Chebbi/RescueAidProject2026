package tn.esprit.rescueaid.transport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import tn.esprit.rescueaid.transport.enums.StatutVehicule;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String immatriculation;

    @NotBlank
    @Column(nullable = false)
    private String type;

    @NotNull
    @Column(nullable = false)
    private Double capaciteKg;

    @NotNull
    @Column(nullable = false)
    private Integer capacitePersonnes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutVehicule statut;

    private String ville;

    private Double latitude;

    private Double longitude;

    private LocalDateTime derniereMiseAJourGps;

    public boolean verifierDisponibilite() {
        return this.statut == StatutVehicule.DISPONIBLE;
    }

    public void mettreAJourPosition(Double latitude, Double longitude, String ville) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (ville != null && !ville.isBlank()) {
            this.ville = ville;
        }
        this.derniereMiseAJourGps = LocalDateTime.now();
    }
}