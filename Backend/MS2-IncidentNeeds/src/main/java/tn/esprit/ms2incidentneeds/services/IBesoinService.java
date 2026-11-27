package tn.esprit.ms2incidentneeds.services;

import tn.esprit.ms2incidentneeds.entities.Besoin;
import tn.esprit.ms2incidentneeds.enums.Priorite;
import tn.esprit.ms2incidentneeds.enums.TypeBesoin;

import java.util.List;

public interface IBesoinService {

    Besoin createBesoin(Besoin besoin);

    List<Besoin> getAllBesoins();

    Besoin getBesoinById(Long id);

    List<Besoin> getBesoinsByIncidentId(Long incidentId);

    List<Besoin> getBesoinsByType(TypeBesoin typeBesoin);

    List<Besoin> getBesoinsByPriorite(Priorite priorite);

    Besoin updateBesoin(Long id, Besoin besoin);

    Besoin mettreAJourQuantite(Long id, Integer quantite);

    Besoin changerPriorite(Long id, Priorite priorite);

    void deleteBesoin(Long id);
}
