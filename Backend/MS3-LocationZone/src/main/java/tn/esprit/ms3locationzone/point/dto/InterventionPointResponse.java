package tn.esprit.ms3locationzone.point.dto;

import tn.esprit.ms3locationzone.point.entity.StatutPoint;
import tn.esprit.ms3locationzone.point.entity.TypePoint;

public record InterventionPointResponse(
        Long id,
        Long zoneId,
        String nom,
        TypePoint type,
        Double latitude,
        Double longitude,
        Integer capaciteMax,
        Integer occupationActuelle,
        Integer placesLibres,
        StatutPoint statut) {
}