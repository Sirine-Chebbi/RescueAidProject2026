package tn.esprit.ms3locationzone.point.service;

import tn.esprit.ms3locationzone.common.exception.ResourceNotFoundException;
import tn.esprit.ms3locationzone.point.dto.InterventionPointRequest;
import tn.esprit.ms3locationzone.point.dto.InterventionPointResponse;
import tn.esprit.ms3locationzone.point.entity.InterventionPoint;
import tn.esprit.ms3locationzone.point.entity.TypePoint;
import tn.esprit.ms3locationzone.point.mapper.InterventionPointMapper;
import tn.esprit.ms3locationzone.point.repository.InterventionPointRepository;
import tn.esprit.ms3locationzone.zone.repository.ZoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InterventionPointService {

    private final InterventionPointRepository repository;
    private final ZoneRepository zoneRepository;
    private final InterventionPointMapper mapper;

    public InterventionPointService(InterventionPointRepository repository,
                                    ZoneRepository zoneRepository,
                                    InterventionPointMapper mapper) {
        this.repository = repository;
        this.zoneRepository = zoneRepository;
        this.mapper = mapper;
    }

    public InterventionPointResponse create(InterventionPointRequest request) {
        verifierZone(request.zoneId());
        InterventionPoint point = mapper.toEntity(request);
        return mapper.toResponse(repository.save(point));
    }

    @Transactional(readOnly = true)
    public List<InterventionPointResponse> findAll(Long zoneId, TypePoint type) {
        return repository.findAll().stream()
                .filter(p -> zoneId == null || zoneId.equals(p.getZoneId()))
                .filter(p -> type == null || type == p.getType())
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public InterventionPointResponse findById(Long id) {
        return mapper.toResponse(getPoint(id));
    }

    public InterventionPointResponse update(Long id, InterventionPointRequest request) {
        InterventionPoint point = getPoint(id);
        verifierZone(request.zoneId());
        mapper.updateEntity(point, request);
        return mapper.toResponse(repository.save(point));
    }

    // Change l'occupation : le statut devient SATURE ou ACTIF automatiquement
    public InterventionPointResponse mettreAJourOccupation(Long id, int occupation) {
        InterventionPoint point = getPoint(id);
        if (occupation > point.getCapaciteMax()) {
            throw new IllegalArgumentException("Occupation (" + occupation
                    + ") superieure a la capacite max (" + point.getCapaciteMax() + ")");
        }
        point.mettreAJourOccupation(occupation);
        return mapper.toResponse(repository.save(point));
    }

    public void delete(Long id) {
        repository.delete(getPoint(id));
    }

    private InterventionPoint getPoint(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Point d'intervention " + id + " introuvable"));
    }

    private void verifierZone(Long zoneId) {
        if (!zoneRepository.existsById(zoneId)) {
            throw new ResourceNotFoundException("Zone " + zoneId + " introuvable");
        }
    }
}