package com.credo.database.service;

import com.credo.database.domain.EventPerk;
import com.credo.database.repository.EventPerkRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EventPerk}.
 */
@Service
@Transactional
public class EventPerkService {

    private final Logger log = LoggerFactory.getLogger(EventPerkService.class);

    private final EventPerkRepository eventPerkRepository;

    public EventPerkService(EventPerkRepository eventPerkRepository) {
        this.eventPerkRepository = eventPerkRepository;
    }

    /**
     * Save a eventPerk.
     *
     * @param eventPerk the entity to save.
     * @return the persisted entity.
     */
    public EventPerk save(EventPerk eventPerk) {
        log.debug("Request to save EventPerk : {}", eventPerk);
        return eventPerkRepository.save(eventPerk);
    }

    /**
     * Partially update a eventPerk.
     *
     * @param eventPerk the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EventPerk> partialUpdate(EventPerk eventPerk) {
        log.debug("Request to partially update EventPerk : {}", eventPerk);

        return eventPerkRepository
            .findById(eventPerk.getId())
            .map(
                existingEventPerk -> {
                    if (eventPerk.getName() != null) {
                        existingEventPerk.setName(eventPerk.getName());
                    }
                    if (eventPerk.getMinimumPrice() != null) {
                        existingEventPerk.setMinimumPrice(eventPerk.getMinimumPrice());
                    }

                    return existingEventPerk;
                }
            )
            .map(eventPerkRepository::save);
    }

    /**
     * Get all the eventPerks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EventPerk> findAll(Pageable pageable) {
        log.debug("Request to get all EventPerks");
        return eventPerkRepository.findAll(pageable);
    }

    /**
     * Get one eventPerk by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EventPerk> findOne(Long id) {
        log.debug("Request to get EventPerk : {}", id);
        return eventPerkRepository.findById(id);
    }

    /**
     * Delete the eventPerk by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EventPerk : {}", id);
        eventPerkRepository.deleteById(id);
    }
}
