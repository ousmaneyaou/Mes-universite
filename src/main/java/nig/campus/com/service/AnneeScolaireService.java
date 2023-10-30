package nig.campus.com.service;

import java.util.List;
import java.util.Optional;
import nig.campus.com.domain.AnneeScolaire;
import nig.campus.com.repository.AnneeScolaireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AnneeScolaire}.
 */
@Service
@Transactional
public class AnneeScolaireService {

    private final Logger log = LoggerFactory.getLogger(AnneeScolaireService.class);

    private final AnneeScolaireRepository anneeScolaireRepository;

    public AnneeScolaireService(AnneeScolaireRepository anneeScolaireRepository) {
        this.anneeScolaireRepository = anneeScolaireRepository;
    }

    /**
     * Save a anneeScolaire.
     *
     * @param anneeScolaire the entity to save.
     * @return the persisted entity.
     */
    public AnneeScolaire save(AnneeScolaire anneeScolaire) {
        log.debug("Request to save AnneeScolaire : {}", anneeScolaire);
        return anneeScolaireRepository.save(anneeScolaire);
    }

    /**
     * Update a anneeScolaire.
     *
     * @param anneeScolaire the entity to save.
     * @return the persisted entity.
     */
    public AnneeScolaire update(AnneeScolaire anneeScolaire) {
        log.debug("Request to update AnneeScolaire : {}", anneeScolaire);
        return anneeScolaireRepository.save(anneeScolaire);
    }

    /**
     * Partially update a anneeScolaire.
     *
     * @param anneeScolaire the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AnneeScolaire> partialUpdate(AnneeScolaire anneeScolaire) {
        log.debug("Request to partially update AnneeScolaire : {}", anneeScolaire);

        return anneeScolaireRepository
            .findById(anneeScolaire.getId())
            .map(existingAnneeScolaire -> {
                if (anneeScolaire.getLibelle() != null) {
                    existingAnneeScolaire.setLibelle(anneeScolaire.getLibelle());
                }
                if (anneeScolaire.getEnCour() != null) {
                    existingAnneeScolaire.setEnCour(anneeScolaire.getEnCour());
                }

                return existingAnneeScolaire;
            })
            .map(anneeScolaireRepository::save);
    }

    /**
     * Get all the anneeScolaires.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AnneeScolaire> findAll() {
        log.debug("Request to get all AnneeScolaires");
        return anneeScolaireRepository.findAll();
    }

    /**
     * Get one anneeScolaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AnneeScolaire> findOne(Long id) {
        log.debug("Request to get AnneeScolaire : {}", id);
        return anneeScolaireRepository.findById(id);
    }

    /**
     * Delete the anneeScolaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AnneeScolaire : {}", id);
        anneeScolaireRepository.deleteById(id);
    }
}
