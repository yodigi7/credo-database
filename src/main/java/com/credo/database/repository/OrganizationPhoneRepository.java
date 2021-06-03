package com.credo.database.repository;

import com.credo.database.domain.OrganizationPhone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrganizationPhone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationPhoneRepository extends JpaRepository<OrganizationPhone, Long>, JpaSpecificationExecutor<OrganizationPhone> {}
