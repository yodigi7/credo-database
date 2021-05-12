package com.credo.database.service;

import com.credo.database.domain.HouseAddress;
import com.credo.database.domain.HouseAddress_;
import com.credo.database.domain.HouseDetails_;
import com.credo.database.repository.HouseAddressRepository;
import com.credo.database.service.criteria.HouseAddressCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HouseAddress} entities in the database.
 * The main input is a {@link HouseAddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HouseAddress} or a {@link Page} of {@link HouseAddress} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HouseAddressQueryService extends QueryService<HouseAddress> {

    private final Logger log = LoggerFactory.getLogger(HouseAddressQueryService.class);

    private final HouseAddressRepository houseAddressRepository;

    public HouseAddressQueryService(HouseAddressRepository houseAddressRepository) {
        this.houseAddressRepository = houseAddressRepository;
    }

    /**
     * Return a {@link List} of {@link HouseAddress} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HouseAddress> findByCriteria(HouseAddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HouseAddress> specification = createSpecification(criteria);
        return houseAddressRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link HouseAddress} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HouseAddress> findByCriteria(HouseAddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HouseAddress> specification = createSpecification(criteria);
        return houseAddressRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HouseAddressCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HouseAddress> specification = createSpecification(criteria);
        return houseAddressRepository.count(specification);
    }

    /**
     * Function to convert {@link HouseAddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HouseAddress> createSpecification(HouseAddressCriteria criteria) {
        Specification<HouseAddress> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HouseAddress_.id));
            }
            if (criteria.getStreetAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStreetAddress(), HouseAddress_.streetAddress));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), HouseAddress_.city));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), HouseAddress_.state));
            }
            if (criteria.getZipcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZipcode(), HouseAddress_.zipcode));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), HouseAddress_.type));
            }
            if (criteria.getMailNewsletterSubscription() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMailNewsletterSubscription(), HouseAddress_.mailNewsletterSubscription)
                    );
            }
            if (criteria.getMailEventNotificationSubscription() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMailEventNotificationSubscription(), HouseAddress_.mailEventNotificationSubscription)
                    );
            }
            if (criteria.getHouseDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHouseDetailsId(),
                            root -> root.join(HouseAddress_.houseDetails, JoinType.LEFT).get(HouseDetails_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
