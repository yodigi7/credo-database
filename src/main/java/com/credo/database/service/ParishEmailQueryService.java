package com.credo.database.service;

import com.credo.database.domain.*; // for static metamodels
import com.credo.database.domain.ParishEmail;
import com.credo.database.repository.ParishEmailRepository;
import com.credo.database.service.criteria.ParishEmailCriteria;
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
 * Service for executing complex queries for {@link ParishEmail} entities in the database.
 * The main input is a {@link ParishEmailCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParishEmail} or a {@link Page} of {@link ParishEmail} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParishEmailQueryService extends QueryService<ParishEmail> {

    private final Logger log = LoggerFactory.getLogger(ParishEmailQueryService.class);

    private final ParishEmailRepository parishEmailRepository;

    public ParishEmailQueryService(ParishEmailRepository parishEmailRepository) {
        this.parishEmailRepository = parishEmailRepository;
    }

    /**
     * Return a {@link List} of {@link ParishEmail} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParishEmail> findByCriteria(ParishEmailCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ParishEmail> specification = createSpecification(criteria);
        return parishEmailRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ParishEmail} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParishEmail> findByCriteria(ParishEmailCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ParishEmail> specification = createSpecification(criteria);
        return parishEmailRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParishEmailCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ParishEmail> specification = createSpecification(criteria);
        return parishEmailRepository.count(specification);
    }

    /**
     * Function to convert {@link ParishEmailCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ParishEmail> createSpecification(ParishEmailCriteria criteria) {
        Specification<ParishEmail> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ParishEmail_.id));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), ParishEmail_.email));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ParishEmail_.type));
            }
            if (criteria.getEmailNewsletterSubscription() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmailNewsletterSubscription(), ParishEmail_.emailNewsletterSubscription)
                    );
            }
            if (criteria.getEmailEventNotificationSubscription() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmailEventNotificationSubscription(),
                            ParishEmail_.emailEventNotificationSubscription
                        )
                    );
            }
            if (criteria.getParishId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParishId(), root -> root.join(ParishEmail_.parish, JoinType.LEFT).get(Parish_.id))
                    );
            }
        }
        return specification;
    }
}
