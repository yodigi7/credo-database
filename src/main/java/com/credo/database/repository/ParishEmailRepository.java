package com.credo.database.repository;

import com.credo.database.domain.ParishEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ParishEmail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParishEmailRepository extends JpaRepository<ParishEmail, Long>, JpaSpecificationExecutor<ParishEmail> {}
