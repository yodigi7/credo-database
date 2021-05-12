package com.credo.database.web.rest;

import com.credo.database.domain.ParishPhone;
import com.credo.database.repository.ParishPhoneRepository;
import com.credo.database.service.ParishPhoneQueryService;
import com.credo.database.service.ParishPhoneService;
import com.credo.database.service.criteria.ParishPhoneCriteria;
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
 * REST controller for managing {@link com.credo.database.domain.ParishPhone}.
 */
@RestController
@RequestMapping("/api")
public class ParishPhoneResource {

    private final Logger log = LoggerFactory.getLogger(ParishPhoneResource.class);

    private static final String ENTITY_NAME = "parishPhone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParishPhoneService parishPhoneService;

    private final ParishPhoneRepository parishPhoneRepository;

    private final ParishPhoneQueryService parishPhoneQueryService;

    public ParishPhoneResource(
        ParishPhoneService parishPhoneService,
        ParishPhoneRepository parishPhoneRepository,
        ParishPhoneQueryService parishPhoneQueryService
    ) {
        this.parishPhoneService = parishPhoneService;
        this.parishPhoneRepository = parishPhoneRepository;
        this.parishPhoneQueryService = parishPhoneQueryService;
    }

    /**
     * {@code POST  /parish-phones} : Create a new parishPhone.
     *
     * @param parishPhone the parishPhone to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new parishPhone, or with status {@code 400 (Bad Request)} if the parishPhone has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/parish-phones")
    public ResponseEntity<ParishPhone> createParishPhone(@Valid @RequestBody ParishPhone parishPhone) throws URISyntaxException {
        log.debug("REST request to save ParishPhone : {}", parishPhone);
        if (parishPhone.getId() != null) {
            throw new BadRequestAlertException("A new parishPhone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParishPhone result = parishPhoneService.save(parishPhone);
        return ResponseEntity
            .created(new URI("/api/parish-phones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /parish-phones/:id} : Updates an existing parishPhone.
     *
     * @param id the id of the parishPhone to save.
     * @param parishPhone the parishPhone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parishPhone,
     * or with status {@code 400 (Bad Request)} if the parishPhone is not valid,
     * or with status {@code 500 (Internal Server Error)} if the parishPhone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/parish-phones/{id}")
    public ResponseEntity<ParishPhone> updateParishPhone(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParishPhone parishPhone
    ) throws URISyntaxException {
        log.debug("REST request to update ParishPhone : {}, {}", id, parishPhone);
        if (parishPhone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parishPhone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parishPhoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParishPhone result = parishPhoneService.save(parishPhone);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, parishPhone.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /parish-phones/:id} : Partial updates given fields of an existing parishPhone, field will ignore if it is null
     *
     * @param id the id of the parishPhone to save.
     * @param parishPhone the parishPhone to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated parishPhone,
     * or with status {@code 400 (Bad Request)} if the parishPhone is not valid,
     * or with status {@code 404 (Not Found)} if the parishPhone is not found,
     * or with status {@code 500 (Internal Server Error)} if the parishPhone couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/parish-phones/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ParishPhone> partialUpdateParishPhone(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParishPhone parishPhone
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParishPhone partially : {}, {}", id, parishPhone);
        if (parishPhone.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, parishPhone.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!parishPhoneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParishPhone> result = parishPhoneService.partialUpdate(parishPhone);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, parishPhone.getId().toString())
        );
    }

    /**
     * {@code GET  /parish-phones} : get all the parishPhones.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of parishPhones in body.
     */
    @GetMapping("/parish-phones")
    public ResponseEntity<List<ParishPhone>> getAllParishPhones(ParishPhoneCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ParishPhones by criteria: {}", criteria);
        Page<ParishPhone> page = parishPhoneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /parish-phones/count} : count all the parishPhones.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/parish-phones/count")
    public ResponseEntity<Long> countParishPhones(ParishPhoneCriteria criteria) {
        log.debug("REST request to count ParishPhones by criteria: {}", criteria);
        return ResponseEntity.ok().body(parishPhoneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /parish-phones/:id} : get the "id" parishPhone.
     *
     * @param id the id of the parishPhone to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the parishPhone, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/parish-phones/{id}")
    public ResponseEntity<ParishPhone> getParishPhone(@PathVariable Long id) {
        log.debug("REST request to get ParishPhone : {}", id);
        Optional<ParishPhone> parishPhone = parishPhoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parishPhone);
    }

    /**
     * {@code DELETE  /parish-phones/:id} : delete the "id" parishPhone.
     *
     * @param id the id of the parishPhone to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/parish-phones/{id}")
    public ResponseEntity<Void> deleteParishPhone(@PathVariable Long id) {
        log.debug("REST request to delete ParishPhone : {}", id);
        parishPhoneService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
