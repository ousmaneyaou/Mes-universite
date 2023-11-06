package nig.campus.com.service;

import java.util.List;
import java.util.Optional;

import nig.campus.com.domain.Faculte;
import nig.campus.com.domain.Universite;
import nig.campus.com.repository.UniversiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Universite}.
 */
@Service
@Transactional
public class UniversiteService {

    private final Logger log = LoggerFactory.getLogger(UniversiteService.class);

    private final UniversiteRepository universiteRepository;

    public UniversiteService(UniversiteRepository universiteRepository) {
        this.universiteRepository = universiteRepository;
    }

    /**
     * Save a universite.
     *
     * @param universite the entity to save.
     * @return the persisted entity.
     */
    public Universite save(Universite universite) {
        log.debug("Request to save Universite : {}", universite);
        return universiteRepository.save(universite);
    }



    /**
     * Update a universite.
     *
     * @param universite the entity to save.
     * @return the persisted entity.
     */
    public Universite update(Universite universite) {
        log.debug("Request to update Universite : {}", universite);
        return universiteRepository.save(universite);
    }

    /**
     * Partially update a universite.
     *
     * @param universite the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Universite> partialUpdate(Universite universite) {
        log.debug("Request to partially update Universite : {}", universite);

        return universiteRepository
            .findById(universite.getId())
            .map(existingUniversite -> {
                if (universite.getLibelle() != null) {
                    existingUniversite.setLibelle(universite.getLibelle());
                }

                return existingUniversite;
            })
            .map(universiteRepository::save);
    }

    /**
     * Get all the universites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Universite> findAll(Pageable pageable) {
        log.debug("Request to get all Universites");
        return universiteRepository.findAll(pageable);
    }

    /**
     * Get one universite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Universite> findOne(Long id) {
        log.debug("Request to get Universite : {}", id);
        return universiteRepository.findById(id);
    }

    /**
     * Delete the universite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Universite : {}", id);
        universiteRepository.deleteById(id);
    }
}
