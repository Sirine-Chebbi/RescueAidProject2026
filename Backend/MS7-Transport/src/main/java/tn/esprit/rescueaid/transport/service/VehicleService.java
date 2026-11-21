package tn.esprit.rescueaid.transport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.rescueaid.transport.entity.Vehicle;
import tn.esprit.rescueaid.transport.enums.StatutVehicule;
import tn.esprit.rescueaid.transport.repository.VehicleRepository;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public Vehicle createVehicle(Vehicle vehicle) {
        if (vehicleRepository.existsByImmatriculation(vehicle.getImmatriculation())) {
            throw new IllegalArgumentException("Un véhicule avec cette immatriculation existe déjà.");
        }
        if (vehicle.getStatut() == null) {
            vehicle.setStatut(StatutVehicule.DISPONIBLE);
        }
        return vehicleRepository.save(vehicle);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule introuvable avec l'id : " + id));
    }

    public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
        Vehicle existing = getVehicleById(id);
        existing.setImmatriculation(updatedVehicle.getImmatriculation());
        existing.setType(updatedVehicle.getType());
        existing.setCapaciteKg(updatedVehicle.getCapaciteKg());
        existing.setCapacitePersonnes(updatedVehicle.getCapacitePersonnes());
        if (updatedVehicle.getStatut() != null) {
            existing.setStatut(updatedVehicle.getStatut());
        }
        if (updatedVehicle.getLatitude() != null && updatedVehicle.getLongitude() != null) {
            existing.mettreAJourPosition(
                    updatedVehicle.getLatitude(),
                    updatedVehicle.getLongitude(),
                    updatedVehicle.getVille()
            );
        } else if (updatedVehicle.getVille() != null) {
            existing.setVille(updatedVehicle.getVille());
        }
        return vehicleRepository.save(existing);
    }

    public void deleteVehicle(Long id) {
        Vehicle existing = getVehicleById(id);
        vehicleRepository.delete(existing);
    }

    @Transactional(readOnly = true)
    public List<Vehicle> suggestVehicles(Double chargeKg, Integer nombrePersonnes, String ville) {
        double neededKg = chargeKg != null ? chargeKg : 0.0;
        int neededPeople = nombrePersonnes != null ? nombrePersonnes : 0;

        List<Vehicle> candidates = (ville != null && !ville.isBlank())
                ? vehicleRepository.findByStatutAndVilleIgnoreCase(StatutVehicule.DISPONIBLE, ville.trim())
                : vehicleRepository.findByStatut(StatutVehicule.DISPONIBLE);

        // Si aucune correspondance exacte dans la ville demandée, on élargit aux véhicules disponibles
        if (candidates.isEmpty() && ville != null && !ville.isBlank()) {
            candidates = vehicleRepository.findByStatut(StatutVehicule.DISPONIBLE);
        }

        return candidates.stream()
                .filter(Vehicle::verifierDisponibilite)
                .filter(v -> v.getCapaciteKg() != null && v.getCapaciteKg() >= neededKg)
                .filter(v -> v.getCapacitePersonnes() != null && v.getCapacitePersonnes() >= neededPeople)
                .sorted(Comparator
                        .comparing((Vehicle v) -> ville != null && !ville.isBlank()
                                && ville.trim().equalsIgnoreCase(v.getVille()) ? 0 : 1)
                        .thenComparing(v -> v.getCapaciteKg() - neededKg)
                        .thenComparing(v -> v.getCapacitePersonnes() - neededPeople))
                .toList();
    }
}