package nig.campus.com.service;

import java.util.List;
import java.util.Optional;
import nig.campus.com.domain.Departement;
import nig.campus.com.repository.DepartementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Departement}.
 */
@Service
@Transactional
public class DepartementService {

    private final Logger log = LoggerFactory.getLogger(DepartementService.class);

    private final DepartementRepository departementRepository;

    public DepartementService(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    /**
     * Save a departement.
     *
     * @param departement the entity to save.
     * @return the persisted entity.
     */
    public Departement save(Departement departement) {
        log.debug("Request to save Departement : {}", departement);
        return departementRepository.save(departement);
    }

    /**
     * Update a departement.
     *
     * @param departement the entity to save.
     * @return the persisted entity.
     */
    public Departement update(Departement departement) {
        log.debug("Request to update Departement : {}", departement);
        return departementRepository.save(departement);
    }

    /**
     * Partially update a departement.
     *
     * @param departement the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Departement> partialUpdate(Departement departement) {
        log.debug("Request to partially update Departement : {}", departement);

        return departementRepository
            .findById(departement.getId())
            .map(existingDepartement -> {
                if (departement.getLibelle() != null) {
                    existingDepartement.setLibelle(departement.getLibelle());
                }

                return existingDepartement;
            })
            .map(departementRepository::save);
    }

    /**
     * Get all the departements.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Departement> findAll() {
        log.debug("Request to get all Departements");
        return departementRepository.findAll();
    }

    /**
     * Get one departement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Departement> findOne(Long id) {
        log.debug("Request to get Departement : {}", id);
        return departementRepository.findById(id);
    }

    /**
     * Delete the departement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Departement : {}", id);
        departementRepository.deleteById(id);
    }
}
