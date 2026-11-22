package tn.esprit.ms6association.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms6association.Entity.Association;
import tn.esprit.ms6association.Entity.Equipe;
import tn.esprit.ms6association.Entity.SpecialityType;
import tn.esprit.ms6association.Service.AssociationService;
import tn.esprit.ms6association.Service.EquipeService;
import tn.esprit.ms6association.Service.Recommandation;

import java.util.List;

@RestController
@RequestMapping("/api/v1/associations")
@RequiredArgsConstructor
public class AssociationController {

    private final AssociationService service;
    private final EquipeService equipeService;

    @PostMapping
    public ResponseEntity<Association> create(@Valid @RequestBody Association a) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(a));
    }

    @GetMapping
    public List<Association> all() { return service.findAll(); }

    @GetMapping("/{id}")
    public Association one(@PathVariable Long id) { return service.findById(id); }

    @PutMapping("/{id}")
    public Association update(@PathVariable Long id, @Valid @RequestBody Association a) {
        return service.update(id, a);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }

    // GET /api/v1/associations/recommendations?type=MEDICAL&lat=36.80&lon=10.18
    @GetMapping("/recommendations")
    public List<Recommandation> recommendations(@RequestParam SpecialityType type,
                                                @RequestParam double lat,
                                                @RequestParam double lon) {
        return equipeService.recommend(type, lat, lon);
    }

    // ----- équipes d'une association -----
    @PostMapping("/{id}/equipes")
    public ResponseEntity<Equipe> addEquipe(@PathVariable Long id, @Valid @RequestBody Equipe e) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addEquipe(id, e));
    }

    @GetMapping("/{id}/equipes")
    public List<Equipe> equipes(@PathVariable Long id) { return service.equipesOf(id); }
}
