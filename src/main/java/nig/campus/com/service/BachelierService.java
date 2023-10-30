package nig.campus.com.service;

import java.util.Optional;
import nig.campus.com.domain.Bachelier;
import nig.campus.com.repository.BachelierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Bachelier}.
 */
@Service
@Transactional
public class BachelierService {

    private final Logger log = LoggerFactory.getLogger(BachelierService.class);

    private final BachelierRepository bachelierRepository;

    public BachelierService(BachelierRepository bachelierRepository) {
        this.bachelierRepository = bachelierRepository;
    }

    /**
     * Save a bachelier.
     *
     * @param bachelier the entity to save.
     * @return the persisted entity.
     */
    public Bachelier save(Bachelier bachelier) {
        log.debug("Request to save Bachelier : {}", bachelier);
        return bachelierRepository.save(bachelier);
    }

    /**
     * Update a bachelier.
     *
     * @param bachelier the entity to save.
     * @return the persisted entity.
     */
    public Bachelier update(Bachelier bachelier) {
        log.debug("Request to update Bachelier : {}", bachelier);
        return bachelierRepository.save(bachelier);
    }

    /**
     * Partially update a bachelier.
     *
     * @param bachelier the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Bachelier> partialUpdate(Bachelier bachelier) {
        log.debug("Request to partially update Bachelier : {}", bachelier);

        return bachelierRepository
            .findById(bachelier.getId())
            .map(existingBachelier -> {
                if (bachelier.getSexe() != null) {
                    existingBachelier.setSexe(bachelier.getSexe());
                }
                if (bachelier.getDateNaissance() != null) {
                    existingBachelier.setDateNaissance(bachelier.getDateNaissance());
                }
                if (bachelier.getLieuNaissance() != null) {
                    existingBachelier.setLieuNaissance(bachelier.getLieuNaissance());
                }
                if (bachelier.getNationalite() != null) {
                    existingBachelier.setNationalite(bachelier.getNationalite());
                }
                if (bachelier.getTelephone() != null) {
                    existingBachelier.setTelephone(bachelier.getTelephone());
                }

                return existingBachelier;
            })
            .map(bachelierRepository::save);
    }

    /**
     * Get all the bacheliers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Bachelier> findAll(Pageable pageable) {
        log.debug("Request to get all Bacheliers");
        return bachelierRepository.findAll(pageable);
    }

    /**
     * Get one bachelier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Bachelier> findOne(Long id) {
        log.debug("Request to get Bachelier : {}", id);
        return bachelierRepository.findById(id);
    }

    /**
     * Delete the bachelier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Bachelier : {}", id);
        bachelierRepository.deleteById(id);
    }
}
