package tn.esprit.ms3locationzone.point.repository;

import tn.esprit.ms3locationzone.point.entity.InterventionPoint;
import tn.esprit.ms3locationzone.point.entity.StatutPoint;
import tn.esprit.ms3locationzone.point.entity.TypePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterventionPointRepository extends JpaRepository<InterventionPoint, Long> {

    boolean existsByZoneId(Long zoneId);

    List<InterventionPoint> findByStatut(StatutPoint statut);

    List<InterventionPoint> findByStatutAndType(StatutPoint statut, TypePoint type);
}