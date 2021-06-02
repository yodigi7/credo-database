package com.credo.database.web.rest;

import com.credo.database.domain.PersonNotes;
import com.credo.database.repository.PersonNotesRepository;
import com.credo.database.service.PersonNotesQueryService;
import com.credo.database.service.PersonNotesService;
import com.credo.database.service.criteria.PersonNotesCriteria;
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
 * REST controller for managing {@link com.credo.database.domain.PersonNotes}.
 */
@RestController
@RequestMapping("/api")
public class PersonNotesResource {

    private final Logger log = LoggerFactory.getLogger(PersonNotesResource.class);

    private static final String ENTITY_NAME = "personNotes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonNotesService personNotesService;

    private final PersonNotesRepository personNotesRepository;

    private final PersonNotesQueryService personNotesQueryService;

    public PersonNotesResource(
        PersonNotesService personNotesService,
        PersonNotesRepository personNotesRepository,
        PersonNotesQueryService personNotesQueryService
    ) {
        this.personNotesService = personNotesService;
        this.personNotesRepository = personNotesRepository;
        this.personNotesQueryService = personNotesQueryService;
    }

    /**
     * {@code POST  /person-notes} : Create a new personNotes.
     *
     * @param personNotes the personNotes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personNotes, or with status {@code 400 (Bad Request)} if the personNotes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/person-notes")
    public ResponseEntity<PersonNotes> createPersonNotes(@RequestBody PersonNotes personNotes) throws URISyntaxException {
        log.debug("REST request to save PersonNotes : {}", personNotes);
        if (personNotes.getId() != null) {
            throw new BadRequestAlertException("A new personNotes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonNotes result = personNotesService.save(personNotes);
        return ResponseEntity
            .created(new URI("/api/person-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /person-notes/:id} : Updates an existing personNotes.
     *
     * @param id the id of the personNotes to save.
     * @param personNotes the personNotes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personNotes,
     * or with status {@code 400 (Bad Request)} if the personNotes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personNotes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/person-notes/{id}")
    public ResponseEntity<PersonNotes> updatePersonNotes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonNotes personNotes
    ) throws URISyntaxException {
        log.debug("REST request to update PersonNotes : {}, {}", id, personNotes);
        if (personNotes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personNotes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personNotesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonNotes result = personNotesService.save(personNotes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personNotes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /person-notes/:id} : Partial updates given fields of an existing personNotes, field will ignore if it is null
     *
     * @param id the id of the personNotes to save.
     * @param personNotes the personNotes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personNotes,
     * or with status {@code 400 (Bad Request)} if the personNotes is not valid,
     * or with status {@code 404 (Not Found)} if the personNotes is not found,
     * or with status {@code 500 (Internal Server Error)} if the personNotes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/person-notes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PersonNotes> partialUpdatePersonNotes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonNotes personNotes
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonNotes partially : {}, {}", id, personNotes);
        if (personNotes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personNotes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personNotesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonNotes> result = personNotesService.partialUpdate(personNotes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personNotes.getId().toString())
        );
    }

    /**
     * {@code GET  /person-notes} : get all the personNotes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personNotes in body.
     */
    @GetMapping("/person-notes")
    public ResponseEntity<List<PersonNotes>> getAllPersonNotes(PersonNotesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PersonNotes by criteria: {}", criteria);
        Page<PersonNotes> page = personNotesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /person-notes/count} : count all the personNotes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/person-notes/count")
    public ResponseEntity<Long> countPersonNotes(PersonNotesCriteria criteria) {
        log.debug("REST request to count PersonNotes by criteria: {}", criteria);
        return ResponseEntity.ok().body(personNotesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /person-notes/:id} : get the "id" personNotes.
     *
     * @param id the id of the personNotes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personNotes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/person-notes/{id}")
    public ResponseEntity<PersonNotes> getPersonNotes(@PathVariable Long id) {
        log.debug("REST request to get PersonNotes : {}", id);
        Optional<PersonNotes> personNotes = personNotesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personNotes);
    }

    /**
     * {@code DELETE  /person-notes/:id} : delete the "id" personNotes.
     *
     * @param id the id of the personNotes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/person-notes/{id}")
    public ResponseEntity<Void> deletePersonNotes(@PathVariable Long id) {
        log.debug("REST request to delete PersonNotes : {}", id);
        personNotesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
