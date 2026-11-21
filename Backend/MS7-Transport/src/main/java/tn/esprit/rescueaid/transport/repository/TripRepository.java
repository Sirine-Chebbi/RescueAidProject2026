package tn.esprit.rescueaid.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.rescueaid.transport.entity.Trip;
import tn.esprit.rescueaid.transport.enums.StatutTrip;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByStatut(StatutTrip statut);

    List<Trip> findByIncidentId(Long incidentId);

    List<Trip> findByDistributionId(Long distributionId);
}