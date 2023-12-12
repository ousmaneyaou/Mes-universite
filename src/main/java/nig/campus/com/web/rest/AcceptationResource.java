package nig.campus.com.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nig.campus.com.domain.Acceptation;
import nig.campus.com.repository.AcceptationRepository;
import nig.campus.com.service.AcceptationService;
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
 * REST controller for managing {@link nig.campus.com.domain.Acceptation}.
 */
@RestController
@RequestMapping("/api")
public class AcceptationResource {

    private final Logger log = LoggerFactory.getLogger(AcceptationResource.class);

    private static final String ENTITY_NAME = "acceptation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcceptationService acceptationService;

    private final AcceptationRepository acceptationRepository;

    public AcceptationResource(AcceptationService acceptationService, AcceptationRepository acceptationRepository) {
        this.acceptationService = acceptationService;
        this.acceptationRepository = acceptationRepository;
    }

    /**
     * {@code POST  /acceptations} : Create a new acceptation.
     *
     * @param acceptation the acceptation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acceptation, or with status {@code 400 (Bad Request)} if the acceptation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acceptations")
    public ResponseEntity<Acceptation> createAcceptation(@RequestBody Acceptation acceptation) throws URISyntaxException {
        log.debug("REST request to save Acceptation : {}", acceptation);
        if (acceptation.getId() != null) {
            throw new BadRequestAlertException("A new acceptation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Acceptation result = acceptationService.save(acceptation);
        return ResponseEntity
            .created(new URI("/api/acceptations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acceptations/:id} : Updates an existing acceptation.
     *
     * @param id the id of the acceptation to save.
     * @param acceptation the acceptation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acceptation,
     * or with status {@code 400 (Bad Request)} if the acceptation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acceptation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acceptations/{id}")
    public ResponseEntity<Acceptation> updateAcceptation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Acceptation acceptation
    ) throws URISyntaxException {
        log.debug("REST request to update Acceptation : {}, {}", id, acceptation);
        if (acceptation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acceptation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acceptationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Acceptation result = acceptationService.update(acceptation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, acceptation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acceptations/:id} : Partial updates given fields of an existing acceptation, field will ignore if it is null
     *
     * @param id the id of the acceptation to save.
     * @param acceptation the acceptation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acceptation,
     * or with status {@code 400 (Bad Request)} if the acceptation is not valid,
     * or with status {@code 404 (Not Found)} if the acceptation is not found,
     * or with status {@code 500 (Internal Server Error)} if the acceptation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acceptations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Acceptation> partialUpdateAcceptation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Acceptation acceptation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Acceptation partially : {}, {}", id, acceptation);
        if (acceptation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acceptation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acceptationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Acceptation> result = acceptationService.partialUpdate(acceptation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, acceptation.getId().toString())
        );
    }

    /**
     * {@code GET  /acceptations} : get all the acceptations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acceptations in body.
     */
    @GetMapping("/acceptations")
    public ResponseEntity<List<Acceptation>> getAllAcceptations(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Acceptations");
        Page<Acceptation> page = acceptationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acceptations/:id} : get the "id" acceptation.
     *
     * @param id the id of the acceptation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acceptation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acceptations/{id}")
    public ResponseEntity<Acceptation> getAcceptation(@PathVariable Long id) {
        log.debug("REST request to get Acceptation : {}", id);
        Optional<Acceptation> acceptation = acceptationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(acceptation);
    }

    /**
     * {@code DELETE  /acceptations/:id} : delete the "id" acceptation.
     *
     * @param id the id of the acceptation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acceptations/{id}")
    public ResponseEntity<Void> deleteAcceptation(@PathVariable Long id) {
        log.debug("REST request to delete Acceptation : {}", id);
        acceptationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
