package com.credo.database.web.rest;

import com.credo.database.domain.OrganizationPhone;
import com.credo.database.repository.OrganizationPhoneRepository;
import com.credo.database.service.OrganizationPhoneQueryService;
import com.credo.database.service.OrganizationPhoneService;
import com.credo.database.service.criteria.OrganizationPhoneCriteria;
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
 * REST controller for managing {@link com.credo.database.domain.OrganizationPhone}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationPhoneResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationPhoneResource.class);

    private static final String ENTITY_NAME = "organizationPhone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationPhoneService organizationPhoneService;

    private final OrganizationPhoneRepository organizationPhoneRepository;

    private final OrganizationPhoneQueryService organizationPhoneQueryService;

    public OrganizationPhoneResource(
        OrganizationPhoneService organizationPhoneService,
        OrganizationPhoneRepository organizationPhoneRepository,
        OrganizationPhoneQueryService organizationPhoneQueryService
    ) {
        this.organizationPhoneService = organizationPhoneService;
        this.organizationPhoneRepository = organizationPhoneRepository;
        this.organizationPhoneQueryService = organizationPhoneQueryService;
    }

    /**
     * {@code POST  /organization-phones} : Create a new organizationPhone.
     *
     * @param organizationPhone the organizationPhone to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationPhone, or with status {@code 400 (Bad Request)} if the organizationPhone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organization-phones")
    public ResponseEntity<OrganizationPhone> createOrganizationPhone(@Valid @RequestBody OrganizationPhone organizationPhone)
        throws URISyntaxException {
        log.debug("REST request to save OrganizationPhone : {}", organizationPhone);
        if (organizationPhone.getId() != null) {
            throw new BadRequestAlertException("A new organizationPhone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationPhone result = organizationPhoneService.save(organizationPhone);
        return ResponseEntity
            .created(new URI("/api/organization-phones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organization-phones/:id} : Updates an existing organizationPhone.
     *
     * @param id the id of the organizationPhone to save.
     * @param organizationPhone the organizationPhone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationPhone,
     * or with status {@code 400 (Bad Request)} if the organizationPhone is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationPhone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organization-phones/{id}")
    public ResponseEntity<OrganizationPhone> updateOrganizationPhone(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrganizationPhone organizationPhone
    ) throws URISyntaxException {
        log.debug("REST request to update OrganizationPhone : {}, {}", id, organizationPhone);
        if (organizationPhone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationPhone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationPhoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrganizationPhone result = organizationPhoneService.save(organizationPhone);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organizationPhone.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /organization-phones/:id} : Partial updates given fields of an existing organizationPhone, field will ignore if it is null
     *
     * @param id the id of the organizationPhone to save.
     * @param organizationPhone the organizationPhone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationPhone,
     * or with status {@code 400 (Bad Request)} if the organizationPhone is not valid,
     * or with status {@code 404 (Not Found)} if the organizationPhone is not found,
     * or with status {@code 500 (Internal Server Error)} if the organizationPhone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/organization-phones/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrganizationPhone> partialUpdateOrganizationPhone(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrganizationPhone organizationPhone
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrganizationPhone partially : {}, {}", id, organizationPhone);
        if (organizationPhone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationPhone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationPhoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrganizationPhone> result = organizationPhoneService.partialUpdate(organizationPhone);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organizationPhone.getId().toString())
        );
    }

    /**
     * {@code GET  /organization-phones} : get all the organizationPhones.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationPhones in body.
     */
    @GetMapping("/organization-phones")
    public ResponseEntity<List<OrganizationPhone>> getAllOrganizationPhones(OrganizationPhoneCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrganizationPhones by criteria: {}", criteria);
        Page<OrganizationPhone> page = organizationPhoneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organization-phones/count} : count all the organizationPhones.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/organization-phones/count")
    public ResponseEntity<Long> countOrganizationPhones(OrganizationPhoneCriteria criteria) {
        log.debug("REST request to count OrganizationPhones by criteria: {}", criteria);
        return ResponseEntity.ok().body(organizationPhoneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /organization-phones/:id} : get the "id" organizationPhone.
     *
     * @param id the id of the organizationPhone to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationPhone, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-phones/{id}")
    public ResponseEntity<OrganizationPhone> getOrganizationPhone(@PathVariable Long id) {
        log.debug("REST request to get OrganizationPhone : {}", id);
        Optional<OrganizationPhone> organizationPhone = organizationPhoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationPhone);
    }

    /**
     * {@code DELETE  /organization-phones/:id} : delete the "id" organizationPhone.
     *
     * @param id the id of the organizationPhone to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organization-phones/{id}")
    public ResponseEntity<Void> deleteOrganizationPhone(@PathVariable Long id) {
        log.debug("REST request to delete OrganizationPhone : {}", id);
        organizationPhoneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
