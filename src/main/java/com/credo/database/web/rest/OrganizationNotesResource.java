package com.credo.database.web.rest;

import com.credo.database.domain.OrganizationNotes;
import com.credo.database.repository.OrganizationNotesRepository;
import com.credo.database.service.OrganizationNotesQueryService;
import com.credo.database.service.OrganizationNotesService;
import com.credo.database.service.criteria.OrganizationNotesCriteria;
import com.credo.database.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.credo.database.domain.OrganizationNotes}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationNotesResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationNotesResource.class);

    private static final String ENTITY_NAME = "organizationNotes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationNotesService organizationNotesService;

    private final OrganizationNotesRepository organizationNotesRepository;

    private final OrganizationNotesQueryService organizationNotesQueryService;

    public OrganizationNotesResource(
        OrganizationNotesService organizationNotesService,
        OrganizationNotesRepository organizationNotesRepository,
        OrganizationNotesQueryService organizationNotesQueryService
    ) {
        this.organizationNotesService = organizationNotesService;
        this.organizationNotesRepository = organizationNotesRepository;
        this.organizationNotesQueryService = organizationNotesQueryService;
    }

    /**
     * {@code POST  /organization-notes} : Create a new organizationNotes.
     *
     * @param organizationNotes the organizationNotes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationNotes, or with status {@code 400 (Bad Request)} if the organizationNotes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organization-notes")
    public ResponseEntity<OrganizationNotes> createOrganizationNotes(@RequestBody OrganizationNotes organizationNotes)
        throws URISyntaxException {
        log.debug("REST request to save OrganizationNotes : {}", organizationNotes);
        if (organizationNotes.getId() != null) {
            throw new BadRequestAlertException("A new organizationNotes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationNotes result = organizationNotesService.save(organizationNotes);
        return ResponseEntity
            .created(new URI("/api/organization-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organization-notes/:id} : Updates an existing organizationNotes.
     *
     * @param id the id of the organizationNotes to save.
     * @param organizationNotes the organizationNotes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationNotes,
     * or with status {@code 400 (Bad Request)} if the organizationNotes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationNotes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organization-notes/{id}")
    public ResponseEntity<OrganizationNotes> updateOrganizationNotes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrganizationNotes organizationNotes
    ) throws URISyntaxException {
        log.debug("REST request to update OrganizationNotes : {}, {}", id, organizationNotes);
        if (organizationNotes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationNotes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationNotesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrganizationNotes result = organizationNotesService.save(organizationNotes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organizationNotes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /organization-notes/:id} : Partial updates given fields of an existing organizationNotes, field will ignore if it is null
     *
     * @param id the id of the organizationNotes to save.
     * @param organizationNotes the organizationNotes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationNotes,
     * or with status {@code 400 (Bad Request)} if the organizationNotes is not valid,
     * or with status {@code 404 (Not Found)} if the organizationNotes is not found,
     * or with status {@code 500 (Internal Server Error)} if the organizationNotes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/organization-notes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrganizationNotes> partialUpdateOrganizationNotes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrganizationNotes organizationNotes
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrganizationNotes partially : {}, {}", id, organizationNotes);
        if (organizationNotes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationNotes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationNotesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrganizationNotes> result = organizationNotesService.partialUpdate(organizationNotes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organizationNotes.getId().toString())
        );
    }

    /**
     * {@code GET  /organization-notes} : get all the organizationNotes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationNotes in body.
     */
    @GetMapping("/organization-notes")
    public ResponseEntity<List<OrganizationNotes>> getAllOrganizationNotes(OrganizationNotesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrganizationNotes by criteria: {}", criteria);
        Page<OrganizationNotes> page = organizationNotesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organization-notes/count} : count all the organizationNotes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/organization-notes/count")
    public ResponseEntity<Long> countOrganizationNotes(OrganizationNotesCriteria criteria) {
        log.debug("REST request to count OrganizationNotes by criteria: {}", criteria);
        return ResponseEntity.ok().body(organizationNotesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /organization-notes/:id} : get the "id" organizationNotes.
     *
     * @param id the id of the organizationNotes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationNotes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-notes/{id}")
    public ResponseEntity<OrganizationNotes> getOrganizationNotes(@PathVariable Long id) {
        log.debug("REST request to get OrganizationNotes : {}", id);
        Optional<OrganizationNotes> organizationNotes = organizationNotesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationNotes);
    }

    /**
     * {@code DELETE  /organization-notes/:id} : delete the "id" organizationNotes.
     *
     * @param id the id of the organizationNotes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organization-notes/{id}")
    public ResponseEntity<Void> deleteOrganizationNotes(@PathVariable Long id) {
        log.debug("REST request to delete OrganizationNotes : {}", id);
        organizationNotesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
