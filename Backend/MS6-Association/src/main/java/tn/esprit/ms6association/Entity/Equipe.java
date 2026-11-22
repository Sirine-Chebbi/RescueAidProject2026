package tn.esprit.ms6association.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipe")
@Getter @Setter @NoArgsConstructor
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpecialityType specialite;

    private Integer effectif;

    private Boolean disponible = true;
    private LocalDate dateDebutDisponibilite;
    private LocalDate dateFinDisponibilite;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "association_id")
    private Association association;

    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Intervention> interventions = new ArrayList<>();

    /** Disponible le jour donné : drapeau actif et date dans la plage (si une plage est définie). */
    public boolean estDisponibleLe(LocalDate d) {
        if (!Boolean.TRUE.equals(disponible)) return false;
        if (dateDebutDisponibilite != null && d.isBefore(dateDebutDisponibilite)) return false;
        if (dateFinDisponibilite != null && d.isAfter(dateFinDisponibilite)) return false;
        return true;
    }
}
