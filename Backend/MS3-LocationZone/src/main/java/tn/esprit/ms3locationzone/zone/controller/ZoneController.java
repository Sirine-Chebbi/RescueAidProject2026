package tn.esprit.ms3locationzone.zone.controller;

import jakarta.validation.Valid;
import tn.esprit.ms3locationzone.incident.dto.IncidentLocationResponse;
import tn.esprit.ms3locationzone.incident.service.IncidentLocationService;
import tn.esprit.ms3locationzone.zone.dto.ZoneRequest;
import tn.esprit.ms3locationzone.zone.dto.ZoneResponse;
import tn.esprit.ms3locationzone.zone.service.ZoneService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/zones")
public class ZoneController {

    private final ZoneService service;
    private final IncidentLocationService incidentService;

    public ZoneController(ZoneService service, IncidentLocationService incidentService) {
        this.service = service;
        this.incidentService = incidentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZoneResponse create(@Valid @RequestBody ZoneRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<ZoneResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ZoneResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public ZoneResponse update(@PathVariable Long id, @Valid @RequestBody ZoneRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // Les incidents situés dans la zone
    @GetMapping("/{id}/incidents")
    public List<IncidentLocationResponse> incidents(@PathVariable Long id) {
        return incidentService.findDansZone(id);
    }
}