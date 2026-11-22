package tn.esprit.ms6association.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "intervention")
@Getter @Setter @NoArgsConstructor
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // simple identifiant vers MS2 (Incident)
    @Column(nullable = false)
    private Long incidentId;

    @Enumerated(EnumType.STRING)
    private StatutIntervention statut = StatutIntervention.ASSIGNED;

    private LocalDateTime dateAffectation = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;
}
