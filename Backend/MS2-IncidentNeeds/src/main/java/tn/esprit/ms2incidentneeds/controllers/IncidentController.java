package tn.esprit.ms2incidentneeds.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms2incidentneeds.entities.Besoin;
import tn.esprit.ms2incidentneeds.entities.Incident;
import tn.esprit.ms2incidentneeds.enums.StatutIncident;
import tn.esprit.ms2incidentneeds.enums.TypeIncident;
import tn.esprit.ms2incidentneeds.services.IIncidentService;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Incidents", description = "CRUD et gestion métier des incidents")
public class IncidentController {

    private final IIncidentService incidentService;

    @PostMapping
    @Operation(summary = "Créer un incident", description = "Enregistre un nouvel incident signalé par un citoyen")
    public ResponseEntity<Incident> createIncident(@Valid @RequestBody Incident incident) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incidentService.createIncident(incident));
    }

    @GetMapping
    @Operation(summary = "Lister tous les incidents")
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un incident par ID")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un incident")
    public ResponseEntity<Incident> updateIncident(
            @PathVariable Long id,
            @Valid @RequestBody Incident incident) {
        return ResponseEntity.ok(incidentService.updateIncident(id, incident));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un incident")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Filtrer les incidents par statut")
    public ResponseEntity<List<Incident>> getIncidentsByStatut(@PathVariable StatutIncident statut) {
        return ResponseEntity.ok(incidentService.getIncidentsByStatut(statut));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Filtrer les incidents par type")
    public ResponseEntity<List<Incident>> getIncidentsByType(@PathVariable TypeIncident type) {
        return ResponseEntity.ok(incidentService.getIncidentsByType(type));
    }

    @GetMapping("/citoyen/{citoyenId}")
    @Operation(summary = "Lister les incidents signalés par un citoyen")
    public ResponseEntity<List<Incident>> getIncidentsByCitoyen(@PathVariable Long citoyenId) {
        return ResponseEntity.ok(incidentService.getIncidentsByCitoyen(citoyenId));
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Mettre à jour le statut d'un incident")
    public ResponseEntity<Incident> mettreAJourStatut(
            @PathVariable Long id,
            @RequestParam StatutIncident statut) {
        return ResponseEntity.ok(incidentService.mettreAJourStatut(id, statut));
    }

    @PostMapping("/{id}/besoins")
    @Operation(summary = "Ajouter un besoin à un incident")
    public ResponseEntity<Besoin> ajouterBesoin(
            @PathVariable Long id,
            @Valid @RequestBody Besoin besoin) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incidentService.ajouterBesoin(id, besoin));
    }

    @PostMapping("/{id}/evaluer")
    @Operation(summary = "Évaluer la gravité d'un incident", description = "Calcule automatiquement le score et le niveau de gravité de l'incident")
    public ResponseEntity<Incident> evaluerGravite(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.evaluerGravite(id));
    }
}
