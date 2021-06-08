package com.credo.database.service;

import com.credo.database.domain.*; // for static metamodels
import com.credo.database.domain.Ribbon;
import com.credo.database.repository.RibbonRepository;
import com.credo.database.service.criteria.RibbonCriteria;
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
 * Service for executing complex queries for {@link Ribbon} entities in the database.
 * The main input is a {@link RibbonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ribbon} or a {@link Page} of {@link Ribbon} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RibbonQueryService extends QueryService<Ribbon> {

    private final Logger log = LoggerFactory.getLogger(RibbonQueryService.class);

    private final RibbonRepository ribbonRepository;

    public RibbonQueryService(RibbonRepository ribbonRepository) {
        this.ribbonRepository = ribbonRepository;
    }

    /**
     * Return a {@link List} of {@link Ribbon} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ribbon> findByCriteria(RibbonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ribbon> specification = createSpecification(criteria);
        return ribbonRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ribbon} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ribbon> findByCriteria(RibbonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ribbon> specification = createSpecification(criteria);
        return ribbonRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RibbonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ribbon> specification = createSpecification(criteria);
        return ribbonRepository.count(specification);
    }

    /**
     * Function to convert {@link RibbonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ribbon> createSpecification(RibbonCriteria criteria) {
        Specification<Ribbon> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ribbon_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Ribbon_.name));
            }
            if (criteria.getPeopleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPeopleId(), root -> root.join(Ribbon_.people, JoinType.LEFT).get(Person_.id))
                    );
            }
        }
        return specification;
    }
}
