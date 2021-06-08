package com.credo.database.web.rest;

import com.credo.database.domain.EventPerk;
import com.credo.database.repository.EventPerkRepository;
import com.credo.database.service.EventPerkQueryService;
import com.credo.database.service.EventPerkService;
import com.credo.database.service.criteria.EventPerkCriteria;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.credo.database.domain.EventPerk}.
 */
@RestController
@RequestMapping("/api")
public class EventPerkResource {

    private final Logger log = LoggerFactory.getLogger(EventPerkResource.class);

    private static final String ENTITY_NAME = "eventPerk";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventPerkService eventPerkService;

    private final EventPerkRepository eventPerkRepository;

    private final EventPerkQueryService eventPerkQueryService;

    public EventPerkResource(
        EventPerkService eventPerkService,
        EventPerkRepository eventPerkRepository,
        EventPerkQueryService eventPerkQueryService
    ) {
        this.eventPerkService = eventPerkService;
        this.eventPerkRepository = eventPerkRepository;
        this.eventPerkQueryService = eventPerkQueryService;
    }

    /**
     * {@code POST  /event-perks} : Create a new eventPerk.
     *
     * @param eventPerk the eventPerk to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventPerk, or with status {@code 400 (Bad Request)} if the eventPerk has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-perks")
    public ResponseEntity<EventPerk> createEventPerk(@RequestBody EventPerk eventPerk) throws URISyntaxException {
        log.debug("REST request to save EventPerk : {}", eventPerk);
        if (eventPerk.getId() != null) {
            throw new BadRequestAlertException("A new eventPerk cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventPerk result = eventPerkService.save(eventPerk);
        return ResponseEntity
            .created(new URI("/api/event-perks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-perks/:id} : Updates an existing eventPerk.
     *
     * @param id the id of the eventPerk to save.
     * @param eventPerk the eventPerk to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventPerk,
     * or with status {@code 400 (Bad Request)} if the eventPerk is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventPerk couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-perks/{id}")
    public ResponseEntity<EventPerk> updateEventPerk(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventPerk eventPerk
    ) throws URISyntaxException {
        log.debug("REST request to update EventPerk : {}, {}", id, eventPerk);
        if (eventPerk.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventPerk.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventPerkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventPerk result = eventPerkService.save(eventPerk);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eventPerk.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-perks/:id} : Partial updates given fields of an existing eventPerk, field will ignore if it is null
     *
     * @param id the id of the eventPerk to save.
     * @param eventPerk the eventPerk to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventPerk,
     * or with status {@code 400 (Bad Request)} if the eventPerk is not valid,
     * or with status {@code 404 (Not Found)} if the eventPerk is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventPerk couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/event-perks/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EventPerk> partialUpdateEventPerk(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventPerk eventPerk
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventPerk partially : {}, {}", id, eventPerk);
        if (eventPerk.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventPerk.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventPerkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventPerk> result = eventPerkService.partialUpdate(eventPerk);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eventPerk.getId().toString())
        );
    }

    /**
     * {@code GET  /event-perks} : get all the eventPerks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventPerks in body.
     */
    @GetMapping("/event-perks")
    public ResponseEntity<List<EventPerk>> getAllEventPerks(EventPerkCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EventPerks by criteria: {}", criteria);
        Page<EventPerk> page = eventPerkQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-perks/count} : count all the eventPerks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/event-perks/count")
    public ResponseEntity<Long> countEventPerks(EventPerkCriteria criteria) {
        log.debug("REST request to count EventPerks by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventPerkQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-perks/:id} : get the "id" eventPerk.
     *
     * @param id the id of the eventPerk to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventPerk, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-perks/{id}")
    public ResponseEntity<EventPerk> getEventPerk(@PathVariable Long id) {
        log.debug("REST request to get EventPerk : {}", id);
        Optional<EventPerk> eventPerk = eventPerkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventPerk);
    }

    /**
     * {@code DELETE  /event-perks/:id} : delete the "id" eventPerk.
     *
     * @param id the id of the eventPerk to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-perks/{id}")
    public ResponseEntity<Void> deleteEventPerk(@PathVariable Long id) {
        log.debug("REST request to delete EventPerk : {}", id);
        eventPerkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
