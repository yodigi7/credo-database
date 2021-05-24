package com.credo.database.service;

import com.credo.database.domain.PersonNotes;
import com.credo.database.repository.PersonNotesRepository;
import com.credo.database.repository.PersonRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PersonNotes}.
 */
@Service
@Transactional
public class PersonNotesService {

    private final Logger log = LoggerFactory.getLogger(PersonNotesService.class);

    private final PersonNotesRepository personNotesRepository;
    private final PersonRepository personRepository;

    public PersonNotesService(PersonNotesRepository personNotesRepository, PersonRepository personRepository) {
        this.personNotesRepository = personNotesRepository;
        this.personRepository = personRepository;
    }

    /**
     * Save a personNotes.
     *
     * @param personNotes the entity to save.
     * @return the persisted entity.
     */
    public PersonNotes save(PersonNotes personNotes) {
        log.debug("Request to save PersonNotes : {}", personNotes);
        if (personNotes.getPerson() != null) {
            if (personNotes.getPerson().getId() != null) {
                personNotes.setPerson(
                    personRepository.findById(personNotes.getPerson().getId()).orElseThrow(() -> new Error("unable to save"))
                );
            }
        }
        return personNotesRepository.save(personNotes);
    }

    /**
     * Partially update a personNotes.
     *
     * @param personNotes the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PersonNotes> partialUpdate(PersonNotes personNotes) {
        log.debug("Request to partially update PersonNotes : {}", personNotes);

        return personNotesRepository
            .findById(personNotes.getId())
            .map(
                existingPersonNotes -> {
                    if (personNotes.getNotes() != null) {
                        existingPersonNotes.setNotes(personNotes.getNotes());
                    }

                    return existingPersonNotes;
                }
            )
            .map(personNotesRepository::save);
    }

    /**
     * Get all the personNotes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonNotes> findAll(Pageable pageable) {
        log.debug("Request to get all PersonNotes");
        return personNotesRepository.findAll(pageable);
    }

    /**
     * Get one personNotes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PersonNotes> findOne(Long id) {
        log.debug("Request to get PersonNotes : {}", id);
        return personNotesRepository.findById(id);
    }

    /**
     * Delete the personNotes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonNotes : {}", id);
        personNotesRepository.deleteById(id);
    }
}
