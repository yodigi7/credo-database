package com.credo.database.web.rest;

import com.credo.database.domain.PersonPhone;
import com.credo.database.repository.PersonPhoneRepository;
import com.credo.database.service.PersonPhoneQueryService;
import com.credo.database.service.PersonPhoneService;
import com.credo.database.service.criteria.PersonPhoneCriteria;
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
 * REST controller for managing {@link com.credo.database.domain.PersonPhone}.
 */
@RestController
@RequestMapping("/api")
public class PersonPhoneResource {

    private final Logger log = LoggerFactory.getLogger(PersonPhoneResource.class);

    private static final String ENTITY_NAME = "personPhone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonPhoneService personPhoneService;

    private final PersonPhoneRepository personPhoneRepository;

    private final PersonPhoneQueryService personPhoneQueryService;

    public PersonPhoneResource(
        PersonPhoneService personPhoneService,
        PersonPhoneRepository personPhoneRepository,
        PersonPhoneQueryService personPhoneQueryService
    ) {
        this.personPhoneService = personPhoneService;
        this.personPhoneRepository = personPhoneRepository;
        this.personPhoneQueryService = personPhoneQueryService;
    }

    /**
     * {@code POST  /person-phones} : Create a new personPhone.
     *
     * @param personPhone the personPhone to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personPhone, or with status {@code 400 (Bad Request)} if the personPhone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/person-phones")
    public ResponseEntity<PersonPhone> createPersonPhone(@Valid @RequestBody PersonPhone personPhone) throws URISyntaxException {
        log.debug("REST request to save PersonPhone : {}", personPhone);
        if (personPhone.getId() != null) {
            throw new BadRequestAlertException("A new personPhone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonPhone result = personPhoneService.save(personPhone);
        return ResponseEntity
            .created(new URI("/api/person-phones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /person-phones/:id} : Updates an existing personPhone.
     *
     * @param id the id of the personPhone to save.
     * @param personPhone the personPhone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personPhone,
     * or with status {@code 400 (Bad Request)} if the personPhone is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personPhone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/person-phones/{id}")
    public ResponseEntity<PersonPhone> updatePersonPhone(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PersonPhone personPhone
    ) throws URISyntaxException {
        log.debug("REST request to update PersonPhone : {}, {}", id, personPhone);
        if (personPhone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personPhone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personPhoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonPhone result = personPhoneService.save(personPhone);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personPhone.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /person-phones/:id} : Partial updates given fields of an existing personPhone, field will ignore if it is null
     *
     * @param id the id of the personPhone to save.
     * @param personPhone the personPhone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personPhone,
     * or with status {@code 400 (Bad Request)} if the personPhone is not valid,
     * or with status {@code 404 (Not Found)} if the personPhone is not found,
     * or with status {@code 500 (Internal Server Error)} if the personPhone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/person-phones/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PersonPhone> partialUpdatePersonPhone(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PersonPhone personPhone
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonPhone partially : {}, {}", id, personPhone);
        if (personPhone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personPhone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personPhoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonPhone> result = personPhoneService.partialUpdate(personPhone);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, personPhone.getId().toString())
        );
    }

    /**
     * {@code GET  /person-phones} : get all the personPhones.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personPhones in body.
     */
    @GetMapping("/person-phones")
    public ResponseEntity<List<PersonPhone>> getAllPersonPhones(PersonPhoneCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PersonPhones by criteria: {}", criteria);
        Page<PersonPhone> page = personPhoneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /person-phones/count} : count all the personPhones.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/person-phones/count")
    public ResponseEntity<Long> countPersonPhones(PersonPhoneCriteria criteria) {
        log.debug("REST request to count PersonPhones by criteria: {}", criteria);
        return ResponseEntity.ok().body(personPhoneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /person-phones/:id} : get the "id" personPhone.
     *
     * @param id the id of the personPhone to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personPhone, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/person-phones/{id}")
    public ResponseEntity<PersonPhone> getPersonPhone(@PathVariable Long id) {
        log.debug("REST request to get PersonPhone : {}", id);
        Optional<PersonPhone> personPhone = personPhoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(personPhone);
    }

    /**
     * {@code DELETE  /person-phones/:id} : delete the "id" personPhone.
     *
     * @param id the id of the personPhone to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/person-phones/{id}")
    public ResponseEntity<Void> deletePersonPhone(@PathVariable Long id) {
        log.debug("REST request to delete PersonPhone : {}", id);
        personPhoneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
