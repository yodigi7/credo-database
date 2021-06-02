package com.credo.database.web.rest;

import com.credo.database.domain.PersonEmail;
import com.credo.database.repository.PersonEmailRepository;
import com.credo.database.service.PersonEmailQueryService;
import com.credo.database.service.PersonEmailService;
import com.credo.database.service.criteria.PersonEmailCriteria;
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
 * REST controller for managing {@link com.credo.database.domain.PersonEmail}.
 */
@RestController
@RequestMapping("/api")
public class PersonEmailResource {

    private final Logger log = LoggerFactory.getLogger(PersonEmailResource.class);

    private static final String ENTITY_NAME = "personEmail";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonEmailService personEmailService;

    private final PersonEmailRepository personEmailRepository;

    private final PersonEmailQueryService personEmailQueryService;

    public PersonEmailResource(
        PersonEmailService personEmailService,
        PersonEmailRepository personEmailRepository,
        PersonEmailQueryService personEmailQueryService
    ) {
        this.personEmailService = personEmailService;
        this.personEmailRepository = personEmailRepository;
        this.personEmailQueryService = personEmailQueryService;
    }

    /**
     * {@code POST  /person-emails} : Create a new personEmail.
     *
     * @param personEmail the personEmail to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personEmail, or with status {@code 400 (Bad Request)} if the personEmail has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/person-emails")
    public ResponseEntity<PersonEmail> createPersonEmail(@Valid @RequestBody PersonEmail personEmail) throws URISyntaxException {
        log.debug("REST request to save PersonEmail : {}", personEmail);
        if (personEmail.getId() != null) {
            throw new BadRequestAlertException("A new personEmail cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonEmail result = personEmailService.save(personEmail);
        return ResponseEntity
            .created(new URI("/api/person-emails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /person-emails/:id} : Updates an existing personEmail.
     *
     * @param id the id of the personEmail to save.
     * @param personEmail the personEmail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personEmail,
     * or with status {@code 400 (Bad Request)} if the personEmail is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personEmail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/person-emails/{id}")
    public ResponseEntity<PersonEmail> updatePersonEmail(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PersonEmail personEmail
    ) throws URISyntaxException {
        log.debug("REST request to update PersonEmail : {}, {}", id, personEmail);
        if (personEmail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personEmail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personEmailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonEmail result = personEmailService.save(personEmail);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personEmail.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /person-emails/:id} : Partial updates given fields of an existing personEmail, field will ignore if it is null
     *
     * @param id the id of the personEmail to save.
     * @param personEmail the personEmail to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personEmail,
     * or with status {@code 400 (Bad Request)} if the personEmail is not valid,
     * or with status {@code 404 (Not Found)} if the personEmail is not found,
     * or with status {@code 500 (Internal Server Error)} if the personEmail couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/person-emails/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PersonEmail> partialUpdatePersonEmail(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PersonEmail personEmail
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonEmail partially : {}, {}", id, personEmail);
        if (personEmail.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personEmail.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personEmailRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonEmail> result = personEmailService.partialUpdate(personEmail);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personEmail.getId().toString())
        );
    }

    /**
     * {@code GET  /person-emails} : get all the personEmails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personEmails in body.
     */
    @GetMapping("/person-emails")
    public ResponseEntity<List<PersonEmail>> getAllPersonEmails(PersonEmailCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PersonEmails by criteria: {}", criteria);
        Page<PersonEmail> page = personEmailQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /person-emails/count} : count all the personEmails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/person-emails/count")
    public ResponseEntity<Long> countPersonEmails(PersonEmailCriteria criteria) {
        log.debug("REST request to count PersonEmails by criteria: {}", criteria);
        return ResponseEntity.ok().body(personEmailQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /person-emails/:id} : get the "id" personEmail.
     *
     * @param id the id of the personEmail to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personEmail, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/person-emails/{id}")
    public ResponseEntity<PersonEmail> getPersonEmail(@PathVariable Long id) {
        log.debug("REST request to get PersonEmail : {}", id);
        Optional<PersonEmail> personEmail = personEmailService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personEmail);
    }

    /**
     * {@code DELETE  /person-emails/:id} : delete the "id" personEmail.
     *
     * @param id the id of the personEmail to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/person-emails/{id}")
    public ResponseEntity<Void> deletePersonEmail(@PathVariable Long id) {
        log.debug("REST request to delete PersonEmail : {}", id);
        personEmailService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
