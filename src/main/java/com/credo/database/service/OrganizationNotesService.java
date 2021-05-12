package com.credo.database.service;

import com.credo.database.domain.OrganizationNotes;
import com.credo.database.repository.OrganizationNotesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrganizationNotes}.
 */
@Service
@Transactional
public class OrganizationNotesService {

    private final Logger log = LoggerFactory.getLogger(OrganizationNotesService.class);

    private final OrganizationNotesRepository organizationNotesRepository;

    public OrganizationNotesService(OrganizationNotesRepository organizationNotesRepository) {
        this.organizationNotesRepository = organizationNotesRepository;
    }

    /**
     * Save a organizationNotes.
     *
     * @param organizationNotes the entity to save.
     * @return the persisted entity.
     */
    public OrganizationNotes save(OrganizationNotes organizationNotes) {
        log.debug("Request to save OrganizationNotes : {}", organizationNotes);
        return organizationNotesRepository.save(organizationNotes);
    }

    /**
     * Partially update a organizationNotes.
     *
     * @param organizationNotes the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrganizationNotes> partialUpdate(OrganizationNotes organizationNotes) {
        log.debug("Request to partially update OrganizationNotes : {}", organizationNotes);

        return organizationNotesRepository
            .findById(organizationNotes.getId())
            .map(
                existingOrganizationNotes -> {
                    if (organizationNotes.getNotes() != null) {
                        existingOrganizationNotes.setNotes(organizationNotes.getNotes());
                    }

                    return existingOrganizationNotes;
                }
            )
            .map(organizationNotesRepository::save);
    }

    /**
     * Get all the organizationNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizationNotes> findAll(Pageable pageable) {
        log.debug("Request to get all OrganizationNotes");
        return organizationNotesRepository.findAll(pageable);
    }

    /**
     * Get one organizationNotes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrganizationNotes> findOne(Long id) {
        log.debug("Request to get OrganizationNotes : {}", id);
        return organizationNotesRepository.findById(id);
    }

    /**
     * Delete the organizationNotes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrganizationNotes : {}", id);
        organizationNotesRepository.deleteById(id);
    }
}
