package tn.esprit.ms2incidentneeds.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms2incidentneeds.entities.Besoin;
import tn.esprit.ms2incidentneeds.enums.Priorite;
import tn.esprit.ms2incidentneeds.enums.TypeBesoin;
import tn.esprit.ms2incidentneeds.services.IBesoinService;

import java.util.List;

@RestController
@RequestMapping("/api/besoins")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Besoins", description = "CRUD et gestion des besoins liés aux incidents")
public class BesoinController {

    private final IBesoinService besoinService;

    @PostMapping
    @Operation(summary = "Créer un besoin")
    public ResponseEntity<Besoin> createBesoin(@Valid @RequestBody Besoin besoin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(besoinService.createBesoin(besoin));
    }

    @GetMapping
    @Operation(summary = "Lister tous les besoins")
    public ResponseEntity<List<Besoin>> getAllBesoins() {
        return ResponseEntity.ok(besoinService.getAllBesoins());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un besoin par ID")
    public ResponseEntity<Besoin> getBesoinById(@PathVariable Long id) {
        return ResponseEntity.ok(besoinService.getBesoinById(id));
    }

    @GetMapping("/incident/{incidentId}")
    @Operation(summary = "Lister les besoins d'un incident")
    public ResponseEntity<List<Besoin>> getBesoinsByIncidentId(@PathVariable Long incidentId) {
        return ResponseEntity.ok(besoinService.getBesoinsByIncidentId(incidentId));
    }

    @GetMapping("/type/{typeBesoin}")
    @Operation(summary = "Filtrer les besoins par type")
    public ResponseEntity<List<Besoin>> getBesoinsByType(@PathVariable TypeBesoin typeBesoin) {
        return ResponseEntity.ok(besoinService.getBesoinsByType(typeBesoin));
    }

    @GetMapping("/priorite/{priorite}")
    @Operation(summary = "Filtrer les besoins par priorité")
    public ResponseEntity<List<Besoin>> getBesoinsByPriorite(@PathVariable Priorite priorite) {
        return ResponseEntity.ok(besoinService.getBesoinsByPriorite(priorite));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un besoin")
    public ResponseEntity<Besoin> updateBesoin(
            @PathVariable Long id,
            @Valid @RequestBody Besoin besoin) {
        return ResponseEntity.ok(besoinService.updateBesoin(id, besoin));
    }

    @PatchMapping("/{id}/quantite")
    @Operation(summary = "Mettre à jour la quantité d'un besoin")
    public ResponseEntity<Besoin> mettreAJourQuantite(
            @PathVariable Long id,
            @RequestParam Integer quantite) {
        return ResponseEntity.ok(besoinService.mettreAJourQuantite(id, quantite));
    }

    @PatchMapping("/{id}/priorite")
    @Operation(summary = "Changer la priorité d'un besoin")
    public ResponseEntity<Besoin> changerPriorite(
            @PathVariable Long id,
            @RequestParam Priorite priorite) {
        return ResponseEntity.ok(besoinService.changerPriorite(id, priorite));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un besoin")
    public ResponseEntity<Void> deleteBesoin(@PathVariable Long id) {
        besoinService.deleteBesoin(id);
        return ResponseEntity.noContent().build();
    }
}
