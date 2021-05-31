package com.credo.database.web.rest;

import com.credo.database.domain.HouseDetails;
import com.credo.database.repository.HouseDetailsRepository;
import com.credo.database.service.HouseDetailsQueryService;
import com.credo.database.service.HouseDetailsService;
import com.credo.database.service.criteria.HouseDetailsCriteria;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.credo.database.domain.HouseDetails}.
 */
@RestController
@RequestMapping("/api")
public class HouseDetailsResource {

    private final Logger log = LoggerFactory.getLogger(HouseDetailsResource.class);

    private static final String ENTITY_NAME = "houseDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HouseDetailsService houseDetailsService;

    private final HouseDetailsRepository houseDetailsRepository;

    private final HouseDetailsQueryService houseDetailsQueryService;

    public HouseDetailsResource(
        HouseDetailsService houseDetailsService,
        HouseDetailsRepository houseDetailsRepository,
        HouseDetailsQueryService houseDetailsQueryService
    ) {
        this.houseDetailsService = houseDetailsService;
        this.houseDetailsRepository = houseDetailsRepository;
        this.houseDetailsQueryService = houseDetailsQueryService;
    }

    /**
     * {@code POST  /house-details} : Create a new houseDetails.
     *
     * @param houseDetails the houseDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new houseDetails, or with status {@code 400 (Bad Request)} if the houseDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/house-details")
    public ResponseEntity<HouseDetails> createHouseDetails(@RequestBody HouseDetails houseDetails) throws URISyntaxException {
        log.debug("REST request to save HouseDetails : {}", houseDetails);
        if (houseDetails.getId() != null) {
            throw new BadRequestAlertException("A new houseDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HouseDetails result = houseDetailsService.save(houseDetails);
        return ResponseEntity
            .created(new URI("/api/house-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /house-details/:id} : Updates an existing houseDetails.
     *
     * @param id the id of the houseDetails to save.
     * @param houseDetails the houseDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated houseDetails,
     * or with status {@code 400 (Bad Request)} if the houseDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the houseDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/house-details/{id}")
    public ResponseEntity<HouseDetails> updateHouseDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HouseDetails houseDetails
    ) throws URISyntaxException {
        log.debug("REST request to update HouseDetails : {}, {}", id, houseDetails);
        if (houseDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, houseDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!houseDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HouseDetails result = houseDetailsService.save(houseDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, houseDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /house-details/:id} : Partial updates given fields of an existing houseDetails, field will ignore if it is null
     *
     * @param id the id of the houseDetails to save.
     * @param houseDetails the houseDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated houseDetails,
     * or with status {@code 400 (Bad Request)} if the houseDetails is not valid,
     * or with status {@code 404 (Not Found)} if the houseDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the houseDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/house-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HouseDetails> partialUpdateHouseDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HouseDetails houseDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update HouseDetails partially : {}, {}", id, houseDetails);
        if (houseDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, houseDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!houseDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HouseDetails> result = houseDetailsService.partialUpdate(houseDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, houseDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /house-details} : get all the houseDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of houseDetails in body.
     */
    @GetMapping("/house-details")
    public ResponseEntity<List<HouseDetails>> getAllHouseDetails(HouseDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HouseDetails by criteria: {}", criteria);
        Page<HouseDetails> page = houseDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /house-details/count} : count all the houseDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/house-details/count")
    public ResponseEntity<Long> countHouseDetails(HouseDetailsCriteria criteria) {
        log.debug("REST request to count HouseDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(houseDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /house-details/:id} : get the "id" houseDetails.
     *
     * @param id the id of the houseDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the houseDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/house-details/{id}")
    public ResponseEntity<HouseDetails> getHouseDetails(@PathVariable Long id) {
        log.debug("REST request to get HouseDetails : {}", id);
        Optional<HouseDetails> houseDetails = houseDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(houseDetails);
    }

    /**
     * {@code DELETE  /house-details/:id} : delete the "id" houseDetails.
     *
     * @param id the id of the houseDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/house-details/{id}")
    public ResponseEntity<Void> deleteHouseDetails(@PathVariable Long id) {
        log.debug("REST request to delete HouseDetails : {}", id);
        houseDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
