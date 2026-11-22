package tn.esprit.ms6association.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms6association.Entity.*;
import tn.esprit.ms6association.Repository.EquipeRepository;
import tn.esprit.ms6association.Repository.InterventionRepository;
import tn.esprit.ms6association.Exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipeService {

    private final EquipeRepository equipeRepository;
    private final InterventionRepository interventionRepository;

    @Transactional(readOnly = true)
    public Equipe findById(Long id) {
        return get(id);
    }

    // ---------- Disponibilité d'une équipe ----------
    public Equipe updateAvailability(Long id, Boolean disponible, LocalDate debut, LocalDate fin) {
        Equipe e = get(id);
        if (debut != null && fin != null && fin.isBefore(debut)) {
            throw new IllegalArgumentException("dateFin doit être >= dateDebut");
        }
        if (disponible != null) e.setDisponible(disponible);
        e.setDateDebutDisponibilite(debut);
        e.setDateFinDisponibilite(fin);
        return equipeRepository.save(e);
    }

    // ---------- Recommandation d'équipes ----------
    @Transactional(readOnly = true)
    public List<Recommandation> recommend(SpecialityType type, double lat, double lon) {
        LocalDate today = LocalDate.now();

        List<Equipe> candidates = equipeRepository.findDisponiblesBySpecialite(type).stream()
                .filter(e -> e.estDisponibleLe(today))
                .filter(e -> distance(e, lat, lon) <= e.getAssociation().getRayonActionKm())
                .toList();

        int maxEffectif = candidates.stream().mapToInt(Equipe::getEffectif).max().orElse(1);

        return candidates.stream()
                .map(e -> {
                    Association a = e.getAssociation();
                    double d = distance(e, lat, lon);
                    double proximite = 1 - d / a.getRayonActionKm();         // 0..1
                    double taille = (double) e.getEffectif() / maxEffectif;  // 0..1
                    double score = Math.round((0.6 * proximite + 0.4 * taille) * 1000.0) / 1000.0;
                    return new Recommandation(e.getId(), e.getNom(), e.getSpecialite(),
                            e.getEffectif(), a.getId(), a.getNom(),
                            Math.round(d * 100.0) / 100.0, score);
                })
                .sorted(Comparator.comparingDouble(Recommandation::score).reversed())
                .toList();
    }

    // ---------- Interventions ----------
    public Intervention assign(Long equipeId, Intervention body) {
        Equipe e = get(equipeId);
        if (!e.estDisponibleLe(LocalDate.now())) {
            throw new IllegalArgumentException("L'équipe " + equipeId + " n'est pas disponible");
        }
        Intervention i = new Intervention();
        i.setEquipe(e);
        i.setIncidentId(body.getIncidentId());
        e.setDisponible(false); // l'équipe est mobilisée
        equipeRepository.save(e);
        return interventionRepository.save(i);
    }

    public Intervention updateInterventionStatus(Long interventionId, StatutIntervention statut) {
        Intervention i = interventionRepository.findById(interventionId)
                .orElseThrow(() -> new ResourceNotFoundException("Intervention " + interventionId + " introuvable"));
        i.setStatut(statut);
        // fin de mission : l'équipe redevient disponible
        if (statut == StatutIntervention.COMPLETED || statut == StatutIntervention.CANCELLED) {
            i.getEquipe().setDisponible(true);
        }
        return interventionRepository.save(i);
    }

    @Transactional(readOnly = true)
    public List<Intervention> interventionsOf(Long equipeId) {
        get(equipeId);
        return interventionRepository.findByEquipeId(equipeId);
    }

    // ---------- helpers ----------
    private Equipe get(Long id) {
        return equipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipe " + id + " introuvable"));
    }

    private double distance(Equipe e, double lat, double lon) {
        Association a = e.getAssociation();
        return haversineKm(lat, lon, a.getLatitude(), a.getLongitude());
    }

    static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double h = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * R * Math.asin(Math.sqrt(h));
    }
}
