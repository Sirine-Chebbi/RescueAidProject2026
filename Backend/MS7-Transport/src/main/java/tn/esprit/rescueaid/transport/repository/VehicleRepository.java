package tn.esprit.rescueaid.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.rescueaid.transport.entity.Vehicle;
import tn.esprit.rescueaid.transport.enums.StatutVehicule;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByStatut(StatutVehicule statut);

    List<Vehicle> findByStatutAndVilleIgnoreCase(StatutVehicule statut, String ville);

    boolean existsByImmatriculation(String immatriculation);
}