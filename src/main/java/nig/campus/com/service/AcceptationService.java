package nig.campus.com.service;

import java.util.Optional;
import nig.campus.com.domain.Acceptation;
import nig.campus.com.repository.AcceptationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Acceptation}.
 */
@Service
@Transactional
public class AcceptationService {

    private final Logger log = LoggerFactory.getLogger(AcceptationService.class);

    private final AcceptationRepository acceptationRepository;

    public AcceptationService(AcceptationRepository acceptationRepository) {
        this.acceptationRepository = acceptationRepository;
    }

    /**
     * Save a acceptation.
     *
     * @param acceptation the entity to save.
     * @return the persisted entity.
     */
    public Acceptation save(Acceptation acceptation) {
        log.debug("Request to save Acceptation : {}", acceptation);
        return acceptationRepository.save(acceptation);
    }

    /**
     * Update a acceptation.
     *
     * @param acceptation the entity to save.
     * @return the persisted entity.
     */
    public Acceptation update(Acceptation acceptation) {
        log.debug("Request to update Acceptation : {}", acceptation);
        return acceptationRepository.save(acceptation);
    }

    /**
     * Partially update a acceptation.
     *
     * @param acceptation the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Acceptation> partialUpdate(Acceptation acceptation) {
        log.debug("Request to partially update Acceptation : {}", acceptation);

        return acceptationRepository
            .findById(acceptation.getId())
            .map(existingAcceptation -> {
                if (acceptation.getFiliere() != null) {
                    existingAcceptation.setFiliere(acceptation.getFiliere());
                }
                if (acceptation.getDateAcceptation() != null) {
                    existingAcceptation.setDateAcceptation(acceptation.getDateAcceptation());
                }

                return existingAcceptation;
            })
            .map(acceptationRepository::save);
    }

    /**
     * Get all the acceptations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Acceptation> findAll(Pageable pageable) {
        log.debug("Request to get all Acceptations");
        return acceptationRepository.findAll(pageable);
    }

    /**
     * Get one acceptation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Acceptation> findOne(Long id) {
        log.debug("Request to get Acceptation : {}", id);
        return acceptationRepository.findById(id);
    }

    /**
     * Delete the acceptation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Acceptation : {}", id);
        acceptationRepository.deleteById(id);
    }
}
