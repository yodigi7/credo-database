package com.credo.database.service;

import com.credo.database.domain.*; // for static metamodels
import com.credo.database.domain.OrganizationPhone;
import com.credo.database.repository.OrganizationPhoneRepository;
import com.credo.database.service.criteria.OrganizationPhoneCriteria;
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
 * Service for executing complex queries for {@link OrganizationPhone} entities in the database.
 * The main input is a {@link OrganizationPhoneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganizationPhone} or a {@link Page} of {@link OrganizationPhone} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganizationPhoneQueryService extends QueryService<OrganizationPhone> {

    private final Logger log = LoggerFactory.getLogger(OrganizationPhoneQueryService.class);

    private final OrganizationPhoneRepository organizationPhoneRepository;

    public OrganizationPhoneQueryService(OrganizationPhoneRepository organizationPhoneRepository) {
        this.organizationPhoneRepository = organizationPhoneRepository;
    }

    /**
     * Return a {@link List} of {@link OrganizationPhone} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganizationPhone> findByCriteria(OrganizationPhoneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrganizationPhone> specification = createSpecification(criteria);
        return organizationPhoneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrganizationPhone} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizationPhone> findByCriteria(OrganizationPhoneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrganizationPhone> specification = createSpecification(criteria);
        return organizationPhoneRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrganizationPhoneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrganizationPhone> specification = createSpecification(criteria);
        return organizationPhoneRepository.count(specification);
    }

    /**
     * Function to convert {@link OrganizationPhoneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrganizationPhone> createSpecification(OrganizationPhoneCriteria criteria) {
        Specification<OrganizationPhone> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrganizationPhone_.id));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), OrganizationPhone_.phoneNumber));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), OrganizationPhone_.type));
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationId(),
                            root -> root.join(OrganizationPhone_.organization, JoinType.LEFT).get(Organization_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
