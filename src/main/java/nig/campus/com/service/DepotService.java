package nig.campus.com.service;

import java.util.List;
import java.util.Optional;
import nig.campus.com.domain.Depot;
import nig.campus.com.domain.Faculte;
import nig.campus.com.domain.Universite;
import nig.campus.com.repository.DepotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Depot}.
 */
@Service
@Transactional
public class DepotService {

    private final Logger log = LoggerFactory.getLogger(DepotService.class);

    private final DepotRepository depotRepository;

    public DepotService(DepotRepository depotRepository) {
        this.depotRepository = depotRepository;
    }

    /**
     * Save a depot.
     *
     * @param depot the entity to save.
     * @return the persisted entity.
     */
    public Depot save(Depot depot) {
        log.debug("Request to save Depot : {}", depot);
        return depotRepository.save(depot);
    }

    /**
     * Update a depot.
     *
     * @param depot the entity to save.
     * @return the persisted entity.
     */
    public Depot update(Depot depot) {
        log.debug("Request to update Depot : {}", depot);
        return depotRepository.save(depot);
    }

    /**
     * Partially update a depot.
     *
     * @param depot the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Depot> partialUpdate(Depot depot) {
        log.debug("Request to partially update Depot : {}", depot);

        return depotRepository
            .findById(depot.getId())
            .map(existingDepot -> {
                if (depot.getNom() != null) {
                    existingDepot.setNom(depot.getNom());
                }
                if (depot.getPrenom() != null) {
                    existingDepot.setPrenom(depot.getPrenom());
                }
                if (depot.getDateNaissance() != null) {
                    existingDepot.setDateNaissance(depot.getDateNaissance());
                }
                if (depot.getLieuNaissance() != null) {
                    existingDepot.setLieuNaissance(depot.getLieuNaissance());
                }
                if (depot.getEmail() != null) {
                    existingDepot.setEmail(depot.getEmail());
                }
                if (depot.getNationalite() != null) {
                    existingDepot.setNationalite(depot.getNationalite());
                }
                if (depot.getTelephone() != null) {
                    existingDepot.setTelephone(depot.getTelephone());
                }
                if (depot.getSexe() != null) {
                    existingDepot.setSexe(depot.getSexe());
                }
                if (depot.getDateDepot() != null) {
                    existingDepot.setDateDepot(depot.getDateDepot());
                }
                if (depot.getNumeroDeTable() != null) {
                    existingDepot.setNumeroDeTable(depot.getNumeroDeTable());
                }
                if (depot.getSerie() != null) {
                    existingDepot.setSerie(depot.getSerie());
                }
                if (depot.getDiplome() != null) {
                    existingDepot.setDiplome(depot.getDiplome());
                }
                if (depot.getDiplomeContentType() != null) {
                    existingDepot.setDiplomeContentType(depot.getDiplomeContentType());
                }
                if (depot.getReleveDeNote() != null) {
                    existingDepot.setReleveDeNote(depot.getReleveDeNote());
                }
                if (depot.getReleveDeNoteContentType() != null) {
                    existingDepot.setReleveDeNoteContentType(depot.getReleveDeNoteContentType());
                }
                if (depot.getAnneeObtention() != null) {
                    existingDepot.setAnneeObtention(depot.getAnneeObtention());
                }
                if (depot.getLieuObtention() != null) {
                    existingDepot.setLieuObtention(depot.getLieuObtention());
                }
                if (depot.getMention() != null) {
                    existingDepot.setMention(depot.getMention());
                }
                if (depot.getLettreDeMotivation() != null) {
                    existingDepot.setLettreDeMotivation(depot.getLettreDeMotivation());
                }
                if (depot.getLettreDeMotivationContentType() != null) {
                    existingDepot.setLettreDeMotivationContentType(depot.getLettreDeMotivationContentType());
                }
                if (depot.getChoix1() != null) {
                    existingDepot.setChoix1(depot.getChoix1());
                }
                if (depot.getChoix2() != null) {
                    existingDepot.setChoix2(depot.getChoix2());
                }
                if (depot.getChoix3() != null) {
                    existingDepot.setChoix3(depot.getChoix3());
                }
                if (depot.getPhoto() != null) {
                    existingDepot.setPhoto(depot.getPhoto());
                }
                if (depot.getPhotoContentType() != null) {
                    existingDepot.setPhotoContentType(depot.getPhotoContentType());
                }

                return existingDepot;
            })
            .map(depotRepository::save);
    }

    /**
     * Get all the depots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Depot> findAll(Pageable pageable) {
        log.debug("Request to get all Depots");
        return depotRepository.findAll(pageable);
    }

    /**
     * Get one depot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Depot> findOne(Long id) {
        log.debug("Request to get Depot : {}", id);
        return depotRepository.findById(id);
    }

    /**
     * Delete the depot by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Depot : {}", id);
        depotRepository.deleteById(id);
    }

    public List<Depot> searchByNom(String nom) {
        return depotRepository.findByNom(nom);
    }

   // public List<Depot> findByFaculte(Faculte fac) {
        //return depotRepository.findAllByFaculte(fac);
    //}
}
