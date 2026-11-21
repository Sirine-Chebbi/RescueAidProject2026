package tn.esprit.ms3locationzone.zone.service;

import tn.esprit.ms3locationzone.common.exception.ConflictException;
import tn.esprit.ms3locationzone.common.exception.ResourceNotFoundException;
import tn.esprit.ms3locationzone.common.exception.ResourceNotFoundException;
import tn.esprit.ms3locationzone.point.repository.InterventionPointRepository;
import tn.esprit.ms3locationzone.zone.dto.ZoneRequest;
import tn.esprit.ms3locationzone.zone.dto.ZoneResponse;
import tn.esprit.ms3locationzone.zone.entity.Zone;
import tn.esprit.ms3locationzone.zone.mapper.ZoneMapper;
import tn.esprit.ms3locationzone.zone.repository.ZoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final InterventionPointRepository pointRepository;
    private final ZoneMapper mapper;

    public ZoneService(ZoneRepository zoneRepository,
                       InterventionPointRepository pointRepository,
                       ZoneMapper mapper) {
        this.zoneRepository = zoneRepository;
        this.pointRepository = pointRepository;
        this.mapper = mapper;
    }

    public ZoneResponse create(ZoneRequest request) {
        Zone zone = mapper.toEntity(request);
        return mapper.toResponse(zoneRepository.save(zone));
    }

    @Transactional(readOnly = true)
    public List<ZoneResponse> findAll() {
        return zoneRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ZoneResponse findById(Long id) {
        return mapper.toResponse(getZone(id));
    }

    public ZoneResponse update(Long id, ZoneRequest request) {
        Zone zone = getZone(id);
        mapper.updateEntity(zone, request);
        return mapper.toResponse(zoneRepository.save(zone));
    }

    public void delete(Long id) {
        Zone zone = getZone(id);
        if (pointRepository.existsByZoneId(id)) {
            throw new ConflictException("Impossible de supprimer : la zone contient encore des points d'intervention");
        }
        zoneRepository.delete(zone);
    }

    private Zone getZone(Long id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone " + id + " introuvable"));
    }
}