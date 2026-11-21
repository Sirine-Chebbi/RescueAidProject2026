package tn.esprit.ms3locationzone.point.mapper;

import tn.esprit.ms3locationzone.point.dto.InterventionPointRequest;
import tn.esprit.ms3locationzone.point.dto.InterventionPointResponse;
import tn.esprit.ms3locationzone.point.entity.InterventionPoint;
import tn.esprit.ms3locationzone.point.entity.StatutPoint;
import org.springframework.stereotype.Component;

@Component
public class InterventionPointMapper {

    public InterventionPoint toEntity(InterventionPointRequest r) {
        InterventionPoint p = new InterventionPoint();
        updateEntity(p, r);
        return p;
    }

    public void updateEntity(InterventionPoint p, InterventionPointRequest r) {
        int occupation = r.occupationActuelle() != null ? r.occupationActuelle() : 0;
        if (occupation > r.capaciteMax()) {
            throw new IllegalArgumentException("occupationActuelle ne peut pas depasser capaciteMax");
        }
        p.setZoneId(r.zoneId());
        p.setNom(r.nom());
        p.setType(r.type());
        p.setLatitude(r.latitude());
        p.setLongitude(r.longitude());
        p.setCapaciteMax(r.capaciteMax());
        p.setStatut(r.statut() != null ? r.statut() : StatutPoint.ACTIF);
        p.mettreAJourOccupation(occupation); // recalcule ACTIF / SATURE (FERME reste FERME)
    }

    public InterventionPointResponse toResponse(InterventionPoint p) {
        return new InterventionPointResponse(p.getId(), p.getZoneId(), p.getNom(), p.getType(),
                p.getLatitude(), p.getLongitude(), p.getCapaciteMax(),
                p.getOccupationActuelle(), p.placesLibres(), p.getStatut());
    }
}