package tn.esprit.ms6association.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.ms6association.Entity.Equipe;
import tn.esprit.ms6association.Entity.SpecialityType;

import java.util.List;

public interface EquipeRepository extends JpaRepository<Equipe, Long> {

    List<Equipe> findByAssociationId(Long associationId);

    @Query("select e from Equipe e join fetch e.association " +
            "where e.specialite = :type and e.disponible = true")
    List<Equipe> findDisponiblesBySpecialite(@Param("type") SpecialityType type);
}
