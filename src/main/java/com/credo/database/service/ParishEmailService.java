package com.credo.database.service;

import com.credo.database.domain.ParishEmail;
import com.credo.database.repository.ParishEmailRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ParishEmail}.
 */
@Service
@Transactional
public class ParishEmailService {

    private final Logger log = LoggerFactory.getLogger(ParishEmailService.class);

    private final ParishEmailRepository parishEmailRepository;

    public ParishEmailService(ParishEmailRepository parishEmailRepository) {
        this.parishEmailRepository = parishEmailRepository;
    }

    /**
     * Save a parishEmail.
     *
     * @param parishEmail the entity to save.
     * @return the persisted entity.
     */
    public ParishEmail save(ParishEmail parishEmail) {
        log.debug("Request to save ParishEmail : {}", parishEmail);
        return parishEmailRepository.save(parishEmail);
    }

    /**
     * Partially update a parishEmail.
     *
     * @param parishEmail the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParishEmail> partialUpdate(ParishEmail parishEmail) {
        log.debug("Request to partially update ParishEmail : {}", parishEmail);

        return parishEmailRepository
            .findById(parishEmail.getId())
            .map(
                existingParishEmail -> {
                    if (parishEmail.getEmail() != null) {
                        existingParishEmail.setEmail(parishEmail.getEmail());
                    }
                    if (parishEmail.getType() != null) {
                        existingParishEmail.setType(parishEmail.getType());
                    }
                    if (parishEmail.getEmailNewsletterSubscription() != null) {
                        existingParishEmail.setEmailNewsletterSubscription(parishEmail.getEmailNewsletterSubscription());
                    }
                    if (parishEmail.getEmailEventNotificationSubscription() != null) {
                        existingParishEmail.setEmailEventNotificationSubscription(parishEmail.getEmailEventNotificationSubscription());
                    }

                    return existingParishEmail;
                }
            )
            .map(parishEmailRepository::save);
    }

    /**
     * Get all the parishEmails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParishEmail> findAll(Pageable pageable) {
        log.debug("Request to get all ParishEmails");
        return parishEmailRepository.findAll(pageable);
    }

    /**
     * Get one parishEmail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParishEmail> findOne(Long id) {
        log.debug("Request to get ParishEmail : {}", id);
        return parishEmailRepository.findById(id);
    }

    /**
     * Delete the parishEmail by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ParishEmail : {}", id);
        parishEmailRepository.deleteById(id);
    }
}
