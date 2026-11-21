package tn.esprit.ms3locationzone.point.controller;

import jakarta.validation.Valid;
import tn.esprit.ms3locationzone.point.dto.InterventionPointRequest;
import tn.esprit.ms3locationzone.point.dto.InterventionPointResponse;
import tn.esprit.ms3locationzone.point.dto.OccupationRequest;
import tn.esprit.ms3locationzone.point.entity.TypePoint;
import tn.esprit.ms3locationzone.point.service.InterventionPointService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/points")
public class InterventionPointController {

    private final InterventionPointService service;

    public InterventionPointController(InterventionPointService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InterventionPointResponse create(@Valid @RequestBody InterventionPointRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<InterventionPointResponse> findAll(
            @RequestParam(value = "zoneId", required = false) Long zoneId,
            @RequestParam(value = "type", required = false) TypePoint type) {
        return service.findAll(zoneId, type);
    }

    @GetMapping("/{id}")
    public InterventionPointResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public InterventionPointResponse update(@PathVariable Long id,
                                            @Valid @RequestBody InterventionPointRequest request) {
        return service.update(id, request);
    }

    @PutMapping("/{id}/occupation")
    public InterventionPointResponse occupation(@PathVariable Long id,
                                                @Valid @RequestBody OccupationRequest request) {
        return service.mettreAJourOccupation(id, request.occupation());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}