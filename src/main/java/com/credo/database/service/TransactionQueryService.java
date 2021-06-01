package com.credo.database.service;

import com.credo.database.domain.Event_;
import com.credo.database.domain.MembershipLevel_;
import com.credo.database.domain.Person_;
import com.credo.database.domain.Ticket_;
import com.credo.database.domain.Transaction;
import com.credo.database.domain.Transaction_;
import com.credo.database.repository.TransactionRepository;
import com.credo.database.service.criteria.TransactionCriteria;
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
 * Service for executing complex queries for {@link Transaction} entities in the database.
 * The main input is a {@link TransactionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Transaction} or a {@link Page} of {@link Transaction} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionQueryService extends QueryService<Transaction> {

    private final Logger log = LoggerFactory.getLogger(TransactionQueryService.class);

    private final TransactionRepository transactionRepository;

    public TransactionQueryService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Return a {@link List} of {@link Transaction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Transaction> findByCriteria(TransactionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Transaction> specification = createSpecification(criteria);
        return transactionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Transaction} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Transaction> findByCriteria(TransactionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transaction> specification = createSpecification(criteria);
        return transactionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Transaction> specification = createSpecification(criteria);
        return transactionRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Transaction> createSpecification(TransactionCriteria criteria) {
        Specification<Transaction> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Transaction_.id));
            }
            if (criteria.getTotalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalAmount(), Transaction_.totalAmount));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Transaction_.date));
            }
            if (criteria.getGenericSubItemsPurchased() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getGenericSubItemsPurchased(), Transaction_.genericSubItemsPurchased)
                    );
            }
            if (criteria.getCostSubItemsPurchased() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCostSubItemsPurchased(), Transaction_.costSubItemsPurchased));
            }
            if (criteria.getNumberOfMemberships() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getNumberOfMemberships(), Transaction_.numberOfMemberships));
            }
            if (criteria.getDonation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDonation(), Transaction_.donation));
            }
            if (criteria.getEventDonation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventDonation(), Transaction_.eventDonation));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Transaction_.notes));
            }
            if (criteria.getTicketsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTicketsId(), root -> root.join(Transaction_.tickets, JoinType.LEFT).get(Ticket_.id))
                    );
            }
            if (criteria.getMembershipLevelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMembershipLevelId(),
                            root -> root.join(Transaction_.membershipLevel, JoinType.LEFT).get(MembershipLevel_.id)
                        )
                    );
            }
            if (criteria.getPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPersonId(), root -> root.join(Transaction_.person, JoinType.LEFT).get(Person_.id))
                    );
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(Transaction_.event, JoinType.LEFT).get(Event_.id))
                    );
            }
        }
        return specification;
    }
}
