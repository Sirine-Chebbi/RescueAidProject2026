package tn.esprit.ms3locationzone.zone.dto;

import tn.esprit.ms3locationzone.zone.entity.NiveauRisque;

public record ZoneResponse(
        Long id,
        String nom,
        String ville,
        Double latitudeCentre,
        Double longitudeCentre,
        Double rayonKm,
        NiveauRisque niveauRisque) {
}