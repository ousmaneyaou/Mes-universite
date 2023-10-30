package nig.campus.com.service;

import java.util.Optional;
import nig.campus.com.domain.Dossier;
import nig.campus.com.repository.DossierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Dossier}.
 */
@Service
@Transactional
public class DossierService {

    private final Logger log = LoggerFactory.getLogger(DossierService.class);

    private final DossierRepository dossierRepository;

    public DossierService(DossierRepository dossierRepository) {
        this.dossierRepository = dossierRepository;
    }

    /**
     * Save a dossier.
     *
     * @param dossier the entity to save.
     * @return the persisted entity.
     */
    public Dossier save(Dossier dossier) {
        log.debug("Request to save Dossier : {}", dossier);
        return dossierRepository.save(dossier);
    }

    /**
     * Update a dossier.
     *
     * @param dossier the entity to save.
     * @return the persisted entity.
     */
    public Dossier update(Dossier dossier) {
        log.debug("Request to update Dossier : {}", dossier);
        return dossierRepository.save(dossier);
    }

    /**
     * Partially update a dossier.
     *
     * @param dossier the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Dossier> partialUpdate(Dossier dossier) {
        log.debug("Request to partially update Dossier : {}", dossier);

        return dossierRepository
            .findById(dossier.getId())
            .map(existingDossier -> {
                if (dossier.getValider() != null) {
                    existingDossier.setValider(dossier.getValider());
                }

                return existingDossier;
            })
            .map(dossierRepository::save);
    }

    /**
     * Get all the dossiers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Dossier> findAll(Pageable pageable) {
        log.debug("Request to get all Dossiers");
        return dossierRepository.findAll(pageable);
    }

    /**
     * Get one dossier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Dossier> findOne(Long id) {
        log.debug("Request to get Dossier : {}", id);
        return dossierRepository.findById(id);
    }

    /**
     * Delete the dossier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dossier : {}", id);
        dossierRepository.deleteById(id);
    }
}
