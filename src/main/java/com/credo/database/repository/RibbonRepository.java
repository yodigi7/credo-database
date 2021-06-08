package com.credo.database.repository;

import com.credo.database.domain.Ribbon;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ribbon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RibbonRepository extends JpaRepository<Ribbon, Long>, JpaSpecificationExecutor<Ribbon> {}
