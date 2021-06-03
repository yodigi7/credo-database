package com.credo.database.repository;

import com.credo.database.domain.OrganizationAddress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrganizationAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationAddressRepository
    extends JpaRepository<OrganizationAddress, Long>, JpaSpecificationExecutor<OrganizationAddress> {}
