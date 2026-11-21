package tn.esprit.rescueaid.transport.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.rescueaid.transport.entity.Driver;
import tn.esprit.rescueaid.transport.repository.DriverRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DriverService {

    private final DriverRepository driverRepository;

    public Driver createDriver(Driver driver) {
        if (driverRepository.existsByNumeroPermis(driver.getNumeroPermis())) {
            throw new IllegalArgumentException("Un chauffeur avec ce numéro de permis existe déjà.");
        }
        if (driver.getDisponible() == null) {
            driver.setDisponible(true);
        }
        return driverRepository.save(driver);
    }

    @Transactional(readOnly = true)
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chauffeur introuvable avec l'id : " + id));
    }

    public Driver updateDriver(Long id, Driver updatedDriver) {
        Driver existing = getDriverById(id);
        existing.setNom(updatedDriver.getNom());
        existing.setTelephone(updatedDriver.getTelephone());
        existing.setNumeroPermis(updatedDriver.getNumeroPermis());
        if (updatedDriver.getDisponible() != null) {
            existing.mettreAJourDisponibilite(updatedDriver.getDisponible());
        }
        existing.setVehicleId(updatedDriver.getVehicleId());
        return driverRepository.save(existing);
    }
}