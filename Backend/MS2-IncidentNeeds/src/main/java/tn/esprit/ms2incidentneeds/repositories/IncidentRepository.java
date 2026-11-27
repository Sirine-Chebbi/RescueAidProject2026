package tn.esprit.ms2incidentneeds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.ms2incidentneeds.entities.Incident;
import tn.esprit.ms2incidentneeds.enums.StatutIncident;
import tn.esprit.ms2incidentneeds.enums.TypeIncident;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findByStatut(StatutIncident statut);

    List<Incident> findByType(TypeIncident type);

    List<Incident> findByCitoyenId(Long citoyenId);

    List<Incident> findByLocalisationContainingIgnoreCase(String localisation);
}
