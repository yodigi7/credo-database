package com.credo.database.repository;

import com.credo.database.domain.EventPerk;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EventPerk entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventPerkRepository extends JpaRepository<EventPerk, Long>, JpaSpecificationExecutor<EventPerk> {}
