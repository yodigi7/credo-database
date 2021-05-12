package com.credo.database.service;

import com.credo.database.domain.PersonEmail;
import com.credo.database.repository.PersonEmailRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PersonEmail}.
 */
@Service
@Transactional
public class PersonEmailService {

    private final Logger log = LoggerFactory.getLogger(PersonEmailService.class);

    private final PersonEmailRepository personEmailRepository;

    public PersonEmailService(PersonEmailRepository personEmailRepository) {
        this.personEmailRepository = personEmailRepository;
    }

    /**
     * Save a personEmail.
     *
     * @param personEmail the entity to save.
     * @return the persisted entity.
     */
    public PersonEmail save(PersonEmail personEmail) {
        log.debug("Request to save PersonEmail : {}", personEmail);
        return personEmailRepository.save(personEmail);
    }

    /**
     * Partially update a personEmail.
     *
     * @param personEmail the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonEmail> partialUpdate(PersonEmail personEmail) {
        log.debug("Request to partially update PersonEmail : {}", personEmail);

        return personEmailRepository
            .findById(personEmail.getId())
            .map(
                existingPersonEmail -> {
                    if (personEmail.getEmail() != null) {
                        existingPersonEmail.setEmail(personEmail.getEmail());
                    }
                    if (personEmail.getType() != null) {
                        existingPersonEmail.setType(personEmail.getType());
                    }
                    if (personEmail.getEmailNewsletterSubscription() != null) {
                        existingPersonEmail.setEmailNewsletterSubscription(personEmail.getEmailNewsletterSubscription());
                    }
                    if (personEmail.getEmailEventNotificationSubscription() != null) {
                        existingPersonEmail.setEmailEventNotificationSubscription(personEmail.getEmailEventNotificationSubscription());
                    }

                    return existingPersonEmail;
                }
            )
            .map(personEmailRepository::save);
    }

    /**
     * Get all the personEmails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonEmail> findAll(Pageable pageable) {
        log.debug("Request to get all PersonEmails");
        return personEmailRepository.findAll(pageable);
    }

    /**
     * Get one personEmail by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonEmail> findOne(Long id) {
        log.debug("Request to get PersonEmail : {}", id);
        return personEmailRepository.findById(id);
    }

    /**
     * Delete the personEmail by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonEmail : {}", id);
        personEmailRepository.deleteById(id);
    }
}
