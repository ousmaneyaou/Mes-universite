package nig.campus.com.service;

import java.util.Optional;
import nig.campus.com.domain.Filiere;
import nig.campus.com.repository.FiliereRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Filiere}.
 */
@Service
@Transactional
public class FiliereService {

    private final Logger log = LoggerFactory.getLogger(FiliereService.class);

    private final FiliereRepository filiereRepository;

    public FiliereService(FiliereRepository filiereRepository) {
        this.filiereRepository = filiereRepository;
    }

    /**
     * Save a filiere.
     *
     * @param filiere the entity to save.
     * @return the persisted entity.
     */
    public Filiere save(Filiere filiere) {
        log.debug("Request to save Filiere : {}", filiere);
        return filiereRepository.save(filiere);
    }

    /**
     * Update a filiere.
     *
     * @param filiere the entity to save.
     * @return the persisted entity.
     */
    public Filiere update(Filiere filiere) {
        log.debug("Request to update Filiere : {}", filiere);
        return filiereRepository.save(filiere);
    }

    /**
     * Partially update a filiere.
     *
     * @param filiere the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Filiere> partialUpdate(Filiere filiere) {
        log.debug("Request to partially update Filiere : {}", filiere);

        return filiereRepository
            .findById(filiere.getId())
            .map(existingFiliere -> {
                if (filiere.getLibelle() != null) {
                    existingFiliere.setLibelle(filiere.getLibelle());
                }

                return existingFiliere;
            })
            .map(filiereRepository::save);
    }

    /**
     * Get all the filieres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Filiere> findAll(Pageable pageable) {
        log.debug("Request to get all Filieres");
        return filiereRepository.findAll(pageable);
    }

    /**
     * Get one filiere by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Filiere> findOne(Long id) {
        log.debug("Request to get Filiere : {}", id);
        return filiereRepository.findById(id);
    }

    /**
     * Delete the filiere by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Filiere : {}", id);
        filiereRepository.deleteById(id);
    }
}
