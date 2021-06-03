package com.credo.database.repository;

import com.credo.database.domain.Parish;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Parish entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParishRepository extends JpaRepository<Parish, Long>, JpaSpecificationExecutor<Parish> {}
