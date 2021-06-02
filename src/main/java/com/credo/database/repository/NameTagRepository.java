package com.credo.database.repository;

import com.credo.database.domain.NameTag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NameTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NameTagRepository extends JpaRepository<NameTag, Long>, JpaSpecificationExecutor<NameTag> {}
