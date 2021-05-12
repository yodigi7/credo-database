package com.credo.database.web.rest;

import com.credo.database.domain.MembershipLevel;
import com.credo.database.repository.MembershipLevelRepository;
import com.credo.database.service.MembershipLevelQueryService;
import com.credo.database.service.MembershipLevelService;
import com.credo.database.service.criteria.MembershipLevelCriteria;
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
 * REST controller for managing {@link com.credo.database.domain.MembershipLevel}.
 */
@RestController
@RequestMapping("/api")
public class MembershipLevelResource {

    private final Logger log = LoggerFactory.getLogger(MembershipLevelResource.class);

    private static final String ENTITY_NAME = "membershipLevel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembershipLevelService membershipLevelService;

    private final MembershipLevelRepository membershipLevelRepository;

    private final MembershipLevelQueryService membershipLevelQueryService;

    public MembershipLevelResource(
        MembershipLevelService membershipLevelService,
        MembershipLevelRepository membershipLevelRepository,
        MembershipLevelQueryService membershipLevelQueryService
    ) {
        this.membershipLevelService = membershipLevelService;
        this.membershipLevelRepository = membershipLevelRepository;
        this.membershipLevelQueryService = membershipLevelQueryService;
    }

    /**
     * {@code POST  /membership-levels} : Create a new membershipLevel.
     *
     * @param membershipLevel the membershipLevel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membershipLevel, or with status {@code 400 (Bad Request)} if the membershipLevel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/membership-levels")
    public ResponseEntity<MembershipLevel> createMembershipLevel(@Valid @RequestBody MembershipLevel membershipLevel)
        throws URISyntaxException {
        log.debug("REST request to save MembershipLevel : {}", membershipLevel);
        if (membershipLevel.getId() != null) {
            throw new BadRequestAlertException("A new membershipLevel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MembershipLevel result = membershipLevelService.save(membershipLevel);
        return ResponseEntity
            .created(new URI("/api/membership-levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /membership-levels/:id} : Updates an existing membershipLevel.
     *
     * @param id the id of the membershipLevel to save.
     * @param membershipLevel the membershipLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipLevel,
     * or with status {@code 400 (Bad Request)} if the membershipLevel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membershipLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/membership-levels/{id}")
    public ResponseEntity<MembershipLevel> updateMembershipLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MembershipLevel membershipLevel
    ) throws URISyntaxException {
        log.debug("REST request to update MembershipLevel : {}, {}", id, membershipLevel);
        if (membershipLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, membershipLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!membershipLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MembershipLevel result = membershipLevelService.save(membershipLevel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, membershipLevel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /membership-levels/:id} : Partial updates given fields of an existing membershipLevel, field will ignore if it is null
     *
     * @param id the id of the membershipLevel to save.
     * @param membershipLevel the membershipLevel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipLevel,
     * or with status {@code 400 (Bad Request)} if the membershipLevel is not valid,
     * or with status {@code 404 (Not Found)} if the membershipLevel is not found,
     * or with status {@code 500 (Internal Server Error)} if the membershipLevel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/membership-levels/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MembershipLevel> partialUpdateMembershipLevel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MembershipLevel membershipLevel
    ) throws URISyntaxException {
        log.debug("REST request to partial update MembershipLevel partially : {}, {}", id, membershipLevel);
        if (membershipLevel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, membershipLevel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!membershipLevelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MembershipLevel> result = membershipLevelService.partialUpdate(membershipLevel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, membershipLevel.getId().toString())
        );
    }

    /**
     * {@code GET  /membership-levels} : get all the membershipLevels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membershipLevels in body.
     */
    @GetMapping("/membership-levels")
    public ResponseEntity<List<MembershipLevel>> getAllMembershipLevels(MembershipLevelCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MembershipLevels by criteria: {}", criteria);
        Page<MembershipLevel> page = membershipLevelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /membership-levels/count} : count all the membershipLevels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/membership-levels/count")
    public ResponseEntity<Long> countMembershipLevels(MembershipLevelCriteria criteria) {
        log.debug("REST request to count MembershipLevels by criteria: {}", criteria);
        return ResponseEntity.ok().body(membershipLevelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /membership-levels/:id} : get the "id" membershipLevel.
     *
     * @param id the id of the membershipLevel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipLevel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/membership-levels/{id}")
    public ResponseEntity<MembershipLevel> getMembershipLevel(@PathVariable Long id) {
        log.debug("REST request to get MembershipLevel : {}", id);
        Optional<MembershipLevel> membershipLevel = membershipLevelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membershipLevel);
    }

    /**
     * {@code DELETE  /membership-levels/:id} : delete the "id" membershipLevel.
     *
     * @param id the id of the membershipLevel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/membership-levels/{id}")
    public ResponseEntity<Void> deleteMembershipLevel(@PathVariable Long id) {
        log.debug("REST request to delete MembershipLevel : {}", id);
        membershipLevelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
