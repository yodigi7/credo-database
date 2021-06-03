package com.credo.database.web.rest;

import com.credo.database.domain.ParishEmail;
import com.credo.database.repository.ParishEmailRepository;
import com.credo.database.service.ParishEmailQueryService;
import com.credo.database.service.ParishEmailService;
import com.credo.database.service.criteria.ParishEmailCriteria;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.credo.database.domain.ParishEmail}.
 */
@RestController
@RequestMapping("/api")
public class ParishEmailResource {

    private final Logger log = LoggerFactory.getLogger(ParishEmailResource.class);

    private static final String ENTITY_NAME = "parishEmail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParishEmailService parishEmailService;

    private final ParishEmailRepository parishEmailRepository;

    private final ParishEmailQueryService parishEmailQueryService;

    public ParishEmailResource(
        ParishEmailService parishEmailService,
        ParishEmailRepository parishEmailRepository,
        ParishEmailQueryService parishEmailQueryService
    ) {
        this.parishEmailService = parishEmailService;
        this.parishEmailRepository = parishEmailRepository;
        this.parishEmailQueryService = parishEmailQueryService;
    }

    /**
     * {@code POST  /parish-emails} : Create a new parishEmail.
     *
     * @param parishEmail the parishEmail to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parishEmail, or with status {@code 400 (Bad Request)} if the parishEmail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parish-emails")
    public ResponseEntity<ParishEmail> createParishEmail(@Valid @RequestBody ParishEmail parishEmail) throws URISyntaxException {
        log.debug("REST request to save ParishEmail : {}", parishEmail);
        if (parishEmail.getId() != null) {
            throw new BadRequestAlertException("A new parishEmail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParishEmail result = parishEmailService.save(parishEmail);
        return ResponseEntity
            .created(new URI("/api/parish-emails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parish-emails/:id} : Updates an existing parishEmail.
     *
     * @param id the id of the parishEmail to save.
     * @param parishEmail the parishEmail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parishEmail,
     * or with status {@code 400 (Bad Request)} if the parishEmail is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parishEmail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parish-emails/{id}")
    public ResponseEntity<ParishEmail> updateParishEmail(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParishEmail parishEmail
    ) throws URISyntaxException {
        log.debug("REST request to update ParishEmail : {}, {}", id, parishEmail);
        if (parishEmail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parishEmail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parishEmailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParishEmail result = parishEmailService.save(parishEmail);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, parishEmail.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parish-emails/:id} : Partial updates given fields of an existing parishEmail, field will ignore if it is null
     *
     * @param id the id of the parishEmail to save.
     * @param parishEmail the parishEmail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parishEmail,
     * or with status {@code 400 (Bad Request)} if the parishEmail is not valid,
     * or with status {@code 404 (Not Found)} if the parishEmail is not found,
     * or with status {@code 500 (Internal Server Error)} if the parishEmail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parish-emails/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ParishEmail> partialUpdateParishEmail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParishEmail parishEmail
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParishEmail partially : {}, {}", id, parishEmail);
        if (parishEmail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parishEmail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parishEmailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParishEmail> result = parishEmailService.partialUpdate(parishEmail);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, parishEmail.getId().toString())
        );
    }

    /**
     * {@code GET  /parish-emails} : get all the parishEmails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parishEmails in body.
     */
    @GetMapping("/parish-emails")
    public ResponseEntity<List<ParishEmail>> getAllParishEmails(ParishEmailCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ParishEmails by criteria: {}", criteria);
        Page<ParishEmail> page = parishEmailQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parish-emails/count} : count all the parishEmails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/parish-emails/count")
    public ResponseEntity<Long> countParishEmails(ParishEmailCriteria criteria) {
        log.debug("REST request to count ParishEmails by criteria: {}", criteria);
        return ResponseEntity.ok().body(parishEmailQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /parish-emails/:id} : get the "id" parishEmail.
     *
     * @param id the id of the parishEmail to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parishEmail, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parish-emails/{id}")
    public ResponseEntity<ParishEmail> getParishEmail(@PathVariable Long id) {
        log.debug("REST request to get ParishEmail : {}", id);
        Optional<ParishEmail> parishEmail = parishEmailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parishEmail);
    }

    /**
     * {@code DELETE  /parish-emails/:id} : delete the "id" parishEmail.
     *
     * @param id the id of the parishEmail to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parish-emails/{id}")
    public ResponseEntity<Void> deleteParishEmail(@PathVariable Long id) {
        log.debug("REST request to delete ParishEmail : {}", id);
        parishEmailService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
