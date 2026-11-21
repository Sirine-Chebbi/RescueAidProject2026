package tn.esprit.ms3locationzone.incident.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import tn.esprit.ms3locationzone.incident.entity.Priorite;

public record IncidentLocationRequest(
        @NotNull Long incidentId,
        @NotNull @DecimalMin("-90") @DecimalMax("90") Double latitude,
        @NotNull @DecimalMin("-180") @DecimalMax("180") Double longitude,
        Priorite priorite) {
}