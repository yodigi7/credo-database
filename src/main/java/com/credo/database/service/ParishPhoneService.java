package com.credo.database.service;

import com.credo.database.domain.ParishPhone;
import com.credo.database.repository.ParishPhoneRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ParishPhone}.
 */
@Service
@Transactional
public class ParishPhoneService {

    private final Logger log = LoggerFactory.getLogger(ParishPhoneService.class);

    private final ParishPhoneRepository parishPhoneRepository;

    public ParishPhoneService(ParishPhoneRepository parishPhoneRepository) {
        this.parishPhoneRepository = parishPhoneRepository;
    }

    /**
     * Save a parishPhone.
     *
     * @param parishPhone the entity to save.
     * @return the persisted entity.
     */
    public ParishPhone save(ParishPhone parishPhone) {
        log.debug("Request to save ParishPhone : {}", parishPhone);
        return parishPhoneRepository.save(parishPhone);
    }

    /**
     * Partially update a parishPhone.
     *
     * @param parishPhone the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParishPhone> partialUpdate(ParishPhone parishPhone) {
        log.debug("Request to partially update ParishPhone : {}", parishPhone);

        return parishPhoneRepository
            .findById(parishPhone.getId())
            .map(
                existingParishPhone -> {
                    if (parishPhone.getPhoneNumber() != null) {
                        existingParishPhone.setPhoneNumber(parishPhone.getPhoneNumber());
                    }
                    if (parishPhone.getType() != null) {
                        existingParishPhone.setType(parishPhone.getType());
                    }

                    return existingParishPhone;
                }
            )
            .map(parishPhoneRepository::save);
    }

    /**
     * Get all the parishPhones.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParishPhone> findAll(Pageable pageable) {
        log.debug("Request to get all ParishPhones");
        return parishPhoneRepository.findAll(pageable);
    }

    /**
     * Get one parishPhone by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParishPhone> findOne(Long id) {
        log.debug("Request to get ParishPhone : {}", id);
        return parishPhoneRepository.findById(id);
    }

    /**
     * Delete the parishPhone by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ParishPhone : {}", id);
        parishPhoneRepository.deleteById(id);
    }
}
