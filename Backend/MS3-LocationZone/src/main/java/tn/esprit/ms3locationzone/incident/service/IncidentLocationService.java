package tn.esprit.ms3locationzone.incident.service;

import tn.esprit.ms3locationzone.common.exception.ResourceNotFoundException;
import tn.esprit.ms3locationzone.common.util.GeoUtils;
import tn.esprit.ms3locationzone.incident.dto.IncidentLocationRequest;
import tn.esprit.ms3locationzone.incident.dto.IncidentLocationResponse;
import tn.esprit.ms3locationzone.incident.entity.IncidentLocation;
import tn.esprit.ms3locationzone.incident.entity.Priorite;
import tn.esprit.ms3locationzone.incident.mapper.IncidentLocationMapper;
import tn.esprit.ms3locationzone.incident.repository.IncidentLocationRepository;
import tn.esprit.ms3locationzone.zone.entity.Zone;
import tn.esprit.ms3locationzone.zone.repository.ZoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class IncidentLocationService {

    private final IncidentLocationRepository repository;
    private final ZoneRepository zoneRepository;
    private final IncidentLocationMapper mapper;

    public IncidentLocationService(IncidentLocationRepository repository,
                                   ZoneRepository zoneRepository,
                                   IncidentLocationMapper mapper) {
        this.repository = repository;
        this.zoneRepository = zoneRepository;
        this.mapper = mapper;
    }

    // Insère ou met à jour : sera aussi appelé plus tard par l'écouteur RabbitMQ
    public IncidentLocationResponse upsert(IncidentLocationRequest request) {
        return upsert(request.incidentId(), request.latitude(), request.longitude(), request.priorite());
    }

    public IncidentLocationResponse upsert(Long incidentId, Double latitude, Double longitude, Priorite priorite) {
        GeoUtils.verifier(latitude, longitude);
        IncidentLocation loc = repository.findById(incidentId).orElseGet(IncidentLocation::new);
        loc.setIncidentId(incidentId);
        loc.setLatitude(latitude);
        loc.setLongitude(longitude);
        loc.setPriorite(priorite != null ? priorite : Priorite.BASSE);
        loc.setZoneId(zoneContenant(latitude, longitude));
        loc.setDateSynchro(LocalDateTime.now());
        return mapper.toResponse(repository.save(loc));
    }

    @Transactional(readOnly = true)
    public List<IncidentLocationResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public IncidentLocationResponse findById(Long incidentId) {
        return mapper.toResponse(repository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident " + incidentId + " introuvable")));
    }

    public void delete(Long incidentId) {
        IncidentLocation loc = repository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident " + incidentId + " introuvable"));
        repository.delete(loc);
    }

    // Les incidents situés dans le cercle d'une zone
    @Transactional(readOnly = true)
    public List<IncidentLocationResponse> findDansZone(Long zoneId) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone " + zoneId + " introuvable"));
        return repository.findAll().stream()
                .filter(i -> GeoUtils.distanceKm(i.getLatitude(), i.getLongitude(),
                        zone.getLatitudeCentre(), zone.getLongitudeCentre()) <= zone.getRayonKm())
                .map(mapper::toResponse)
                .toList();
    }

    // La zone la plus proche qui contient le point, sinon null
    private Long zoneContenant(double lat, double lon) {
        return zoneRepository.findAll().stream()
                .filter(z -> GeoUtils.distanceKm(lat, lon, z.getLatitudeCentre(), z.getLongitudeCentre()) <= z.getRayonKm())
                .min(Comparator.comparingDouble(
                        z -> GeoUtils.distanceKm(lat, lon, z.getLatitudeCentre(), z.getLongitudeCentre())))
                .map(Zone::getId)
                .orElse(null);
    }
}
