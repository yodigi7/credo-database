package com.credo.database.service;

import com.credo.database.domain.Organization;
import com.credo.database.repository.OrganizationRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Organization}.
 */
@Service
@Transactional
public class OrganizationService {

    private final Logger log = LoggerFactory.getLogger(OrganizationService.class);

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    /**
     * Save a organization.
     *
     * @param organization the entity to save.
     * @return the persisted entity.
     */
    public Organization save(Organization organization) {
        log.debug("Request to save Organization : {}", organization);
        return organizationRepository.save(organization);
    }

    /**
     * Partially update a organization.
     *
     * @param organization the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Organization> partialUpdate(Organization organization) {
        log.debug("Request to partially update Organization : {}", organization);

        return organizationRepository
            .findById(organization.getId())
            .map(
                existingOrganization -> {
                    if (organization.getName() != null) {
                        existingOrganization.setName(organization.getName());
                    }
                    if (organization.getMailingLabel() != null) {
                        existingOrganization.setMailingLabel(organization.getMailingLabel());
                    }

                    return existingOrganization;
                }
            )
            .map(organizationRepository::save);
    }

    /**
     * Get all the organizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Organization> findAll(Pageable pageable) {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAll(pageable);
    }

    /**
     *  Get all the organizations where Notes is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Organization> findAllWhereNotesIsNull() {
        log.debug("Request to get all organizations where Notes is null");
        return StreamSupport
            .stream(organizationRepository.findAll().spliterator(), false)
            .filter(organization -> organization.getNotes() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one organization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Organization> findOne(Long id) {
        log.debug("Request to get Organization : {}", id);
        return organizationRepository.findById(id);
    }

    /**
     * Delete the organization by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
    }
}
