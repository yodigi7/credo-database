package com.credo.database.service;

import com.credo.database.domain.*;
import com.credo.database.repository.PersonRepository;
import com.credo.database.service.criteria.PersonCriteria;
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
 * Service for executing complex queries for {@link Person} entities in the database.
 * The main input is a {@link PersonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Person} or a {@link Page} of {@link Person} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonQueryService extends QueryService<Person> {

    private final Logger log = LoggerFactory.getLogger(PersonQueryService.class);

    private final PersonRepository personRepository;

    public PersonQueryService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Return a {@link List} of {@link Person} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Person> findByCriteria(PersonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Person} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Person> findByCriteria(PersonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Person> createSpecification(PersonCriteria criteria) {
        Specification<Person> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Person_.id));
            }
            if (criteria.getPrefix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPrefix(), Person_.prefix));
            }
            if (criteria.getPreferredName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPreferredName(), Person_.preferredName));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Person_.firstName));
            }
            if (criteria.getMiddleName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMiddleName(), Person_.middleName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Person_.lastName));
            }
            if (criteria.getSuffix() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSuffix(), Person_.suffix));
            }
            if (criteria.getNameTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameTag(), Person_.nameTag));
            }
            if (criteria.getCurrentMember() != null) {
                specification = specification.and(buildSpecification(criteria.getCurrentMember(), Person_.currentMember));
            }
            if (criteria.getMembershipStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMembershipStartDate(), Person_.membershipStartDate));
            }
            if (criteria.getMembershipExpirationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getMembershipExpirationDate(), Person_.membershipExpirationDate));
            }
            if (criteria.getIsHeadOfHouse() != null) {
                specification = specification.and(buildSpecification(criteria.getIsHeadOfHouse(), Person_.isHeadOfHouse));
            }
            if (criteria.getIsDeceased() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeceased(), Person_.isDeceased));
            }
            if (criteria.getSpouseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSpouseId(), root -> root.join(Person_.spouse, JoinType.LEFT).get(Person_.id))
                    );
            }
            if (criteria.getMembershipLevelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMembershipLevelId(),
                            root -> root.join(Person_.membershipLevel, JoinType.LEFT).get(MembershipLevel_.id)
                        )
                    );
            }
            if (criteria.getHeadOfHouseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHeadOfHouseId(),
                            root -> root.join(Person_.headOfHouse, JoinType.LEFT).get(Person_.id)
                        )
                    );
            }
            if (criteria.getParishId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParishId(), root -> root.join(Person_.parish, JoinType.LEFT).get(Parish_.id))
                    );
            }
            if (criteria.getOrganizationsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationsId(),
                            root -> root.join(Person_.organizations, JoinType.LEFT).get(Organization_.id)
                        )
                    );
            }
            if (criteria.getHouseDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHouseDetailsId(),
                            root -> root.join(Person_.houseDetails, JoinType.LEFT).get(HouseDetails_.id)
                        )
                    );
            }
            if (criteria.getNotesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getNotesId(), root -> root.join(Person_.notes, JoinType.LEFT).get(PersonNotes_.id))
                    );
            }
            if (criteria.getPhonesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPhonesId(), root -> root.join(Person_.phones, JoinType.LEFT).get(PersonPhone_.id))
                    );
            }
            if (criteria.getTransactionsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionsId(),
                            root -> root.join(Person_.transactions, JoinType.LEFT).get(Transaction_.id)
                        )
                    );
            }
            if (criteria.getEmailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmailsId(), root -> root.join(Person_.emails, JoinType.LEFT).get(PersonEmail_.id))
                    );
            }
            if (criteria.getPersonsInHouseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPersonsInHouseId(),
                            root -> root.join(Person_.personsInHouses, JoinType.LEFT).get(Person_.id)
                        )
                    );
            }
            if (criteria.getTicketsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTicketsId(), root -> root.join(Person_.tickets, JoinType.LEFT).get(Ticket_.id))
                    );
            }
        }
        return specification;
    }
}
