package com.credo.database.web.rest;

import com.credo.database.domain.OrganizationAddress;
import com.credo.database.repository.OrganizationAddressRepository;
import com.credo.database.service.OrganizationAddressQueryService;
import com.credo.database.service.OrganizationAddressService;
import com.credo.database.service.criteria.OrganizationAddressCriteria;
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
 * REST controller for managing {@link com.credo.database.domain.OrganizationAddress}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationAddressResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationAddressResource.class);

    private static final String ENTITY_NAME = "organizationAddress";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationAddressService organizationAddressService;

    private final OrganizationAddressRepository organizationAddressRepository;

    private final OrganizationAddressQueryService organizationAddressQueryService;

    public OrganizationAddressResource(
        OrganizationAddressService organizationAddressService,
        OrganizationAddressRepository organizationAddressRepository,
        OrganizationAddressQueryService organizationAddressQueryService
    ) {
        this.organizationAddressService = organizationAddressService;
        this.organizationAddressRepository = organizationAddressRepository;
        this.organizationAddressQueryService = organizationAddressQueryService;
    }

    /**
     * {@code POST  /organization-addresses} : Create a new organizationAddress.
     *
     * @param organizationAddress the organizationAddress to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationAddress, or with status {@code 400 (Bad Request)} if the organizationAddress has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organization-addresses")
    public ResponseEntity<OrganizationAddress> createOrganizationAddress(@RequestBody OrganizationAddress organizationAddress)
        throws URISyntaxException {
        log.debug("REST request to save OrganizationAddress : {}", organizationAddress);
        if (organizationAddress.getId() != null) {
            throw new BadRequestAlertException("A new organizationAddress cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationAddress result = organizationAddressService.save(organizationAddress);
        return ResponseEntity
            .created(new URI("/api/organization-addresses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organization-addresses/:id} : Updates an existing organizationAddress.
     *
     * @param id the id of the organizationAddress to save.
     * @param organizationAddress the organizationAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationAddress,
     * or with status {@code 400 (Bad Request)} if the organizationAddress is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organization-addresses/{id}")
    public ResponseEntity<OrganizationAddress> updateOrganizationAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrganizationAddress organizationAddress
    ) throws URISyntaxException {
        log.debug("REST request to update OrganizationAddress : {}, {}", id, organizationAddress);
        if (organizationAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationAddress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrganizationAddress result = organizationAddressService.save(organizationAddress);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organizationAddress.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /organization-addresses/:id} : Partial updates given fields of an existing organizationAddress, field will ignore if it is null
     *
     * @param id the id of the organizationAddress to save.
     * @param organizationAddress the organizationAddress to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationAddress,
     * or with status {@code 400 (Bad Request)} if the organizationAddress is not valid,
     * or with status {@code 404 (Not Found)} if the organizationAddress is not found,
     * or with status {@code 500 (Internal Server Error)} if the organizationAddress couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/organization-addresses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrganizationAddress> partialUpdateOrganizationAddress(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrganizationAddress organizationAddress
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrganizationAddress partially : {}, {}", id, organizationAddress);
        if (organizationAddress.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationAddress.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationAddressRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrganizationAddress> result = organizationAddressService.partialUpdate(organizationAddress);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organizationAddress.getId().toString())
        );
    }

    /**
     * {@code GET  /organization-addresses} : get all the organizationAddresses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationAddresses in body.
     */
    @GetMapping("/organization-addresses")
    public ResponseEntity<List<OrganizationAddress>> getAllOrganizationAddresses(OrganizationAddressCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrganizationAddresses by criteria: {}", criteria);
        Page<OrganizationAddress> page = organizationAddressQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organization-addresses/count} : count all the organizationAddresses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/organization-addresses/count")
    public ResponseEntity<Long> countOrganizationAddresses(OrganizationAddressCriteria criteria) {
        log.debug("REST request to count OrganizationAddresses by criteria: {}", criteria);
        return ResponseEntity.ok().body(organizationAddressQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /organization-addresses/:id} : get the "id" organizationAddress.
     *
     * @param id the id of the organizationAddress to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationAddress, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-addresses/{id}")
    public ResponseEntity<OrganizationAddress> getOrganizationAddress(@PathVariable Long id) {
        log.debug("REST request to get OrganizationAddress : {}", id);
        Optional<OrganizationAddress> organizationAddress = organizationAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationAddress);
    }

    /**
     * {@code DELETE  /organization-addresses/:id} : delete the "id" organizationAddress.
     *
     * @param id the id of the organizationAddress to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organization-addresses/{id}")
    public ResponseEntity<Void> deleteOrganizationAddress(@PathVariable Long id) {
        log.debug("REST request to delete OrganizationAddress : {}", id);
        organizationAddressService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
