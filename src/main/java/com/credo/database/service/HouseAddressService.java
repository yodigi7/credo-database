package com.credo.database.service;

import com.credo.database.domain.HouseAddress;
import com.credo.database.repository.HouseAddressRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HouseAddress}.
 */
@Service
@Transactional
public class HouseAddressService {

    private final Logger log = LoggerFactory.getLogger(HouseAddressService.class);

    private final HouseAddressRepository houseAddressRepository;

    public HouseAddressService(HouseAddressRepository houseAddressRepository) {
        this.houseAddressRepository = houseAddressRepository;
    }

    /**
     * Save a houseAddress.
     *
     * @param houseAddress the entity to save.
     * @return the persisted entity.
     */
    public HouseAddress save(HouseAddress houseAddress) {
        log.debug("Request to save HouseAddress : {}", houseAddress);
        return houseAddressRepository.save(houseAddress);
    }

    /**
     * Partially update a houseAddress.
     *
     * @param houseAddress the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HouseAddress> partialUpdate(HouseAddress houseAddress) {
        log.debug("Request to partially update HouseAddress : {}", houseAddress);

        return houseAddressRepository
            .findById(houseAddress.getId())
            .map(
                existingHouseAddress -> {
                    if (houseAddress.getStreetAddress() != null) {
                        existingHouseAddress.setStreetAddress(houseAddress.getStreetAddress());
                    }
                    if (houseAddress.getCity() != null) {
                        existingHouseAddress.setCity(houseAddress.getCity());
                    }
                    if (houseAddress.getState() != null) {
                        existingHouseAddress.setState(houseAddress.getState());
                    }
                    if (houseAddress.getZipcode() != null) {
                        existingHouseAddress.setZipcode(houseAddress.getZipcode());
                    }
                    if (houseAddress.getType() != null) {
                        existingHouseAddress.setType(houseAddress.getType());
                    }
                    if (houseAddress.getMailNewsletterSubscription() != null) {
                        existingHouseAddress.setMailNewsletterSubscription(houseAddress.getMailNewsletterSubscription());
                    }
                    if (houseAddress.getMailEventNotificationSubscription() != null) {
                        existingHouseAddress.setMailEventNotificationSubscription(houseAddress.getMailEventNotificationSubscription());
                    }

                    return existingHouseAddress;
                }
            )
            .map(houseAddressRepository::save);
    }

    /**
     * Get all the houseAddresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HouseAddress> findAll(Pageable pageable) {
        log.debug("Request to get all HouseAddresses");
        return houseAddressRepository.findAll(pageable);
    }

    /**
     * Get one houseAddress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HouseAddress> findOne(Long id) {
        log.debug("Request to get HouseAddress : {}", id);
        return houseAddressRepository.findById(id);
    }

    /**
     * Delete the houseAddress by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete HouseAddress : {}", id);
        houseAddressRepository.deleteById(id);
    }
}
