package com.credo.database.repository;

import com.credo.database.domain.OrganizationNotes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OrganizationNotes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationNotesRepository extends JpaRepository<OrganizationNotes, Long>, JpaSpecificationExecutor<OrganizationNotes> {}
