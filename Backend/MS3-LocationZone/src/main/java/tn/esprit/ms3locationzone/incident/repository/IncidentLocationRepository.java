package tn.esprit.ms3locationzone.incident.repository;

import tn.esprit.ms3locationzone.incident.entity.IncidentLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IncidentLocationRepository extends JpaRepository<IncidentLocation, Long> {
}