package com.credo.database.web.rest;

import com.credo.database.domain.OrganizationEmail;
import com.credo.database.repository.OrganizationEmailRepository;
import com.credo.database.service.OrganizationEmailQueryService;
import com.credo.database.service.OrganizationEmailService;
import com.credo.database.service.criteria.OrganizationEmailCriteria;
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
 * REST controller for managing {@link com.credo.database.domain.OrganizationEmail}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationEmailResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationEmailResource.class);

    private static final String ENTITY_NAME = "organizationEmail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationEmailService organizationEmailService;

    private final OrganizationEmailRepository organizationEmailRepository;

    private final OrganizationEmailQueryService organizationEmailQueryService;

    public OrganizationEmailResource(
        OrganizationEmailService organizationEmailService,
        OrganizationEmailRepository organizationEmailRepository,
        OrganizationEmailQueryService organizationEmailQueryService
    ) {
        this.organizationEmailService = organizationEmailService;
        this.organizationEmailRepository = organizationEmailRepository;
        this.organizationEmailQueryService = organizationEmailQueryService;
    }

    /**
     * {@code POST  /organization-emails} : Create a new organizationEmail.
     *
     * @param organizationEmail the organizationEmail to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationEmail, or with status {@code 400 (Bad Request)} if the organizationEmail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organization-emails")
    public ResponseEntity<OrganizationEmail> createOrganizationEmail(@Valid @RequestBody OrganizationEmail organizationEmail)
        throws URISyntaxException {
        log.debug("REST request to save OrganizationEmail : {}", organizationEmail);
        if (organizationEmail.getId() != null) {
            throw new BadRequestAlertException("A new organizationEmail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationEmail result = organizationEmailService.save(organizationEmail);
        return ResponseEntity
            .created(new URI("/api/organization-emails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organization-emails/:id} : Updates an existing organizationEmail.
     *
     * @param id the id of the organizationEmail to save.
     * @param organizationEmail the organizationEmail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationEmail,
     * or with status {@code 400 (Bad Request)} if the organizationEmail is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationEmail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organization-emails/{id}")
    public ResponseEntity<OrganizationEmail> updateOrganizationEmail(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrganizationEmail organizationEmail
    ) throws URISyntaxException {
        log.debug("REST request to update OrganizationEmail : {}, {}", id, organizationEmail);
        if (organizationEmail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationEmail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationEmailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrganizationEmail result = organizationEmailService.save(organizationEmail);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organizationEmail.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /organization-emails/:id} : Partial updates given fields of an existing organizationEmail, field will ignore if it is null
     *
     * @param id the id of the organizationEmail to save.
     * @param organizationEmail the organizationEmail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationEmail,
     * or with status {@code 400 (Bad Request)} if the organizationEmail is not valid,
     * or with status {@code 404 (Not Found)} if the organizationEmail is not found,
     * or with status {@code 500 (Internal Server Error)} if the organizationEmail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/organization-emails/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OrganizationEmail> partialUpdateOrganizationEmail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrganizationEmail organizationEmail
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrganizationEmail partially : {}, {}", id, organizationEmail);
        if (organizationEmail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationEmail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationEmailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrganizationEmail> result = organizationEmailService.partialUpdate(organizationEmail);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, organizationEmail.getId().toString())
        );
    }

    /**
     * {@code GET  /organization-emails} : get all the organizationEmails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationEmails in body.
     */
    @GetMapping("/organization-emails")
    public ResponseEntity<List<OrganizationEmail>> getAllOrganizationEmails(OrganizationEmailCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OrganizationEmails by criteria: {}", criteria);
        Page<OrganizationEmail> page = organizationEmailQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /organization-emails/count} : count all the organizationEmails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/organization-emails/count")
    public ResponseEntity<Long> countOrganizationEmails(OrganizationEmailCriteria criteria) {
        log.debug("REST request to count OrganizationEmails by criteria: {}", criteria);
        return ResponseEntity.ok().body(organizationEmailQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /organization-emails/:id} : get the "id" organizationEmail.
     *
     * @param id the id of the organizationEmail to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationEmail, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-emails/{id}")
    public ResponseEntity<OrganizationEmail> getOrganizationEmail(@PathVariable Long id) {
        log.debug("REST request to get OrganizationEmail : {}", id);
        Optional<OrganizationEmail> organizationEmail = organizationEmailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationEmail);
    }

    /**
     * {@code DELETE  /organization-emails/:id} : delete the "id" organizationEmail.
     *
     * @param id the id of the organizationEmail to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organization-emails/{id}")
    public ResponseEntity<Void> deleteOrganizationEmail(@PathVariable Long id) {
        log.debug("REST request to delete OrganizationEmail : {}", id);
        organizationEmailService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
