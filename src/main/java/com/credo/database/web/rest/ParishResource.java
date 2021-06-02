package com.credo.database.web.rest;

import com.credo.database.domain.Parish;
import com.credo.database.repository.ParishRepository;
import com.credo.database.service.ParishQueryService;
import com.credo.database.service.ParishService;
import com.credo.database.service.criteria.ParishCriteria;
import com.credo.database.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.credo.database.domain.Parish}.
 */
@RestController
@RequestMapping("/api")
public class ParishResource {

    private final Logger log = LoggerFactory.getLogger(ParishResource.class);

    private static final String ENTITY_NAME = "parish";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParishService parishService;

    private final ParishRepository parishRepository;

    private final ParishQueryService parishQueryService;

    public ParishResource(ParishService parishService, ParishRepository parishRepository, ParishQueryService parishQueryService) {
        this.parishService = parishService;
        this.parishRepository = parishRepository;
        this.parishQueryService = parishQueryService;
    }

    /**
     * {@code POST  /parishes} : Create a new parish.
     *
     * @param parish the parish to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parish, or with status {@code 400 (Bad Request)} if the parish has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parishes")
    public ResponseEntity<Parish> createParish(@Valid @RequestBody Parish parish) throws URISyntaxException {
        log.debug("REST request to save Parish : {}", parish);
        if (parish.getId() != null) {
            throw new BadRequestAlertException("A new parish cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Parish result = parishService.save(parish);
        return ResponseEntity
            .created(new URI("/api/parishes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parishes/:id} : Updates an existing parish.
     *
     * @param id the id of the parish to save.
     * @param parish the parish to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parish,
     * or with status {@code 400 (Bad Request)} if the parish is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parish couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parishes/{id}")
    public ResponseEntity<Parish> updateParish(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Parish parish
    ) throws URISyntaxException {
        log.debug("REST request to update Parish : {}, {}", id, parish);
        if (parish.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parish.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parishRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Parish result = parishService.save(parish);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, parish.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parishes/:id} : Partial updates given fields of an existing parish, field will ignore if it is null
     *
     * @param id the id of the parish to save.
     * @param parish the parish to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parish,
     * or with status {@code 400 (Bad Request)} if the parish is not valid,
     * or with status {@code 404 (Not Found)} if the parish is not found,
     * or with status {@code 500 (Internal Server Error)} if the parish couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parishes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Parish> partialUpdateParish(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Parish parish
    ) throws URISyntaxException {
        log.debug("REST request to partial update Parish partially : {}, {}", id, parish);
        if (parish.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parish.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parishRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Parish> result = parishService.partialUpdate(parish);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, parish.getId().toString())
        );
    }

    /**
     * {@code GET  /parishes} : get all the parishes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parishes in body.
     */
    @GetMapping("/parishes")
    public ResponseEntity<List<Parish>> getAllParishes(ParishCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Parishes by criteria: {}", criteria);
        Page<Parish> page = parishQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parishes/count} : count all the parishes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/parishes/count")
    public ResponseEntity<Long> countParishes(ParishCriteria criteria) {
        log.debug("REST request to count Parishes by criteria: {}", criteria);
        return ResponseEntity.ok().body(parishQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /parishes/:id} : get the "id" parish.
     *
     * @param id the id of the parish to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parish, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parishes/{id}")
    public ResponseEntity<Parish> getParish(@PathVariable Long id) {
        log.debug("REST request to get Parish : {}", id);
        Optional<Parish> parish = parishService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parish);
    }

    /**
     * {@code DELETE  /parishes/:id} : delete the "id" parish.
     *
     * @param id the id of the parish to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parishes/{id}")
    public ResponseEntity<Void> deleteParish(@PathVariable Long id) {
        log.debug("REST request to delete Parish : {}", id);
        parishService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
