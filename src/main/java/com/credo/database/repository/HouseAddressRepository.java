package com.credo.database.repository;

import com.credo.database.domain.HouseAddress;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HouseAddress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HouseAddressRepository extends JpaRepository<HouseAddress, Long>, JpaSpecificationExecutor<HouseAddress> {}
