package tn.esprit.rescueaid.transport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.rescueaid.transport.entity.Driver;
import tn.esprit.rescueaid.transport.entity.Trip;
import tn.esprit.rescueaid.transport.entity.Vehicle;
import tn.esprit.rescueaid.transport.enums.StatutTrip;
import tn.esprit.rescueaid.transport.enums.StatutVehicule;
import tn.esprit.rescueaid.transport.repository.TripRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TripService {

    private final TripRepository tripRepository;
    private final VehicleService vehicleService;
    private final DriverService driverService;

    public Trip planTrip(Trip trip) {
        Vehicle vehicle = vehicleService.getVehicleById(trip.getVehicleId());
        if (!vehicle.verifierDisponibilite()) {
            throw new IllegalStateException("Le véhicule sélectionné n'est pas disponible.");
        }

        Driver driver = driverService.getDriverById(trip.getDriverId());
        if (Boolean.FALSE.equals(driver.getDisponible())) {
            throw new IllegalStateException("Le chauffeur sélectionné n'est pas disponible.");
        }

        if (trip.getChargeKg() != null && vehicle.getCapaciteKg() != null
                && trip.getChargeKg() > vehicle.getCapaciteKg()) {
            throw new IllegalArgumentException("La charge (kg) dépasse la capacité maximale du véhicule.");
        }

        if (trip.getNombrePersonnes() != null && vehicle.getCapacitePersonnes() != null
                && trip.getNombrePersonnes() > vehicle.getCapacitePersonnes()) {
            throw new IllegalArgumentException("Le nombre de personnes dépasse la capacité du véhicule.");
        }

        trip.planifier();
        vehicle.setStatut(StatutVehicule.EN_MISSION);
        vehicleService.updateVehicle(vehicle.getId(), vehicle);

        driver.affecterVehicule(vehicle.getId());
        driverService.updateDriver(driver.getId(), driver);

        return tripRepository.save(trip);
    }

    @Transactional(readOnly = true)
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Trip getTripById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trajet introuvable avec l'id : " + id));
    }

    public Trip updateTrip(Long id, Trip updatedTrip) {
        Trip existing = getTripById(id);
        existing.setPointDepart(updatedTrip.getPointDepart());
        existing.setDestination(updatedTrip.getDestination());
        existing.setTypeTrajet(updatedTrip.getTypeTrajet());
        existing.setChargeKg(updatedTrip.getChargeKg());
        existing.setNombrePersonnes(updatedTrip.getNombrePersonnes());
        existing.setIncidentId(updatedTrip.getIncidentId());
        existing.setDistributionId(updatedTrip.getDistributionId());
        existing.setDateDepart(updatedTrip.getDateDepart());
        existing.setDateArrivee(updatedTrip.getDateArrivee());

        if (updatedTrip.getStatut() != null && updatedTrip.getStatut() != existing.getStatut()) {
            existing.mettreAJourStatut(updatedTrip.getStatut());
            syncResourcesOnStatusChange(existing);
        }

        return tripRepository.save(existing);
    }

    public void deleteTrip(Long id) {
        Trip existing = getTripById(id);
        if (existing.getStatut() == StatutTrip.PLANIFIE || existing.getStatut() == StatutTrip.EN_COURS) {
            existing.mettreAJourStatut(StatutTrip.ANNULE);
            syncResourcesOnStatusChange(existing);
        }
        tripRepository.delete(existing);
    }

    private void syncResourcesOnStatusChange(Trip trip) {
        Vehicle vehicle = vehicleService.getVehicleById(trip.getVehicleId());
        Driver driver = driverService.getDriverById(trip.getDriverId());

        if (trip.getStatut() == StatutTrip.EN_COURS) {
            vehicle.setStatut(StatutVehicule.EN_MISSION);
            driver.setDisponible(false);
            driver.setVehicleId(vehicle.getId());
        } else if (trip.getStatut() == StatutTrip.TERMINE || trip.getStatut() == StatutTrip.ANNULE) {
            vehicle.setStatut(StatutVehicule.DISPONIBLE);
            driver.setDisponible(true);
            driver.setVehicleId(null);
        }

        vehicleService.updateVehicle(vehicle.getId(), vehicle);
        driverService.updateDriver(driver.getId(), driver);
    }
}