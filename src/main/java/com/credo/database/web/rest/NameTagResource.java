package com.credo.database.web.rest;

import com.credo.database.domain.NameTag;
import com.credo.database.repository.NameTagRepository;
import com.credo.database.service.NameTagQueryService;
import com.credo.database.service.NameTagService;
import com.credo.database.service.criteria.NameTagCriteria;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.credo.database.domain.NameTag}.
 */
@RestController
@RequestMapping("/api")
public class NameTagResource {

    private final Logger log = LoggerFactory.getLogger(NameTagResource.class);

    private static final String ENTITY_NAME = "nameTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NameTagService nameTagService;

    private final NameTagRepository nameTagRepository;

    private final NameTagQueryService nameTagQueryService;

    public NameTagResource(NameTagService nameTagService, NameTagRepository nameTagRepository, NameTagQueryService nameTagQueryService) {
        this.nameTagService = nameTagService;
        this.nameTagRepository = nameTagRepository;
        this.nameTagQueryService = nameTagQueryService;
    }

    /**
     * {@code POST  /name-tags} : Create a new nameTag.
     *
     * @param nameTag the nameTag to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nameTag, or with status {@code 400 (Bad Request)} if the nameTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/name-tags")
    public ResponseEntity<NameTag> createNameTag(@RequestBody NameTag nameTag) throws URISyntaxException {
        log.debug("REST request to save NameTag : {}", nameTag);
        if (nameTag.getId() != null) {
            throw new BadRequestAlertException("A new nameTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NameTag result = nameTagService.save(nameTag);
        return ResponseEntity
            .created(new URI("/api/name-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /name-tags/:id} : Updates an existing nameTag.
     *
     * @param id the id of the nameTag to save.
     * @param nameTag the nameTag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nameTag,
     * or with status {@code 400 (Bad Request)} if the nameTag is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nameTag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/name-tags/{id}")
    public ResponseEntity<NameTag> updateNameTag(@PathVariable(value = "id", required = false) final Long id, @RequestBody NameTag nameTag)
        throws URISyntaxException {
        log.debug("REST request to update NameTag : {}, {}", id, nameTag);
        if (nameTag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nameTag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nameTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NameTag result = nameTagService.save(nameTag);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nameTag.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /name-tags/:id} : Partial updates given fields of an existing nameTag, field will ignore if it is null
     *
     * @param id the id of the nameTag to save.
     * @param nameTag the nameTag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nameTag,
     * or with status {@code 400 (Bad Request)} if the nameTag is not valid,
     * or with status {@code 404 (Not Found)} if the nameTag is not found,
     * or with status {@code 500 (Internal Server Error)} if the nameTag couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/name-tags/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<NameTag> partialUpdateNameTag(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NameTag nameTag
    ) throws URISyntaxException {
        log.debug("REST request to partial update NameTag partially : {}, {}", id, nameTag);
        if (nameTag.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nameTag.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nameTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NameTag> result = nameTagService.partialUpdate(nameTag);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nameTag.getId().toString())
        );
    }

    /**
     * {@code GET  /name-tags} : get all the nameTags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nameTags in body.
     */
    @GetMapping("/name-tags")
    public ResponseEntity<List<NameTag>> getAllNameTags(NameTagCriteria criteria, Pageable pageable) {
        log.debug("REST request to get NameTags by criteria: {}", criteria);
        Page<NameTag> page = nameTagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /name-tags/count} : count all the nameTags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/name-tags/count")
    public ResponseEntity<Long> countNameTags(NameTagCriteria criteria) {
        log.debug("REST request to count NameTags by criteria: {}", criteria);
        return ResponseEntity.ok().body(nameTagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /name-tags/:id} : get the "id" nameTag.
     *
     * @param id the id of the nameTag to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nameTag, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/name-tags/{id}")
    public ResponseEntity<NameTag> getNameTag(@PathVariable Long id) {
        log.debug("REST request to get NameTag : {}", id);
        Optional<NameTag> nameTag = nameTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nameTag);
    }

    /**
     * {@code DELETE  /name-tags/:id} : delete the "id" nameTag.
     *
     * @param id the id of the nameTag to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/name-tags/{id}")
    public ResponseEntity<Void> deleteNameTag(@PathVariable Long id) {
        log.debug("REST request to delete NameTag : {}", id);
        nameTagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
