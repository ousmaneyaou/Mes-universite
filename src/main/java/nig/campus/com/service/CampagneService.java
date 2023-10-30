package nig.campus.com.service;

import java.util.List;
import java.util.Optional;
import nig.campus.com.domain.Campagne;
import nig.campus.com.repository.CampagneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Campagne}.
 */
@Service
@Transactional
public class CampagneService {

    private final Logger log = LoggerFactory.getLogger(CampagneService.class);

    private final CampagneRepository campagneRepository;

    public CampagneService(CampagneRepository campagneRepository) {
        this.campagneRepository = campagneRepository;
    }

    /**
     * Save a campagne.
     *
     * @param campagne the entity to save.
     * @return the persisted entity.
     */
    public Campagne save(Campagne campagne) {
        log.debug("Request to save Campagne : {}", campagne);
        return campagneRepository.save(campagne);
    }

    /**
     * Update a campagne.
     *
     * @param campagne the entity to save.
     * @return the persisted entity.
     */
    public Campagne update(Campagne campagne) {
        log.debug("Request to update Campagne : {}", campagne);
        return campagneRepository.save(campagne);
    }

    /**
     * Partially update a campagne.
     *
     * @param campagne the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Campagne> partialUpdate(Campagne campagne) {
        log.debug("Request to partially update Campagne : {}", campagne);

        return campagneRepository
            .findById(campagne.getId())
            .map(existingCampagne -> {
                if (campagne.getIntitule() != null) {
                    existingCampagne.setIntitule(campagne.getIntitule());
                }
                if (campagne.getDateDebut() != null) {
                    existingCampagne.setDateDebut(campagne.getDateDebut());
                }
                if (campagne.getDateFin() != null) {
                    existingCampagne.setDateFin(campagne.getDateFin());
                }

                return existingCampagne;
            })
            .map(campagneRepository::save);
    }

    /**
     * Get all the campagnes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Campagne> findAll() {
        log.debug("Request to get all Campagnes");
        return campagneRepository.findAll();
    }

    /**
     * Get one campagne by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Campagne> findOne(Long id) {
        log.debug("Request to get Campagne : {}", id);
        return campagneRepository.findById(id);
    }

    /**
     * Delete the campagne by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Campagne : {}", id);
        campagneRepository.deleteById(id);
    }
}
