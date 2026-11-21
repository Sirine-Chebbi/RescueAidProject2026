package tn.esprit.ms3locationzone.point.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import tn.esprit.ms3locationzone.point.entity.StatutPoint;
import tn.esprit.ms3locationzone.point.entity.TypePoint;

public record InterventionPointRequest(
        @NotNull Long zoneId,
        @NotBlank(message = "nom obligatoire") String nom,
        @NotNull TypePoint type,
        @NotNull @DecimalMin("-90") @DecimalMax("90") Double latitude,
        @NotNull @DecimalMin("-180") @DecimalMax("180") Double longitude,
        @NotNull @Positive(message = "capaciteMax doit etre > 0") Integer capaciteMax,
        @PositiveOrZero Integer occupationActuelle,
        StatutPoint statut) {
}