package tn.esprit.rescueaid.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.rescueaid.transport.entity.Driver;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByDisponibleTrue();

    boolean existsByNumeroPermis(String numeroPermis);
}