package tn.esprit.ms2incidentneeds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.ms2incidentneeds.entities.Evaluation;
import tn.esprit.ms2incidentneeds.enums.NiveauGravite;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    Optional<Evaluation> findByIncidentId(Long incidentId);

    boolean existsByIncidentId(Long incidentId);

    List<Evaluation> findByNiveau(NiveauGravite niveau);
}
