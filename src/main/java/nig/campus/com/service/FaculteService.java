package nig.campus.com.service;

import java.util.List;
import java.util.Optional;
import nig.campus.com.domain.Faculte;
import nig.campus.com.domain.Universite;
import nig.campus.com.repository.FaculteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Faculte}.
 */
@Service
@Transactional
public class FaculteService {

    private final Logger log = LoggerFactory.getLogger(FaculteService.class);

    private final FaculteRepository faculteRepository;

    public FaculteService(FaculteRepository faculteRepository) {
        this.faculteRepository = faculteRepository;
    }

    /**
     * Save a faculte.
     *
     * @param faculte the entity to save.
     * @return the persisted entity.
     */
    public Faculte save(Faculte faculte) {
        log.debug("Request to save Faculte : {}", faculte);
        return faculteRepository.save(faculte);
    }

    public List<Faculte> findByUniversite(Universite uni) {
        return faculteRepository.findAllByUniversite(uni);
    }

    /**
     * Update a faculte.
     *
     * @param faculte the entity to save.
     * @return the persisted entity.
     */
    public Faculte update(Faculte faculte) {
        log.debug("Request to update Faculte : {}", faculte);
        return faculteRepository.save(faculte);
    }

    /**
     * Partially update a faculte.
     *
     * @param faculte the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Faculte> partialUpdate(Faculte faculte) {
        log.debug("Request to partially update Faculte : {}", faculte);

        return faculteRepository
            .findById(faculte.getId())
            .map(existingFaculte -> {
                if (faculte.getLibelle() != null) {
                    existingFaculte.setLibelle(faculte.getLibelle());
                }

                return existingFaculte;
            })
            .map(faculteRepository::save);
    }

    /**
     * Get all the facultes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Faculte> findAll(Pageable pageable) {
        log.debug("Request to get all Facultes");
        return faculteRepository.findAll(pageable);
    }

    /**
     * Get one faculte by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Faculte> findOne(Long id) {
        log.debug("Request to get Faculte : {}", id);
        return faculteRepository.findById(id);
    }

    /**
     * Delete the faculte by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Faculte : {}", id);
        faculteRepository.deleteById(id);
    }
}
