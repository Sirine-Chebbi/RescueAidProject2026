package tn.esprit.ms3locationzone.incident.controller;

import jakarta.validation.Valid;
import tn.esprit.ms3locationzone.incident.dto.IncidentLocationRequest;
import tn.esprit.ms3locationzone.incident.dto.IncidentLocationResponse;
import tn.esprit.ms3locationzone.incident.service.IncidentLocationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/incident-locations")
public class IncidentLocationController {

    private final IncidentLocationService service;

    public IncidentLocationController(IncidentLocationService service) {
        this.service = service;
    }

    // Simule la réception d'un IncidentReportedEvent (insère ou met à jour)
    @PostMapping
    public IncidentLocationResponse upsert(@Valid @RequestBody IncidentLocationRequest request) {
        return service.upsert(request);
    }

    @GetMapping
    public List<IncidentLocationResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{incidentId}")
    public IncidentLocationResponse findById(@PathVariable Long incidentId) {
        return service.findById(incidentId);
    }

    @DeleteMapping("/{incidentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long incidentId) {
        service.delete(incidentId);
    }
}