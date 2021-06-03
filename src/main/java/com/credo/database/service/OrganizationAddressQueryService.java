package com.credo.database.service;

import com.credo.database.domain.*; // for static metamodels
import com.credo.database.domain.OrganizationAddress;
import com.credo.database.repository.OrganizationAddressRepository;
import com.credo.database.service.criteria.OrganizationAddressCriteria;
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
 * Service for executing complex queries for {@link OrganizationAddress} entities in the database.
 * The main input is a {@link OrganizationAddressCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganizationAddress} or a {@link Page} of {@link OrganizationAddress} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganizationAddressQueryService extends QueryService<OrganizationAddress> {

    private final Logger log = LoggerFactory.getLogger(OrganizationAddressQueryService.class);

    private final OrganizationAddressRepository organizationAddressRepository;

    public OrganizationAddressQueryService(OrganizationAddressRepository organizationAddressRepository) {
        this.organizationAddressRepository = organizationAddressRepository;
    }

    /**
     * Return a {@link List} of {@link OrganizationAddress} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganizationAddress> findByCriteria(OrganizationAddressCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrganizationAddress> specification = createSpecification(criteria);
        return organizationAddressRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrganizationAddress} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizationAddress> findByCriteria(OrganizationAddressCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrganizationAddress> specification = createSpecification(criteria);
        return organizationAddressRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrganizationAddressCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrganizationAddress> specification = createSpecification(criteria);
        return organizationAddressRepository.count(specification);
    }

    /**
     * Function to convert {@link OrganizationAddressCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrganizationAddress> createSpecification(OrganizationAddressCriteria criteria) {
        Specification<OrganizationAddress> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrganizationAddress_.id));
            }
            if (criteria.getStreetAddress() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getStreetAddress(), OrganizationAddress_.streetAddress));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), OrganizationAddress_.city));
            }
            if (criteria.getState() != null) {
                specification = specification.and(buildStringSpecification(criteria.getState(), OrganizationAddress_.state));
            }
            if (criteria.getZipcode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZipcode(), OrganizationAddress_.zipcode));
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationId(),
                            root -> root.join(OrganizationAddress_.organization, JoinType.LEFT).get(Organization_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
