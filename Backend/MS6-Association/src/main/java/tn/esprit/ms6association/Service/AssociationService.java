package tn.esprit.ms6association.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms6association.Entity.Association;
import tn.esprit.ms6association.Entity.Equipe;
import tn.esprit.ms6association.Repository.AssociationRepository;
import tn.esprit.ms6association.Repository.EquipeRepository;
import tn.esprit.ms6association.Exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssociationService {

    private final AssociationRepository associationRepository;
    private final EquipeRepository equipeRepository;

    public Association create(Association a) {
        a.setId(null);
        return associationRepository.save(a);
    }

    @Transactional(readOnly = true)
    public List<Association> findAll() {
        return associationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Association findById(Long id) {
        return get(id);
    }

    public Association update(Long id, Association data) {
        Association a = get(id);
        a.setNom(data.getNom());
        a.setAdresse(data.getAdresse());
        a.setTelephone(data.getTelephone());
        a.setEmailContact(data.getEmailContact());
        a.setLatitude(data.getLatitude());
        a.setLongitude(data.getLongitude());
        a.setRayonActionKm(data.getRayonActionKm());
        a.setUserId(data.getUserId());
        return associationRepository.save(a);
    }

    public void delete(Long id) {
        associationRepository.delete(get(id));
    }

    // ---------- Equipes d'une association ----------
    public Equipe addEquipe(Long associationId, Equipe e) {
        e.setId(null);
        e.setAssociation(get(associationId));
        return equipeRepository.save(e);
    }

    @Transactional(readOnly = true)
    public List<Equipe> equipesOf(Long associationId) {
        get(associationId);
        return equipeRepository.findByAssociationId(associationId);
    }

    private Association get(Long id) {
        return associationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Association " + id + " introuvable"));
    }
}
