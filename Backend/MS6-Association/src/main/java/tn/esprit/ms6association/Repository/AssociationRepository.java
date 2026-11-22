package tn.esprit.ms6association.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.ms6association.Entity.Association;

public interface AssociationRepository extends JpaRepository<Association, Long> {
}
