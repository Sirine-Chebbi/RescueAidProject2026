package tn.esprit.ms3locationzone.point.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "intervention_point")
public class InterventionPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Simple numéro de la zone (pas de relation JPA)
    @Column(nullable = false)
    private Long zoneId;

    @Column(nullable = false)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePoint type;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Integer capaciteMax;

    @Column(nullable = false)
    private Integer occupationActuelle = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPoint statut = StatutPoint.ACTIF;

    public boolean estSature() {
        return occupationActuelle >= capaciteMax;
    }

    public int placesLibres() {
        return Math.max(0, capaciteMax - occupationActuelle);
    }

    // Met à jour l'occupation et change le statut tout seul (ACTIF <-> SATURE)
    public void mettreAJourOccupation(int n) {
        this.occupationActuelle = n;
        if (this.statut != StatutPoint.FERME) {
            this.statut = estSature() ? StatutPoint.SATURE : StatutPoint.ACTIF;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getZoneId() { return zoneId; }
    public void setZoneId(Long zoneId) { this.zoneId = zoneId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public TypePoint getType() { return type; }
    public void setType(TypePoint type) { this.type = type; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Integer getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(Integer capaciteMax) { this.capaciteMax = capaciteMax; }

    public Integer getOccupationActuelle() { return occupationActuelle; }
    public void setOccupationActuelle(Integer occupationActuelle) { this.occupationActuelle = occupationActuelle; }

    public StatutPoint getStatut() { return statut; }
    public void setStatut(StatutPoint statut) { this.statut = statut; }
}