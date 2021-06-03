package com.credo.database.repository;

import com.credo.database.domain.OrganizationEmail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrganizationEmail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationEmailRepository extends JpaRepository<OrganizationEmail, Long>, JpaSpecificationExecutor<OrganizationEmail> {}
