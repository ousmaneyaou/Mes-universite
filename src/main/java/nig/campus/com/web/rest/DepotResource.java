package nig.campus.com.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nig.campus.com.domain.Depot;
import nig.campus.com.domain.Faculte;
import nig.campus.com.domain.Universite;
import nig.campus.com.repository.DepotRepository;
import nig.campus.com.service.DepotService;
import nig.campus.com.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link nig.campus.com.domain.Depot}.
 */
@RestController
@RequestMapping("/api")
public class DepotResource {

    private final Logger log = LoggerFactory.getLogger(DepotResource.class);

    private static final String ENTITY_NAME = "depot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepotService depotService;

    private final DepotRepository depotRepository;

    public DepotResource(DepotService depotService, DepotRepository depotRepository) {
        this.depotService = depotService;
        this.depotRepository = depotRepository;
    }

    /**
     * {@code POST  /depots} : Create a new depot.
     *
     * @param depot the depot to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depot, or with status {@code 400 (Bad Request)} if the depot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depots")
    public ResponseEntity<Depot> createDepot(@RequestBody Depot depot) throws URISyntaxException {
        log.debug("REST request to save Depot : {}", depot);
        if (depot.getId() != null) {
            throw new BadRequestAlertException("A new depot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Depot result = depotService.save(depot);
        return ResponseEntity
            .created(new URI("/api/depots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depots/:id} : Updates an existing depot.
     *
     * @param id the id of the depot to save.
     * @param depot the depot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depot,
     * or with status {@code 400 (Bad Request)} if the depot is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depots/{id}")
    public ResponseEntity<Depot> updateDepot(@PathVariable(value = "id", required = false) final Long id, @RequestBody Depot depot)
        throws URISyntaxException {
        log.debug("REST request to update Depot : {}, {}", id, depot);
        if (depot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Depot result = depotService.update(depot);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, depot.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depots/:id} : Partial updates given fields of an existing depot, field will ignore if it is null
     *
     * @param id the id of the depot to save.
     * @param depot the depot to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depot,
     * or with status {@code 400 (Bad Request)} if the depot is not valid,
     * or with status {@code 404 (Not Found)} if the depot is not found,
     * or with status {@code 500 (Internal Server Error)} if the depot couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depots/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Depot> partialUpdateDepot(@PathVariable(value = "id", required = false) final Long id, @RequestBody Depot depot)
        throws URISyntaxException {
        log.debug("REST request to partial update Depot partially : {}, {}", id, depot);
        if (depot.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depot.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Depot> result = depotService.partialUpdate(depot);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, depot.getId().toString())
        );
    }

    /**
     * {@code GET  /depots} : get all the depots.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depots in body.
     */
    @GetMapping("/depots")
    public ResponseEntity<List<Depot>> getAllDepots(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Depots");
        Page<Depot> page = depotService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depots/:id} : get the "id" depot.
     *
     * @param id the id of the depot to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depot, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depots/{id}")
    public ResponseEntity<Depot> getDepot(@PathVariable Long id) {
        log.debug("REST request to get Depot : {}", id);
        Optional<Depot> depot = depotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depot);
    }

    /**
     * {@code DELETE  /depots/:id} : delete the "id" depot.
     *
     * @param id the id of the depot to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depots/{id}")
    public ResponseEntity<Void> deleteDepot(@PathVariable Long id) {
        log.debug("REST request to delete Depot : {}", id);
        depotService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
    /**
     pour la recherche
     */
    @GetMapping("/depots/search/{nom}")
    public ResponseEntity<List<Depot>> search(@PathVariable String nom) {
        List<Depot> depots = depotService.searchByNom(nom);
        return ResponseEntity.ok(depots);
    }

    //@PostMapping("/depots/faculte")            /**la methode poste mapping*/
    //public ResponseEntity<List<Depot>> getDepot(@RequestBody Faculte fac) {
        //log.debug("REST request to get Faculte : {}", fac);
       // List<Depot> depot = depotService.findByFaculte(fac);
       // return ResponseEntity.ok().body(depot);
    //}

    @PostMapping("/depot/departement")            /**la methode poste mapping*/
    public ResponseEntity<Depot> getFaculte(@RequestBody Depot dep) {
      //Faculte faculte= new Faculte();
        //faculte.setUniversite(uni);
        // List<Faculte> faculte = faculteService.findByUniversite(uni);
        return ResponseEntity.ok().body(depotService.save(dep));
    }

}
