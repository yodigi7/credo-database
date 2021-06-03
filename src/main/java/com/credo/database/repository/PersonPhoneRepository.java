package com.credo.database.repository;

import com.credo.database.domain.PersonPhone;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonPhone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonPhoneRepository extends JpaRepository<PersonPhone, Long>, JpaSpecificationExecutor<PersonPhone> {}
