package tn.esprit.ms2incidentneeds.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2incidentneeds.entities.Evaluation;
import tn.esprit.ms2incidentneeds.entities.Incident;
import tn.esprit.ms2incidentneeds.enums.NiveauGravite;
import tn.esprit.ms2incidentneeds.exceptions.BusinessException;
import tn.esprit.ms2incidentneeds.exceptions.ResourceNotFoundException;
import tn.esprit.ms2incidentneeds.repositories.EvaluationRepository;
import tn.esprit.ms2incidentneeds.repositories.IncidentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationServiceImpl implements IEvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final IncidentRepository incidentRepository;

    @Override
    public Evaluation createEvaluation(Evaluation request) {
        if (request.getIncidentId() == null) {
            throw new BusinessException("L'identifiant de l'incident est obligatoire");
        }
        Incident incident = incidentRepository.findById(request.getIncidentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Incident introuvable avec l'id : " + request.getIncidentId()));

        if (evaluationRepository.existsByIncidentId(incident.getId())) {
            throw new BusinessException("L'incident " + incident.getId() + " possède déjà une évaluation");
        }

        Evaluation evaluation = Evaluation.builder()
                .incident(incident)
                .incidentId(incident.getId())
                .dateEvaluation(LocalDateTime.now())
                .build();

        applyScoreAndNiveau(evaluation, request.getScore(), request.getNiveau());
        incident.setEvaluation(evaluation);

        return evaluationRepository.save(evaluation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Evaluation getEvaluationById(Long id) {
        return findEvaluationEntity(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Evaluation getEvaluationByIncidentId(Long incidentId) {
        return evaluationRepository.findByIncidentId(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Évaluation introuvable pour l'incident : " + incidentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluation> getEvaluationsByNiveau(NiveauGravite niveau) {
        return evaluationRepository.findByNiveau(niveau);
    }

    @Override
    public Evaluation updateEvaluation(Long id, Evaluation request) {
        Evaluation evaluation = findEvaluationEntity(id);
        if (request.getIncidentId() != null && !request.getIncidentId().equals(evaluation.getIncidentId())) {
            Incident newIncident = incidentRepository.findById(request.getIncidentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Incident introuvable avec l'id : " + request.getIncidentId()));
            if (evaluationRepository.existsByIncidentId(newIncident.getId())) {
                throw new BusinessException("L'incident " + newIncident.getId() + " possède déjà une évaluation");
            }
            if (evaluation.getIncident() != null) {
                evaluation.getIncident().setEvaluation(null);
            }
            evaluation.setIncident(newIncident);
            evaluation.setIncidentId(newIncident.getId());
            newIncident.setEvaluation(evaluation);
        }

        applyScoreAndNiveau(evaluation, request.getScore(), request.getNiveau());
        evaluation.setDateEvaluation(LocalDateTime.now());

        return evaluationRepository.save(evaluation);
    }

    @Override
    public Evaluation recalculerEvaluation(Long id) {
        Evaluation evaluation = findEvaluationEntity(id);
        evaluation.calculerScore();
        evaluation.determinerNiveau();
        evaluation.setDateEvaluation(LocalDateTime.now());
        return evaluationRepository.save(evaluation);
    }

    @Override
    public void deleteEvaluation(Long id) {
        Evaluation evaluation = findEvaluationEntity(id);
        Incident incident = evaluation.getIncident();
        if (incident != null) {
            incident.setEvaluation(null);
        }
        evaluationRepository.delete(evaluation);
    }

    private Evaluation findEvaluationEntity(Long id) {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Évaluation introuvable avec l'id : " + id));
    }

    private void applyScoreAndNiveau(Evaluation evaluation, Double requestedScore, NiveauGravite requestedNiveau) {
        if (requestedScore != null) {
            evaluation.setScore(requestedScore);
        } else {
            evaluation.calculerScore();
        }

        if (requestedNiveau != null) {
            evaluation.setNiveau(requestedNiveau);
        } else {
            evaluation.determinerNiveau();
        }
    }
}
