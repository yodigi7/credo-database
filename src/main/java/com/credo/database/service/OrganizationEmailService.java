package com.credo.database.service;

import com.credo.database.domain.OrganizationEmail;
import com.credo.database.repository.OrganizationEmailRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrganizationEmail}.
 */
@Service
@Transactional
public class OrganizationEmailService {

    private final Logger log = LoggerFactory.getLogger(OrganizationEmailService.class);

    private final OrganizationEmailRepository organizationEmailRepository;

    public OrganizationEmailService(OrganizationEmailRepository organizationEmailRepository) {
        this.organizationEmailRepository = organizationEmailRepository;
    }

    /**
     * Save a organizationEmail.
     *
     * @param organizationEmail the entity to save.
     * @return the persisted entity.
     */
    public OrganizationEmail save(OrganizationEmail organizationEmail) {
        log.debug("Request to save OrganizationEmail : {}", organizationEmail);
        return organizationEmailRepository.save(organizationEmail);
    }

    /**
     * Partially update a organizationEmail.
     *
     * @param organizationEmail the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrganizationEmail> partialUpdate(OrganizationEmail organizationEmail) {
        log.debug("Request to partially update OrganizationEmail : {}", organizationEmail);

        return organizationEmailRepository
            .findById(organizationEmail.getId())
            .map(
                existingOrganizationEmail -> {
                    if (organizationEmail.getEmail() != null) {
                        existingOrganizationEmail.setEmail(organizationEmail.getEmail());
                    }
                    if (organizationEmail.getType() != null) {
                        existingOrganizationEmail.setType(organizationEmail.getType());
                    }
                    if (organizationEmail.getEmailNewsletterSubscription() != null) {
                        existingOrganizationEmail.setEmailNewsletterSubscription(organizationEmail.getEmailNewsletterSubscription());
                    }
                    if (organizationEmail.getEmailEventNotificationSubscription() != null) {
                        existingOrganizationEmail.setEmailEventNotificationSubscription(
                            organizationEmail.getEmailEventNotificationSubscription()
                        );
                    }

                    return existingOrganizationEmail;
                }
            )
            .map(organizationEmailRepository::save);
    }

    /**
     * Get all the organizationEmails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizationEmail> findAll(Pageable pageable) {
        log.debug("Request to get all OrganizationEmails");
        return organizationEmailRepository.findAll(pageable);
    }

    /**
     * Get one organizationEmail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrganizationEmail> findOne(Long id) {
        log.debug("Request to get OrganizationEmail : {}", id);
        return organizationEmailRepository.findById(id);
    }

    /**
     * Delete the organizationEmail by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrganizationEmail : {}", id);
        organizationEmailRepository.deleteById(id);
    }
}
