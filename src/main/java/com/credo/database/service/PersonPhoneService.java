package com.credo.database.service;

import com.credo.database.domain.PersonPhone;
import com.credo.database.repository.PersonPhoneRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PersonPhone}.
 */
@Service
@Transactional
public class PersonPhoneService {

    private final Logger log = LoggerFactory.getLogger(PersonPhoneService.class);

    private final PersonPhoneRepository personPhoneRepository;

    public PersonPhoneService(PersonPhoneRepository personPhoneRepository) {
        this.personPhoneRepository = personPhoneRepository;
    }

    /**
     * Save a personPhone.
     *
     * @param personPhone the entity to save.
     * @return the persisted entity.
     */
    public PersonPhone save(PersonPhone personPhone) {
        log.debug("Request to save PersonPhone : {}", personPhone);
        return personPhoneRepository.save(personPhone);
    }

    /**
     * Partially update a personPhone.
     *
     * @param personPhone the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonPhone> partialUpdate(PersonPhone personPhone) {
        log.debug("Request to partially update PersonPhone : {}", personPhone);

        return personPhoneRepository
            .findById(personPhone.getId())
            .map(
                existingPersonPhone -> {
                    if (personPhone.getPhoneNumber() != null) {
                        existingPersonPhone.setPhoneNumber(personPhone.getPhoneNumber());
                    }
                    if (personPhone.getType() != null) {
                        existingPersonPhone.setType(personPhone.getType());
                    }

                    return existingPersonPhone;
                }
            )
            .map(personPhoneRepository::save);
    }

    /**
     * Get all the personPhones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonPhone> findAll(Pageable pageable) {
        log.debug("Request to get all PersonPhones");
        return personPhoneRepository.findAll(pageable);
    }

    /**
     * Get one personPhone by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonPhone> findOne(Long id) {
        log.debug("Request to get PersonPhone : {}", id);
        return personPhoneRepository.findById(id);
    }

    /**
     * Delete the personPhone by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonPhone : {}", id);
        personPhoneRepository.deleteById(id);
    }
}
