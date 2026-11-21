package tn.esprit.ms3locationzone.zone.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import tn.esprit.ms3locationzone.zone.entity.NiveauRisque;

public record ZoneRequest(
        @NotBlank(message = "nom obligatoire") String nom,
        String ville,
        @NotNull @DecimalMin("-90") @DecimalMax("90") Double latitudeCentre,
        @NotNull @DecimalMin("-180") @DecimalMax("180") Double longitudeCentre,
        @NotNull @Positive(message = "rayonKm doit etre > 0") Double rayonKm,
        NiveauRisque niveauRisque) {
}