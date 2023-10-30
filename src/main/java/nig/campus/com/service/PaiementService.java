package nig.campus.com.service;

import java.util.List;
import java.util.Optional;
import nig.campus.com.domain.Paiement;
import nig.campus.com.repository.PaiementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Paiement}.
 */
@Service
@Transactional
public class PaiementService {

    private final Logger log = LoggerFactory.getLogger(PaiementService.class);

    private final PaiementRepository paiementRepository;

    public PaiementService(PaiementRepository paiementRepository) {
        this.paiementRepository = paiementRepository;
    }

    /**
     * Save a paiement.
     *
     * @param paiement the entity to save.
     * @return the persisted entity.
     */
    public Paiement save(Paiement paiement) {
        log.debug("Request to save Paiement : {}", paiement);
        return paiementRepository.save(paiement);
    }

    /**
     * Update a paiement.
     *
     * @param paiement the entity to save.
     * @return the persisted entity.
     */
    public Paiement update(Paiement paiement) {
        log.debug("Request to update Paiement : {}", paiement);
        return paiementRepository.save(paiement);
    }

    /**
     * Partially update a paiement.
     *
     * @param paiement the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Paiement> partialUpdate(Paiement paiement) {
        log.debug("Request to partially update Paiement : {}", paiement);

        return paiementRepository
            .findById(paiement.getId())
            .map(existingPaiement -> {
                if (paiement.getDatePaie() != null) {
                    existingPaiement.setDatePaie(paiement.getDatePaie());
                }
                if (paiement.getEtat() != null) {
                    existingPaiement.setEtat(paiement.getEtat());
                }

                return existingPaiement;
            })
            .map(paiementRepository::save);
    }

    /**
     * Get all the paiements.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Paiement> findAll() {
        log.debug("Request to get all Paiements");
        return paiementRepository.findAll();
    }

    /**
     * Get one paiement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Paiement> findOne(Long id) {
        log.debug("Request to get Paiement : {}", id);
        return paiementRepository.findById(id);
    }

    /**
     * Delete the paiement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Paiement : {}", id);
        paiementRepository.deleteById(id);
    }
}
