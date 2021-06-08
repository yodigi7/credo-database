package com.credo.database.service;

import com.credo.database.domain.Ribbon;
import com.credo.database.repository.RibbonRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ribbon}.
 */
@Service
@Transactional
public class RibbonService {

    private final Logger log = LoggerFactory.getLogger(RibbonService.class);

    private final RibbonRepository ribbonRepository;

    public RibbonService(RibbonRepository ribbonRepository) {
        this.ribbonRepository = ribbonRepository;
    }

    /**
     * Save a ribbon.
     *
     * @param ribbon the entity to save.
     * @return the persisted entity.
     */
    public Ribbon save(Ribbon ribbon) {
        log.debug("Request to save Ribbon : {}", ribbon);
        return ribbonRepository.save(ribbon);
    }

    /**
     * Partially update a ribbon.
     *
     * @param ribbon the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Ribbon> partialUpdate(Ribbon ribbon) {
        log.debug("Request to partially update Ribbon : {}", ribbon);

        return ribbonRepository
            .findById(ribbon.getId())
            .map(
                existingRibbon -> {
                    if (ribbon.getName() != null) {
                        existingRibbon.setName(ribbon.getName());
                    }

                    return existingRibbon;
                }
            )
            .map(ribbonRepository::save);
    }

    /**
     * Get all the ribbons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Ribbon> findAll(Pageable pageable) {
        log.debug("Request to get all Ribbons");
        return ribbonRepository.findAll(pageable);
    }

    /**
     * Get one ribbon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Ribbon> findOne(Long id) {
        log.debug("Request to get Ribbon : {}", id);
        return ribbonRepository.findById(id);
    }

    /**
     * Delete the ribbon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ribbon : {}", id);
        ribbonRepository.deleteById(id);
    }
}
