package tn.esprit.ms3locationzone.zone.mapper;

import tn.esprit.ms3locationzone.zone.dto.ZoneRequest;
import tn.esprit.ms3locationzone.zone.dto.ZoneResponse;
import tn.esprit.ms3locationzone.zone.entity.Zone;
import org.springframework.stereotype.Component;

@Component
public class ZoneMapper {

    public Zone toEntity(ZoneRequest r) {
        Zone z = new Zone();
        updateEntity(z, r);
        return z;
    }

    public void updateEntity(Zone z, ZoneRequest r) {
        z.setNom(r.nom());
        z.setVille(r.ville());
        z.setLatitudeCentre(r.latitudeCentre());
        z.setLongitudeCentre(r.longitudeCentre());
        z.setRayonKm(r.rayonKm());
        if (r.niveauRisque() != null) {
            z.mettreAJourRisque(r.niveauRisque());
        }
    }

    public ZoneResponse toResponse(Zone z) {
        return new ZoneResponse(z.getId(), z.getNom(), z.getVille(),
                z.getLatitudeCentre(), z.getLongitudeCentre(),
                z.getRayonKm(), z.getNiveauRisque());
    }
}