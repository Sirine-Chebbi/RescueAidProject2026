package tn.esprit.ms3locationzone.incident.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incident_location")
public class IncidentLocation {

    // Cet id vient de MS2 : on ne le génère pas nous-mêmes
    @Id
    private Long incidentId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priorite priorite = Priorite.BASSE;

    private Long zoneId;

    private LocalDateTime dateSynchro;

    public Long getIncidentId() { return incidentId; }
    public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public Priorite getPriorite() { return priorite; }
    public void setPriorite(Priorite priorite) { this.priorite = priorite; }

    public Long getZoneId() { return zoneId; }
    public void setZoneId(Long zoneId) { this.zoneId = zoneId; }

    public LocalDateTime getDateSynchro() { return dateSynchro; }
    public void setDateSynchro(LocalDateTime dateSynchro) { this.dateSynchro = dateSynchro; }
}