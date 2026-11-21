package tn.esprit.ms3locationzone.point.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record OccupationRequest(@NotNull @PositiveOrZero Integer occupation) {
}