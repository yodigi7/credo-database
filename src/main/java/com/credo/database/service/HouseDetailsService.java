package com.credo.database.service;

import com.credo.database.domain.HouseDetails;
import com.credo.database.repository.HouseDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HouseDetails}.
 */
@Service
@Transactional
public class HouseDetailsService {

    private final Logger log = LoggerFactory.getLogger(HouseDetailsService.class);

    private final HouseDetailsRepository houseDetailsRepository;

    public HouseDetailsService(HouseDetailsRepository houseDetailsRepository) {
        this.houseDetailsRepository = houseDetailsRepository;
    }

    /**
     * Save a houseDetails.
     *
     * @param houseDetails the entity to save.
     * @return the persisted entity.
     */
    public HouseDetails save(HouseDetails houseDetails) {
        log.debug("Request to save HouseDetails : {}", houseDetails);
        return houseDetailsRepository.save(houseDetails);
    }

    /**
     * Partially update a houseDetails.
     *
     * @param houseDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HouseDetails> partialUpdate(HouseDetails houseDetails) {
        log.debug("Request to partially update HouseDetails : {}", houseDetails);

        return houseDetailsRepository
            .findById(houseDetails.getId())
            .map(
                existingHouseDetails -> {
                    if (houseDetails.getMailingLabel() != null) {
                        existingHouseDetails.setMailingLabel(houseDetails.getMailingLabel());
                    }

                    return existingHouseDetails;
                }
            )
            .map(houseDetailsRepository::save);
    }

    /**
     * Get all the houseDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HouseDetails> findAll(Pageable pageable) {
        log.debug("Request to get all HouseDetails");
        return houseDetailsRepository.findAll(pageable);
    }

    /**
     * Get one houseDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HouseDetails> findOne(Long id) {
        log.debug("Request to get HouseDetails : {}", id);
        return houseDetailsRepository.findById(id);
    }

    /**
     * Delete the houseDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HouseDetails : {}", id);
        houseDetailsRepository.deleteById(id);
    }
}
