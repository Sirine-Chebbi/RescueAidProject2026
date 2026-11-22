package tn.esprit.ms6association.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms6association.Entity.Equipe;
import tn.esprit.ms6association.Entity.Intervention;
import tn.esprit.ms6association.Service.EquipeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/equipes")
@RequiredArgsConstructor
public class EquipeController {

    private final EquipeService service;

    @GetMapping("/{id}")
    public Equipe one(@PathVariable Long id) { return service.findById(id); }

    // PUT /api/v1/equipes/1/availability?disponible=true&dateDebut=2026-11-25&dateFin=2026-12-05
    @PutMapping("/{id}/availability")
    public Equipe availability(@PathVariable Long id,
                               @RequestParam(required = false) Boolean disponible,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        return service.updateAvailability(id, disponible, dateDebut, dateFin);
    }

    // body : {"incidentId": 12}
    @PostMapping("/{id}/interventions")
    public ResponseEntity<Intervention> assign(@PathVariable Long id, @Valid @RequestBody Intervention i) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.assign(id, i));
    }

    @GetMapping("/{id}/interventions")
    public List<Intervention> interventions(@PathVariable Long id) {
        return service.interventionsOf(id);
    }
}
