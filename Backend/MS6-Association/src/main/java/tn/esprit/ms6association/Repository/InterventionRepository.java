package tn.esprit.ms6association.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms6association.Entity.Intervention;

import java.util.List;

public interface InterventionRepository extends JpaRepository<Intervention, Long> {
    List<Intervention> findByEquipeId(Long equipeId);
    List<Intervention> findByIncidentId(Long incidentId);
}
