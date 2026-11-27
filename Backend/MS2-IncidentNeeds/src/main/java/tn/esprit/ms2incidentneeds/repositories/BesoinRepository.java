package tn.esprit.ms2incidentneeds.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.ms2incidentneeds.entities.Besoin;
import tn.esprit.ms2incidentneeds.enums.Priorite;
import tn.esprit.ms2incidentneeds.enums.TypeBesoin;

import java.util.List;

@Repository
public interface BesoinRepository extends JpaRepository<Besoin, Long> {

    List<Besoin> findByIncidentId(Long incidentId);

    List<Besoin> findByTypeBesoin(TypeBesoin typeBesoin);

    List<Besoin> findByPriorite(Priorite priorite);
}
