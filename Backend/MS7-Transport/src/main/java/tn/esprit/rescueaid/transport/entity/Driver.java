package tn.esprit.rescueaid.transport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nom;

    @NotBlank
    @Column(nullable = false)
    private String telephone;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String numeroPermis;

    @NotNull
    @Column(nullable = false)
    private Boolean disponible;

    private Long vehicleId;

    public void affecterVehicule(Long vehicleId) {
        this.vehicleId = vehicleId;
        this.disponible = false;
    }

    public void mettreAJourDisponibilite(Boolean disponible) {
        this.disponible = disponible;
    }
}