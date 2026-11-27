package tn.esprit.ms2incidentneeds.services;

import tn.esprit.ms2incidentneeds.entities.Evaluation;
import tn.esprit.ms2incidentneeds.enums.NiveauGravite;

import java.util.List;

public interface IEvaluationService {

    Evaluation createEvaluation(Evaluation evaluation);

    List<Evaluation> getAllEvaluations();

    Evaluation getEvaluationById(Long id);

    Evaluation getEvaluationByIncidentId(Long incidentId);

    List<Evaluation> getEvaluationsByNiveau(NiveauGravite niveau);

    Evaluation updateEvaluation(Long id, Evaluation evaluation);

    Evaluation recalculerEvaluation(Long id);

    void deleteEvaluation(Long id);
}
