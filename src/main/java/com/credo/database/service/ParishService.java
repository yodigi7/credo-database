package com.credo.database.service;

import com.credo.database.domain.Parish;
import com.credo.database.repository.ParishRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Parish}.
 */
@Service
@Transactional
public class ParishService {

    private final Logger log = LoggerFactory.getLogger(ParishService.class);

    private final ParishRepository parishRepository;

    public ParishService(ParishRepository parishRepository) {
        this.parishRepository = parishRepository;
    }

    /**
     * Save a parish.
     *
     * @param parish the entity to save.
     * @return the persisted entity.
     */
    public Parish save(Parish parish) {
        log.debug("Request to save Parish : {}", parish);
        return parishRepository.save(parish);
    }

    /**
     * Partially update a parish.
     *
     * @param parish the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Parish> partialUpdate(Parish parish) {
        log.debug("Request to partially update Parish : {}", parish);

        return parishRepository
            .findById(parish.getId())
            .map(
                existingParish -> {
                    if (parish.getName() != null) {
                        existingParish.setName(parish.getName());
                    }

                    return existingParish;
                }
            )
            .map(parishRepository::save);
    }

    /**
     * Get all the parishes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Parish> findAll(Pageable pageable) {
        log.debug("Request to get all Parishes");
        return parishRepository.findAll(pageable);
    }

    /**
     * Get one parish by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Parish> findOne(Long id) {
        log.debug("Request to get Parish : {}", id);
        return parishRepository.findById(id);
    }

    /**
     * Delete the parish by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Parish : {}", id);
        parishRepository.deleteById(id);
    }
}
