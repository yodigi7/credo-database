package com.credo.database.service;

import com.credo.database.domain.OrganizationAddress;
import com.credo.database.repository.OrganizationAddressRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrganizationAddress}.
 */
@Service
@Transactional
public class OrganizationAddressService {

    private final Logger log = LoggerFactory.getLogger(OrganizationAddressService.class);

    private final OrganizationAddressRepository organizationAddressRepository;

    public OrganizationAddressService(OrganizationAddressRepository organizationAddressRepository) {
        this.organizationAddressRepository = organizationAddressRepository;
    }

    /**
     * Save a organizationAddress.
     *
     * @param organizationAddress the entity to save.
     * @return the persisted entity.
     */
    public OrganizationAddress save(OrganizationAddress organizationAddress) {
        log.debug("Request to save OrganizationAddress : {}", organizationAddress);
        return organizationAddressRepository.save(organizationAddress);
    }

    /**
     * Partially update a organizationAddress.
     *
     * @param organizationAddress the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrganizationAddress> partialUpdate(OrganizationAddress organizationAddress) {
        log.debug("Request to partially update OrganizationAddress : {}", organizationAddress);

        return organizationAddressRepository
            .findById(organizationAddress.getId())
            .map(
                existingOrganizationAddress -> {
                    if (organizationAddress.getStreetAddress() != null) {
                        existingOrganizationAddress.setStreetAddress(organizationAddress.getStreetAddress());
                    }
                    if (organizationAddress.getCity() != null) {
                        existingOrganizationAddress.setCity(organizationAddress.getCity());
                    }
                    if (organizationAddress.getState() != null) {
                        existingOrganizationAddress.setState(organizationAddress.getState());
                    }
                    if (organizationAddress.getZipcode() != null) {
                        existingOrganizationAddress.setZipcode(organizationAddress.getZipcode());
                    }

                    return existingOrganizationAddress;
                }
            )
            .map(organizationAddressRepository::save);
    }

    /**
     * Get all the organizationAddresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizationAddress> findAll(Pageable pageable) {
        log.debug("Request to get all OrganizationAddresses");
        return organizationAddressRepository.findAll(pageable);
    }

    /**
     * Get one organizationAddress by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrganizationAddress> findOne(Long id) {
        log.debug("Request to get OrganizationAddress : {}", id);
        return organizationAddressRepository.findById(id);
    }

    /**
     * Delete the organizationAddress by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrganizationAddress : {}", id);
        organizationAddressRepository.deleteById(id);
    }
}
