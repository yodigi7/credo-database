package com.credo.database.service;

import com.credo.database.domain.*; // for static metamodels
import com.credo.database.domain.EventPerk;
import com.credo.database.repository.EventPerkRepository;
import com.credo.database.service.criteria.EventPerkCriteria;
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
 * Service for executing complex queries for {@link EventPerk} entities in the database.
 * The main input is a {@link EventPerkCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventPerk} or a {@link Page} of {@link EventPerk} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventPerkQueryService extends QueryService<EventPerk> {

    private final Logger log = LoggerFactory.getLogger(EventPerkQueryService.class);

    private final EventPerkRepository eventPerkRepository;

    public EventPerkQueryService(EventPerkRepository eventPerkRepository) {
        this.eventPerkRepository = eventPerkRepository;
    }

    /**
     * Return a {@link List} of {@link EventPerk} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventPerk> findByCriteria(EventPerkCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventPerk> specification = createSpecification(criteria);
        return eventPerkRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EventPerk} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventPerk> findByCriteria(EventPerkCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventPerk> specification = createSpecification(criteria);
        return eventPerkRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventPerkCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventPerk> specification = createSpecification(criteria);
        return eventPerkRepository.count(specification);
    }

    /**
     * Function to convert {@link EventPerkCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventPerk> createSpecification(EventPerkCriteria criteria) {
        Specification<EventPerk> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventPerk_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EventPerk_.name));
            }
            if (criteria.getMinimumPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinimumPrice(), EventPerk_.minimumPrice));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(EventPerk_.event, JoinType.LEFT).get(Event_.id))
                    );
            }
            if (criteria.getPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPersonId(), root -> root.join(EventPerk_.person, JoinType.LEFT).get(Person_.id))
                    );
            }
        }
        return specification;
    }
}
