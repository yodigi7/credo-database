package com.credo.database.service;

import com.credo.database.domain.*;
import com.credo.database.repository.OrganizationRepository;
import com.credo.database.service.criteria.OrganizationCriteria;
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
 * Service for executing complex queries for {@link Organization} entities in the database.
 * The main input is a {@link OrganizationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Organization} or a {@link Page} of {@link Organization} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganizationQueryService extends QueryService<Organization> {

    private final Logger log = LoggerFactory.getLogger(OrganizationQueryService.class);

    private final OrganizationRepository organizationRepository;

    public OrganizationQueryService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    /**
     * Return a {@link List} of {@link Organization} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Organization> findByCriteria(OrganizationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Organization> specification = createSpecification(criteria);
        return organizationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Organization} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Organization> findByCriteria(OrganizationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Organization> specification = createSpecification(criteria);
        return organizationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrganizationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Organization> specification = createSpecification(criteria);
        return organizationRepository.count(specification);
    }

    /**
     * Function to convert {@link OrganizationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Organization> createSpecification(OrganizationCriteria criteria) {
        Specification<Organization> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Organization_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Organization_.name));
            }
            if (criteria.getMailingLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMailingLabel(), Organization_.mailingLabel));
            }
            if (criteria.getParishId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParishId(), root -> root.join(Organization_.parish, JoinType.LEFT).get(Parish_.id))
                    );
            }
            if (criteria.getNotesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotesId(),
                            root -> root.join(Organization_.notes, JoinType.LEFT).get(OrganizationNotes_.id)
                        )
                    );
            }
            if (criteria.getAddressesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAddressesId(),
                            root -> root.join(Organization_.addresses, JoinType.LEFT).get(OrganizationAddress_.id)
                        )
                    );
            }
            if (criteria.getPhonesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPhonesId(),
                            root -> root.join(Organization_.phones, JoinType.LEFT).get(OrganizationPhone_.id)
                        )
                    );
            }
            if (criteria.getEmailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmailsId(),
                            root -> root.join(Organization_.emails, JoinType.LEFT).get(OrganizationEmail_.id)
                        )
                    );
            }
            if (criteria.getPersonsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPersonsId(), root -> root.join(Organization_.persons, JoinType.LEFT).get(Person_.id))
                    );
            }
        }
        return specification;
    }
}
