package com.credo.database.repository;

import com.credo.database.domain.PersonEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PersonEmail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonEmailRepository extends JpaRepository<PersonEmail, Long>, JpaSpecificationExecutor<PersonEmail> {}
