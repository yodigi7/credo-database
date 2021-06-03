package com.credo.database.service;

import com.credo.database.domain.*; // for static metamodels
import com.credo.database.domain.OrganizationEmail;
import com.credo.database.repository.OrganizationEmailRepository;
import com.credo.database.service.criteria.OrganizationEmailCriteria;
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
 * Service for executing complex queries for {@link OrganizationEmail} entities in the database.
 * The main input is a {@link OrganizationEmailCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganizationEmail} or a {@link Page} of {@link OrganizationEmail} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganizationEmailQueryService extends QueryService<OrganizationEmail> {

    private final Logger log = LoggerFactory.getLogger(OrganizationEmailQueryService.class);

    private final OrganizationEmailRepository organizationEmailRepository;

    public OrganizationEmailQueryService(OrganizationEmailRepository organizationEmailRepository) {
        this.organizationEmailRepository = organizationEmailRepository;
    }

    /**
     * Return a {@link List} of {@link OrganizationEmail} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganizationEmail> findByCriteria(OrganizationEmailCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrganizationEmail> specification = createSpecification(criteria);
        return organizationEmailRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrganizationEmail} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizationEmail> findByCriteria(OrganizationEmailCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrganizationEmail> specification = createSpecification(criteria);
        return organizationEmailRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrganizationEmailCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrganizationEmail> specification = createSpecification(criteria);
        return organizationEmailRepository.count(specification);
    }

    /**
     * Function to convert {@link OrganizationEmailCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrganizationEmail> createSpecification(OrganizationEmailCriteria criteria) {
        Specification<OrganizationEmail> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrganizationEmail_.id));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), OrganizationEmail_.email));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), OrganizationEmail_.type));
            }
            if (criteria.getEmailNewsletterSubscription() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmailNewsletterSubscription(), OrganizationEmail_.emailNewsletterSubscription)
                    );
            }
            if (criteria.getEmailEventNotificationSubscription() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmailEventNotificationSubscription(),
                            OrganizationEmail_.emailEventNotificationSubscription
                        )
                    );
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationId(),
                            root -> root.join(OrganizationEmail_.organization, JoinType.LEFT).get(Organization_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
