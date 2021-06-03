package com.credo.database.service;

import com.credo.database.domain.*; // for static metamodels
import com.credo.database.domain.Parish;
import com.credo.database.repository.ParishRepository;
import com.credo.database.service.criteria.ParishCriteria;
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
 * Service for executing complex queries for {@link Parish} entities in the database.
 * The main input is a {@link ParishCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Parish} or a {@link Page} of {@link Parish} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParishQueryService extends QueryService<Parish> {

    private final Logger log = LoggerFactory.getLogger(ParishQueryService.class);

    private final ParishRepository parishRepository;

    public ParishQueryService(ParishRepository parishRepository) {
        this.parishRepository = parishRepository;
    }

    /**
     * Return a {@link List} of {@link Parish} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Parish> findByCriteria(ParishCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Parish> specification = createSpecification(criteria);
        return parishRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Parish} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Parish> findByCriteria(ParishCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Parish> specification = createSpecification(criteria);
        return parishRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParishCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Parish> specification = createSpecification(criteria);
        return parishRepository.count(specification);
    }

    /**
     * Function to convert {@link ParishCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Parish> createSpecification(ParishCriteria criteria) {
        Specification<Parish> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Parish_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Parish_.name));
            }
            if (criteria.getOrganizationsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationsId(),
                            root -> root.join(Parish_.organizations, JoinType.LEFT).get(Organization_.id)
                        )
                    );
            }
            if (criteria.getPhonesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPhonesId(), root -> root.join(Parish_.phones, JoinType.LEFT).get(ParishPhone_.id))
                    );
            }
            if (criteria.getPeopleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPeopleId(), root -> root.join(Parish_.people, JoinType.LEFT).get(Person_.id))
                    );
            }
            if (criteria.getEmailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmailsId(), root -> root.join(Parish_.emails, JoinType.LEFT).get(ParishEmail_.id))
                    );
            }
        }
        return specification;
    }
}
