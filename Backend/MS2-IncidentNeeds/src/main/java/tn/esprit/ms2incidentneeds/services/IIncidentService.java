package tn.esprit.ms2incidentneeds.services;

import tn.esprit.ms2incidentneeds.entities.Besoin;
import tn.esprit.ms2incidentneeds.entities.Incident;
import tn.esprit.ms2incidentneeds.enums.StatutIncident;
import tn.esprit.ms2incidentneeds.enums.TypeIncident;

import java.util.List;

public interface IIncidentService {

    Incident createIncident(Incident incident);

    List<Incident> getAllIncidents();

    Incident getIncidentById(Long id);

    Incident updateIncident(Long id, Incident incident);

    void deleteIncident(Long id);

    List<Incident> getIncidentsByStatut(StatutIncident statut);

    List<Incident> getIncidentsByType(TypeIncident type);

    List<Incident> getIncidentsByCitoyen(Long citoyenId);

    Incident mettreAJourStatut(Long id, StatutIncident statut);

    Besoin ajouterBesoin(Long incidentId, Besoin besoin);

    Incident evaluerGravite(Long id);
}
