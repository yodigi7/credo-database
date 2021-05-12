package com.credo.database.service;

import com.credo.database.domain.Person;
import com.credo.database.repository.PersonRepository;
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
 * Service Implementation for managing {@link Person}.
 */
@Service
@Transactional
public class PersonService {

    private final Logger log = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Save a person.
     *
     * @param person the entity to save.
     * @return the persisted entity.
     */
    public Person save(Person person) {
        log.debug("Request to save Person : {}", person);
        return personRepository.save(person);
    }

    /**
     * Partially update a person.
     *
     * @param person the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Person> partialUpdate(Person person) {
        log.debug("Request to partially update Person : {}", person);

        return personRepository
            .findById(person.getId())
            .map(
                existingPerson -> {
                    if (person.getPrefix() != null) {
                        existingPerson.setPrefix(person.getPrefix());
                    }
                    if (person.getPreferredName() != null) {
                        existingPerson.setPreferredName(person.getPreferredName());
                    }
                    if (person.getFirstName() != null) {
                        existingPerson.setFirstName(person.getFirstName());
                    }
                    if (person.getMiddleName() != null) {
                        existingPerson.setMiddleName(person.getMiddleName());
                    }
                    if (person.getLastName() != null) {
                        existingPerson.setLastName(person.getLastName());
                    }
                    if (person.getSuffix() != null) {
                        existingPerson.setSuffix(person.getSuffix());
                    }
                    if (person.getNameTag() != null) {
                        existingPerson.setNameTag(person.getNameTag());
                    }
                    if (person.getCurrentMember() != null) {
                        existingPerson.setCurrentMember(person.getCurrentMember());
                    }
                    if (person.getMembershipStartDate() != null) {
                        existingPerson.setMembershipStartDate(person.getMembershipStartDate());
                    }
                    if (person.getMembershipExpirationDate() != null) {
                        existingPerson.setMembershipExpirationDate(person.getMembershipExpirationDate());
                    }
                    if (person.getIsHeadOfHouse() != null) {
                        existingPerson.setIsHeadOfHouse(person.getIsHeadOfHouse());
                    }
                    if (person.getIsDeceased() != null) {
                        existingPerson.setIsDeceased(person.getIsDeceased());
                    }

                    return existingPerson;
                }
            )
            .map(personRepository::save);
    }

    /**
     * Get all the people.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Person> findAll(Pageable pageable) {
        log.debug("Request to get all People");
        return personRepository.findAll(pageable);
    }

    /**
     * Get all the people with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Person> findAllWithEagerRelationships(Pageable pageable) {
        return personRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the people where HouseDetails is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Person> findAllWhereHouseDetailsIsNull() {
        log.debug("Request to get all people where HouseDetails is null");
        return StreamSupport
            .stream(personRepository.findAll().spliterator(), false)
            .filter(person -> person.getHouseDetails() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get all the people where Notes is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Person> findAllWhereNotesIsNull() {
        log.debug("Request to get all people where Notes is null");
        return StreamSupport
            .stream(personRepository.findAll().spliterator(), false)
            .filter(person -> person.getNotes() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one person by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Person> findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        return personRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the person by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        personRepository.deleteById(id);
    }
}
