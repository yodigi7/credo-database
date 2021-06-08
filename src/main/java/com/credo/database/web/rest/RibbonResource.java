package com.credo.database.web.rest;

import com.credo.database.domain.Ribbon;
import com.credo.database.repository.RibbonRepository;
import com.credo.database.service.RibbonQueryService;
import com.credo.database.service.RibbonService;
import com.credo.database.service.criteria.RibbonCriteria;
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
 * REST controller for managing {@link com.credo.database.domain.Ribbon}.
 */
@RestController
@RequestMapping("/api")
public class RibbonResource {

    private final Logger log = LoggerFactory.getLogger(RibbonResource.class);

    private static final String ENTITY_NAME = "ribbon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RibbonService ribbonService;

    private final RibbonRepository ribbonRepository;

    private final RibbonQueryService ribbonQueryService;

    public RibbonResource(RibbonService ribbonService, RibbonRepository ribbonRepository, RibbonQueryService ribbonQueryService) {
        this.ribbonService = ribbonService;
        this.ribbonRepository = ribbonRepository;
        this.ribbonQueryService = ribbonQueryService;
    }

    /**
     * {@code POST  /ribbons} : Create a new ribbon.
     *
     * @param ribbon the ribbon to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ribbon, or with status {@code 400 (Bad Request)} if the ribbon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ribbons")
    public ResponseEntity<Ribbon> createRibbon(@RequestBody Ribbon ribbon) throws URISyntaxException {
        log.debug("REST request to save Ribbon : {}", ribbon);
        if (ribbon.getId() != null) {
            throw new BadRequestAlertException("A new ribbon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ribbon result = ribbonService.save(ribbon);
        return ResponseEntity
            .created(new URI("/api/ribbons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ribbons/:id} : Updates an existing ribbon.
     *
     * @param id the id of the ribbon to save.
     * @param ribbon the ribbon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ribbon,
     * or with status {@code 400 (Bad Request)} if the ribbon is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ribbon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ribbons/{id}")
    public ResponseEntity<Ribbon> updateRibbon(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ribbon ribbon)
        throws URISyntaxException {
        log.debug("REST request to update Ribbon : {}, {}", id, ribbon);
        if (ribbon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ribbon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ribbonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ribbon result = ribbonService.save(ribbon);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ribbon.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ribbons/:id} : Partial updates given fields of an existing ribbon, field will ignore if it is null
     *
     * @param id the id of the ribbon to save.
     * @param ribbon the ribbon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ribbon,
     * or with status {@code 400 (Bad Request)} if the ribbon is not valid,
     * or with status {@code 404 (Not Found)} if the ribbon is not found,
     * or with status {@code 500 (Internal Server Error)} if the ribbon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ribbons/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Ribbon> partialUpdateRibbon(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ribbon ribbon
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ribbon partially : {}, {}", id, ribbon);
        if (ribbon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ribbon.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ribbonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ribbon> result = ribbonService.partialUpdate(ribbon);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ribbon.getId().toString())
        );
    }

    /**
     * {@code GET  /ribbons} : get all the ribbons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ribbons in body.
     */
    @GetMapping("/ribbons")
    public ResponseEntity<List<Ribbon>> getAllRibbons(RibbonCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ribbons by criteria: {}", criteria);
        Page<Ribbon> page = ribbonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ribbons/count} : count all the ribbons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ribbons/count")
    public ResponseEntity<Long> countRibbons(RibbonCriteria criteria) {
        log.debug("REST request to count Ribbons by criteria: {}", criteria);
        return ResponseEntity.ok().body(ribbonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ribbons/:id} : get the "id" ribbon.
     *
     * @param id the id of the ribbon to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ribbon, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ribbons/{id}")
    public ResponseEntity<Ribbon> getRibbon(@PathVariable Long id) {
        log.debug("REST request to get Ribbon : {}", id);
        Optional<Ribbon> ribbon = ribbonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ribbon);
    }

    /**
     * {@code DELETE  /ribbons/:id} : delete the "id" ribbon.
     *
     * @param id the id of the ribbon to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ribbons/{id}")
    public ResponseEntity<Void> deleteRibbon(@PathVariable Long id) {
        log.debug("REST request to delete Ribbon : {}", id);
        ribbonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
