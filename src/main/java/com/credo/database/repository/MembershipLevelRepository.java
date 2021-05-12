package com.credo.database.repository;

import com.credo.database.domain.MembershipLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MembershipLevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipLevelRepository extends JpaRepository<MembershipLevel, Long>, JpaSpecificationExecutor<MembershipLevel> {}
