package com.credo.database.service;

import com.credo.database.domain.*; // for static metamodels
import com.credo.database.domain.MembershipLevel;
import com.credo.database.repository.MembershipLevelRepository;
import com.credo.database.service.criteria.MembershipLevelCriteria;
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
 * Service for executing complex queries for {@link MembershipLevel} entities in the database.
 * The main input is a {@link MembershipLevelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MembershipLevel} or a {@link Page} of {@link MembershipLevel} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MembershipLevelQueryService extends QueryService<MembershipLevel> {

    private final Logger log = LoggerFactory.getLogger(MembershipLevelQueryService.class);

    private final MembershipLevelRepository membershipLevelRepository;

    public MembershipLevelQueryService(MembershipLevelRepository membershipLevelRepository) {
        this.membershipLevelRepository = membershipLevelRepository;
    }

    /**
     * Return a {@link List} of {@link MembershipLevel} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MembershipLevel> findByCriteria(MembershipLevelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MembershipLevel> specification = createSpecification(criteria);
        return membershipLevelRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link MembershipLevel} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MembershipLevel> findByCriteria(MembershipLevelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MembershipLevel> specification = createSpecification(criteria);
        return membershipLevelRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MembershipLevelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MembershipLevel> specification = createSpecification(criteria);
        return membershipLevelRepository.count(specification);
    }

    /**
     * Function to convert {@link MembershipLevelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MembershipLevel> createSpecification(MembershipLevelCriteria criteria) {
        Specification<MembershipLevel> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MembershipLevel_.id));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLevel(), MembershipLevel_.level));
            }
            if (criteria.getCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCost(), MembershipLevel_.cost));
            }
            if (criteria.getPeopleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPeopleId(),
                            root -> root.join(MembershipLevel_.people, JoinType.LEFT).get(Person_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
