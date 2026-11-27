package tn.esprit.ms2incidentneeds.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.ms2incidentneeds.entities.Evaluation;
import tn.esprit.ms2incidentneeds.enums.NiveauGravite;
import tn.esprit.ms2incidentneeds.services.IEvaluationService;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Evaluations", description = "CRUD et calcul de gravité des évaluations")
public class EvaluationController {

    private final IEvaluationService evaluationService;

    @PostMapping
    @Operation(summary = "Créer une évaluation", description = "Crée une évaluation pour un incident (calcule score et niveau automatiquement si omis)")
    public ResponseEntity<Evaluation> createEvaluation(@Valid @RequestBody Evaluation evaluation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluationService.createEvaluation(evaluation));
    }

    @GetMapping
    @Operation(summary = "Lister toutes les évaluations")
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        return ResponseEntity.ok(evaluationService.getAllEvaluations());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une évaluation par ID")
    public ResponseEntity<Evaluation> getEvaluationById(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.getEvaluationById(id));
    }

    @GetMapping("/incident/{incidentId}")
    @Operation(summary = "Récupérer l'évaluation d'un incident")
    public ResponseEntity<Evaluation> getEvaluationByIncidentId(@PathVariable Long incidentId) {
        return ResponseEntity.ok(evaluationService.getEvaluationByIncidentId(incidentId));
    }

    @GetMapping("/niveau/{niveau}")
    @Operation(summary = "Filtrer les évaluations par niveau de gravité")
    public ResponseEntity<List<Evaluation>> getEvaluationsByNiveau(@PathVariable NiveauGravite niveau) {
        return ResponseEntity.ok(evaluationService.getEvaluationsByNiveau(niveau));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une évaluation")
    public ResponseEntity<Evaluation> updateEvaluation(
            @PathVariable Long id,
            @Valid @RequestBody Evaluation evaluation) {
        return ResponseEntity.ok(evaluationService.updateEvaluation(id, evaluation));
    }

    @PostMapping("/{id}/recalculer")
    @Operation(summary = "Recalculer le score et le niveau d'une évaluation")
    public ResponseEntity<Evaluation> recalculerEvaluation(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.recalculerEvaluation(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une évaluation")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.noContent().build();
    }
}
