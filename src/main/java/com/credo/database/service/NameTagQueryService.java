package com.credo.database.service;

import com.credo.database.domain.*; // for static metamodels
import com.credo.database.domain.NameTag;
import com.credo.database.repository.NameTagRepository;
import com.credo.database.service.criteria.NameTagCriteria;
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
 * Service for executing complex queries for {@link NameTag} entities in the database.
 * The main input is a {@link NameTagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NameTag} or a {@link Page} of {@link NameTag} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NameTagQueryService extends QueryService<NameTag> {

    private final Logger log = LoggerFactory.getLogger(NameTagQueryService.class);

    private final NameTagRepository nameTagRepository;

    public NameTagQueryService(NameTagRepository nameTagRepository) {
        this.nameTagRepository = nameTagRepository;
    }

    /**
     * Return a {@link List} of {@link NameTag} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NameTag> findByCriteria(NameTagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NameTag> specification = createSpecification(criteria);
        return nameTagRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link NameTag} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NameTag> findByCriteria(NameTagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NameTag> specification = createSpecification(criteria);
        return nameTagRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NameTagCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NameTag> specification = createSpecification(criteria);
        return nameTagRepository.count(specification);
    }

    /**
     * Function to convert {@link NameTagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NameTag> createSpecification(NameTagCriteria criteria) {
        Specification<NameTag> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NameTag_.id));
            }
            if (criteria.getNameTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameTag(), NameTag_.nameTag));
            }
            if (criteria.getTicketId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTicketId(), root -> root.join(NameTag_.ticket, JoinType.LEFT).get(Ticket_.id))
                    );
            }
        }
        return specification;
    }
}
