package com.credo.database.service;

import com.credo.database.domain.Event_;
import com.credo.database.domain.Payment_;
import com.credo.database.domain.Person_;
import com.credo.database.domain.Ticket;
import com.credo.database.domain.Ticket_;
import com.credo.database.repository.TicketRepository;
import com.credo.database.service.criteria.TicketCriteria;
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
 * Service for executing complex queries for {@link Ticket} entities in the database.
 * The main input is a {@link TicketCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ticket} or a {@link Page} of {@link Ticket} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TicketQueryService extends QueryService<Ticket> {

    private final Logger log = LoggerFactory.getLogger(TicketQueryService.class);

    private final TicketRepository ticketRepository;

    public TicketQueryService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Return a {@link List} of {@link Ticket} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ticket> findByCriteria(TicketCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ticket> specification = createSpecification(criteria);
        return ticketRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ticket} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ticket> findByCriteria(TicketCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ticket> specification = createSpecification(criteria);
        return ticketRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TicketCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ticket> specification = createSpecification(criteria);
        return ticketRepository.count(specification);
    }

    /**
     * Function to convert {@link TicketCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ticket> createSpecification(TicketCriteria criteria) {
        Specification<Ticket> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ticket_.id));
            }
            if (criteria.getCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCount(), Ticket_.count));
            }
            if (criteria.getPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPersonId(), root -> root.join(Ticket_.person, JoinType.LEFT).get(Person_.id))
                    );
            }
            if (criteria.getPaymentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPaymentId(), root -> root.join(Ticket_.payments, JoinType.LEFT).get(Payment_.id))
                    );
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(Ticket_.events, JoinType.LEFT).get(Event_.id))
                    );
            }
        }
        return specification;
    }
}
