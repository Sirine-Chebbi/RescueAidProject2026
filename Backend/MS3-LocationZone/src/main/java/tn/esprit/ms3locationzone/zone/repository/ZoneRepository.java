package tn.esprit.ms3locationzone.zone.repository;

import tn.esprit.ms3locationzone.zone.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ZoneRepository extends JpaRepository<Zone, Long> {
}