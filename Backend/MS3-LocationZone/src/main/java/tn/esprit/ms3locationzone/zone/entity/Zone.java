package tn.esprit.ms3locationzone.zone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "zone")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String ville;

    @Column(nullable = false)
    private Double latitudeCentre;

    @Column(nullable = false)
    private Double longitudeCentre;

    @Column(nullable = false)
    private Double rayonKm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NiveauRisque niveauRisque = NiveauRisque.FAIBLE;

    public void mettreAJourRisque(NiveauRisque r) {
        this.niveauRisque = r;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public Double getLatitudeCentre() { return latitudeCentre; }
    public void setLatitudeCentre(Double latitudeCentre) { this.latitudeCentre = latitudeCentre; }

    public Double getLongitudeCentre() { return longitudeCentre; }
    public void setLongitudeCentre(Double longitudeCentre) { this.longitudeCentre = longitudeCentre; }

    public Double getRayonKm() { return rayonKm; }
    public void setRayonKm(Double rayonKm) { this.rayonKm = rayonKm; }

    public NiveauRisque getNiveauRisque() { return niveauRisque; }
    public void setNiveauRisque(NiveauRisque niveauRisque) { this.niveauRisque = niveauRisque; }
}