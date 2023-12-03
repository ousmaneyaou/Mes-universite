package nig.campus.com.service;

import java.util.Optional;
import nig.campus.com.domain.Inscription;
import nig.campus.com.repository.InscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Inscription}.
 */
@Service
@Transactional
public class InscriptionService {

    private final Logger log = LoggerFactory.getLogger(InscriptionService.class);

    private final InscriptionRepository inscriptionRepository;

    public InscriptionService(InscriptionRepository inscriptionRepository) {
        this.inscriptionRepository = inscriptionRepository;
    }

    /**
     * Save a inscription.
     *
     * @param inscription the entity to save.
     * @return the persisted entity.
     */
    public Inscription save(Inscription inscription) {
        log.debug("Request to save Inscription : {}", inscription);
        return inscriptionRepository.save(inscription);
    }

    /**
     * Update a inscription.
     *
     * @param inscription the entity to save.
     * @return the persisted entity.
     */
    public Inscription update(Inscription inscription) {
        log.debug("Request to update Inscription : {}", inscription);
        return inscriptionRepository.save(inscription);
    }

    /**
     * Partially update a inscription.
     *
     * @param inscription the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Inscription> partialUpdate(Inscription inscription) {
        log.debug("Request to partially update Inscription : {}", inscription);

        return inscriptionRepository
            .findById(inscription.getId())
            .map(existingInscription -> {
                if (inscription.getDateInscription() != null) {
                    existingInscription.setDateInscription(inscription.getDateInscription());
                }
                if (inscription.getRegime() != null) {
                    existingInscription.setRegime(inscription.getRegime());
                }
                if (inscription.getAnneeAcademique() != null) {
                    existingInscription.setAnneeAcademique(inscription.getAnneeAcademique());
                }
                if (inscription.getMontantInscription() != null) {
                    existingInscription.setMontantInscription(inscription.getMontantInscription());
                }
                if (inscription.getNiveau() != null) {
                    existingInscription.setNiveau(inscription.getNiveau());
                }
                if (inscription.getObservation() != null) {
                    existingInscription.setObservation(inscription.getObservation());
                }

                return existingInscription;
            })
            .map(inscriptionRepository::save);
    }

    /**
     * Get all the inscriptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Inscription> findAll(Pageable pageable) {
        log.debug("Request to get all Inscriptions");
        return inscriptionRepository.findAll(pageable);
    }

    /**
     * Get one inscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Inscription> findOne(Long id) {
        log.debug("Request to get Inscription : {}", id);
        return inscriptionRepository.findById(id);
    }

    /**
     * Delete the inscription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Inscription : {}", id);
        inscriptionRepository.deleteById(id);
    }
}
