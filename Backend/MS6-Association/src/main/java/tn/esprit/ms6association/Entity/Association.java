package tn.esprit.ms6association.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "association")
@Getter @Setter @NoArgsConstructor
public class Association {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;
    private String adresse;
    private String telephone;
    private String emailContact;

    private Double latitude;
    private Double longitude;
    private Double rayonActionKm;

    // simple identifiant vers MS1 (pas de relation JPA entre services)
    private Long userId;

    @OneToMany(mappedBy = "association", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Equipe> equipes = new ArrayList<>();
}
