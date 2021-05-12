package com.credo.database.service;

import com.credo.database.domain.Person_;
import com.credo.database.domain.Relationship;
import com.credo.database.domain.Relationship_;
import com.credo.database.repository.RelationshipRepository;
import com.credo.database.service.criteria.RelationshipCriteria;
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
 * Service for executing complex queries for {@link Relationship} entities in the database.
 * The main input is a {@link RelationshipCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Relationship} or a {@link Page} of {@link Relationship} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RelationshipQueryService extends QueryService<Relationship> {

    private final Logger log = LoggerFactory.getLogger(RelationshipQueryService.class);

    private final RelationshipRepository relationshipRepository;

    public RelationshipQueryService(RelationshipRepository relationshipRepository) {
        this.relationshipRepository = relationshipRepository;
    }

    /**
     * Return a {@link List} of {@link Relationship} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Relationship> findByCriteria(RelationshipCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Relationship> specification = createSpecification(criteria);
        return relationshipRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Relationship} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Relationship> findByCriteria(RelationshipCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Relationship> specification = createSpecification(criteria);
        return relationshipRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RelationshipCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Relationship> specification = createSpecification(criteria);
        return relationshipRepository.count(specification);
    }

    /**
     * Function to convert {@link RelationshipCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Relationship> createSpecification(RelationshipCriteria criteria) {
        Specification<Relationship> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Relationship_.id));
            }
            if (criteria.getRelationship() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRelationship(), Relationship_.relationship));
            }
            if (criteria.getPeopleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPeopleId(), root -> root.join(Relationship_.people, JoinType.LEFT).get(Person_.id))
                    );
            }
        }
        return specification;
    }
}
