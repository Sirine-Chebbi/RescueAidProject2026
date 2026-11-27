package tn.esprit.ms2incidentneeds.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms2incidentneeds.entities.Besoin;
import tn.esprit.ms2incidentneeds.entities.Incident;
import tn.esprit.ms2incidentneeds.enums.Priorite;
import tn.esprit.ms2incidentneeds.enums.TypeBesoin;
import tn.esprit.ms2incidentneeds.exceptions.BusinessException;
import tn.esprit.ms2incidentneeds.exceptions.ResourceNotFoundException;
import tn.esprit.ms2incidentneeds.repositories.BesoinRepository;
import tn.esprit.ms2incidentneeds.repositories.IncidentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BesoinServiceImpl implements IBesoinService {

    private final BesoinRepository besoinRepository;
    private final IncidentRepository incidentRepository;

    @Override
    public Besoin createBesoin(Besoin request) {
        if (request.getIncidentId() == null) {
            throw new BusinessException("L'identifiant de l'incident est obligatoire");
        }
        Incident incident = findIncidentEntity(request.getIncidentId());
        Besoin besoin = Besoin.builder()
                .typeBesoin(request.getTypeBesoin())
                .quantite(request.getQuantite())
                .description(request.getDescription())
                .priorite(request.getPriorite() != null ? request.getPriorite() : Priorite.MOYENNE)
                .build();

        incident.ajouterBesoin(besoin);
        return besoinRepository.save(besoin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Besoin> getAllBesoins() {
        return besoinRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Besoin getBesoinById(Long id) {
        return findBesoinEntity(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Besoin> getBesoinsByIncidentId(Long incidentId) {
        return besoinRepository.findByIncidentId(incidentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Besoin> getBesoinsByType(TypeBesoin typeBesoin) {
        return besoinRepository.findByTypeBesoin(typeBesoin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Besoin> getBesoinsByPriorite(Priorite priorite) {
        return besoinRepository.findByPriorite(priorite);
    }

    @Override
    public Besoin updateBesoin(Long id, Besoin request) {
        Besoin besoin = findBesoinEntity(id);
        besoin.setTypeBesoin(request.getTypeBesoin());
        besoin.mettreAJourQuantite(request.getQuantite());
        besoin.setDescription(request.getDescription());
        if (request.getPriorite() != null) {
            besoin.changerPriorite(request.getPriorite());
        }
        if (request.getIncidentId() != null && !request.getIncidentId().equals(besoin.getIncidentId())) {
            Incident newIncident = findIncidentEntity(request.getIncidentId());
            if (besoin.getIncident() != null && besoin.getIncident().getBesoins() != null) {
                besoin.getIncident().getBesoins().remove(besoin);
            }
            newIncident.ajouterBesoin(besoin);
        }

        return besoinRepository.save(besoin);
    }

    @Override
    public Besoin mettreAJourQuantite(Long id, Integer quantite) {
        Besoin besoin = findBesoinEntity(id);
        besoin.mettreAJourQuantite(quantite);
        return besoinRepository.save(besoin);
    }

    @Override
    public Besoin changerPriorite(Long id, Priorite priorite) {
        Besoin besoin = findBesoinEntity(id);
        besoin.changerPriorite(priorite);
        return besoinRepository.save(besoin);
    }

    @Override
    public void deleteBesoin(Long id) {
        Besoin besoin = findBesoinEntity(id);
        if (besoin.getIncident() != null && besoin.getIncident().getBesoins() != null) {
            besoin.getIncident().getBesoins().remove(besoin);
        }
        besoinRepository.delete(besoin);
    }

    private Besoin findBesoinEntity(Long id) {
        return besoinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Besoin introuvable avec l'id : " + id));
    }

    private Incident findIncidentEntity(Long incidentId) {
        return incidentRepository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident introuvable avec l'id : " + incidentId));
    }
}
