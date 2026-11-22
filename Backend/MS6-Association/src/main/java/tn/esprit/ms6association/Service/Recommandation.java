package tn.esprit.ms6association.Service;

import tn.esprit.ms6association.Entity.SpecialityType;

/** Résultat de la recommandation (objet de lecture, non persisté). */
public record Recommandation(Long equipeId, String equipeNom, SpecialityType specialite,
                             int effectif, Long associationId, String associationNom,
                             double distanceKm, double score) {}
