package com.credo.database.service;

import com.credo.database.domain.OrganizationPhone;
import com.credo.database.repository.OrganizationPhoneRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrganizationPhone}.
 */
@Service
@Transactional
public class OrganizationPhoneService {

    private final Logger log = LoggerFactory.getLogger(OrganizationPhoneService.class);

    private final OrganizationPhoneRepository organizationPhoneRepository;

    public OrganizationPhoneService(OrganizationPhoneRepository organizationPhoneRepository) {
        this.organizationPhoneRepository = organizationPhoneRepository;
    }

    /**
     * Save a organizationPhone.
     *
     * @param organizationPhone the entity to save.
     * @return the persisted entity.
     */
    public OrganizationPhone save(OrganizationPhone organizationPhone) {
        log.debug("Request to save OrganizationPhone : {}", organizationPhone);
        return organizationPhoneRepository.save(organizationPhone);
    }

    /**
     * Partially update a organizationPhone.
     *
     * @param organizationPhone the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrganizationPhone> partialUpdate(OrganizationPhone organizationPhone) {
        log.debug("Request to partially update OrganizationPhone : {}", organizationPhone);

        return organizationPhoneRepository
            .findById(organizationPhone.getId())
            .map(
                existingOrganizationPhone -> {
                    if (organizationPhone.getPhoneNumber() != null) {
                        existingOrganizationPhone.setPhoneNumber(organizationPhone.getPhoneNumber());
                    }
                    if (organizationPhone.getType() != null) {
                        existingOrganizationPhone.setType(organizationPhone.getType());
                    }

                    return existingOrganizationPhone;
                }
            )
            .map(organizationPhoneRepository::save);
    }

    /**
     * Get all the organizationPhones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizationPhone> findAll(Pageable pageable) {
        log.debug("Request to get all OrganizationPhones");
        return organizationPhoneRepository.findAll(pageable);
    }

    /**
     * Get one organizationPhone by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrganizationPhone> findOne(Long id) {
        log.debug("Request to get OrganizationPhone : {}", id);
        return organizationPhoneRepository.findById(id);
    }

    /**
     * Delete the organizationPhone by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrganizationPhone : {}", id);
        organizationPhoneRepository.deleteById(id);
    }
}
