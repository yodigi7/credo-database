package com.credo.database.repository;

import com.credo.database.domain.HouseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HouseDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HouseDetailsRepository extends JpaRepository<HouseDetails, Long>, JpaSpecificationExecutor<HouseDetails> {}
