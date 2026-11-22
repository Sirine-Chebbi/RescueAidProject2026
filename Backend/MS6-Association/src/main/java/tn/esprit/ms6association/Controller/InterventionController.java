package tn.esprit.ms6association.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms6association.Entity.Intervention;
import tn.esprit.ms6association.Entity.StatutIntervention;
import tn.esprit.ms6association.Service.EquipeService;

@RestController
@RequestMapping("/api/v1/interventions")
@RequiredArgsConstructor
public class InterventionController {

    private final EquipeService service;

    // PUT /api/v1/interventions/1/status?statut=ON_SITE
    @PutMapping("/{id}/status")
    public Intervention status(@PathVariable Long id, @RequestParam StatutIntervention statut) {
        return service.updateInterventionStatus(id, statut);
    }
}
