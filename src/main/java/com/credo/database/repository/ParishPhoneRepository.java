package com.credo.database.repository;

import com.credo.database.domain.ParishPhone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ParishPhone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParishPhoneRepository extends JpaRepository<ParishPhone, Long>, JpaSpecificationExecutor<ParishPhone> {}
