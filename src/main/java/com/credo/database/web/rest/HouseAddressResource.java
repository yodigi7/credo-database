package com.credo.database.web.rest;

import com.credo.database.domain.HouseAddress;
import com.credo.database.repository.HouseAddressRepository;
import com.credo.database.service.HouseAddressQueryService;
import com.credo.database.service.HouseAddressService;
import com.credo.database.service.criteria.HouseAddressCriteria;
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
 * REST controller for managing {@link com.credo.database.domain.HouseAddress}.
 */
@RestController
@RequestMapping("/api")
public class HouseAddressResource {

    private final Logger log = LoggerFactory.getLogger(HouseAddressResource.class);

    private static final String ENTITY_NAME = "houseAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HouseAddressService houseAddressService;

    private final HouseAddressRepository houseAddressRepository;

    private final HouseAddressQueryService houseAddressQueryService;

    public HouseAddressResource(
        HouseAddressService houseAddressService,
        HouseAddressRepository houseAddressRepository,
        HouseAddressQueryService houseAddressQueryService
    ) {
        this.houseAddressService = houseAddressService;
        this.houseAddressRepository = houseAddressRepository;
        this.houseAddressQueryService = houseAddressQueryService;
    }

    /**
     * {@code POST  /house-addresses} : Create a new houseAddress.
     *
     * @param houseAddress the houseAddress to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new houseAddress, or with status {@code 400 (Bad Request)} if the houseAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/house-addresses")
    public ResponseEntity<HouseAddress> createHouseAddress(@RequestBody HouseAddress houseAddress) throws URISyntaxException {
        log.debug("REST request to save HouseAddress : {}", houseAddress);
        if (houseAddress.getId() != null) {
            throw new BadRequestAlertException("A new houseAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HouseAddress result = houseAddressService.save(houseAddress);
        return ResponseEntity
            .created(new URI("/api/house-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /house-addresses/:id} : Updates an existing houseAddress.
     *
     * @param id the id of the houseAddress to save.
     * @param houseAddress the houseAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated houseAddress,
     * or with status {@code 400 (Bad Request)} if the houseAddress is not valid,
     * or with status {@code 500 (Internal Server Error)} if the houseAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/house-addresses/{id}")
    public ResponseEntity<HouseAddress> updateHouseAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HouseAddress houseAddress
    ) throws URISyntaxException {
        log.debug("REST request to update HouseAddress : {}, {}", id, houseAddress);
        if (houseAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, houseAddress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!houseAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        HouseAddress result = houseAddressService.save(houseAddress);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, houseAddress.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /house-addresses/:id} : Partial updates given fields of an existing houseAddress, field will ignore if it is null
     *
     * @param id the id of the houseAddress to save.
     * @param houseAddress the houseAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated houseAddress,
     * or with status {@code 400 (Bad Request)} if the houseAddress is not valid,
     * or with status {@code 404 (Not Found)} if the houseAddress is not found,
     * or with status {@code 500 (Internal Server Error)} if the houseAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/house-addresses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<HouseAddress> partialUpdateHouseAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HouseAddress houseAddress
    ) throws URISyntaxException {
        log.debug("REST request to partial update HouseAddress partially : {}, {}", id, houseAddress);
        if (houseAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, houseAddress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!houseAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HouseAddress> result = houseAddressService.partialUpdate(houseAddress);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, houseAddress.getId().toString())
        );
    }

    /**
     * {@code GET  /house-addresses} : get all the houseAddresses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of houseAddresses in body.
     */
    @GetMapping("/house-addresses")
    public ResponseEntity<List<HouseAddress>> getAllHouseAddresses(HouseAddressCriteria criteria, Pageable pageable) {
        log.debug("REST request to get HouseAddresses by criteria: {}", criteria);
        Page<HouseAddress> page = houseAddressQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /house-addresses/count} : count all the houseAddresses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/house-addresses/count")
    public ResponseEntity<Long> countHouseAddresses(HouseAddressCriteria criteria) {
        log.debug("REST request to count HouseAddresses by criteria: {}", criteria);
        return ResponseEntity.ok().body(houseAddressQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /house-addresses/:id} : get the "id" houseAddress.
     *
     * @param id the id of the houseAddress to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the houseAddress, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/house-addresses/{id}")
    public ResponseEntity<HouseAddress> getHouseAddress(@PathVariable Long id) {
        log.debug("REST request to get HouseAddress : {}", id);
        Optional<HouseAddress> houseAddress = houseAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(houseAddress);
    }

    /**
     * {@code DELETE  /house-addresses/:id} : delete the "id" houseAddress.
     *
     * @param id the id of the houseAddress to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/house-addresses/{id}")
    public ResponseEntity<Void> deleteHouseAddress(@PathVariable Long id) {
        log.debug("REST request to delete HouseAddress : {}", id);
        houseAddressService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
