package tn.esprit.ms3locationzone.incident.dto;

import tn.esprit.ms3locationzone.incident.entity.Priorite;

import java.time.LocalDateTime;

public record IncidentLocationResponse(
        Long incidentId,
        Double latitude,
        Double longitude,
        Priorite priorite,
        Long zoneId,
        LocalDateTime dateSynchro) {
}