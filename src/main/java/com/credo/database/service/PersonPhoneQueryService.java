package com.credo.database.service;

import com.credo.database.domain.PersonPhone;
import com.credo.database.domain.PersonPhone_;
import com.credo.database.domain.Person_;
import com.credo.database.repository.PersonPhoneRepository;
import com.credo.database.service.criteria.PersonPhoneCriteria;
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
 * Service for executing complex queries for {@link PersonPhone} entities in the database.
 * The main input is a {@link PersonPhoneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonPhone} or a {@link Page} of {@link PersonPhone} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonPhoneQueryService extends QueryService<PersonPhone> {

    private final Logger log = LoggerFactory.getLogger(PersonPhoneQueryService.class);

    private final PersonPhoneRepository personPhoneRepository;

    public PersonPhoneQueryService(PersonPhoneRepository personPhoneRepository) {
        this.personPhoneRepository = personPhoneRepository;
    }

    /**
     * Return a {@link List} of {@link PersonPhone} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonPhone> findByCriteria(PersonPhoneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PersonPhone> specification = createSpecification(criteria);
        return personPhoneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PersonPhone} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonPhone> findByCriteria(PersonPhoneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PersonPhone> specification = createSpecification(criteria);
        return personPhoneRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonPhoneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PersonPhone> specification = createSpecification(criteria);
        return personPhoneRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonPhoneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PersonPhone> createSpecification(PersonPhoneCriteria criteria) {
        Specification<PersonPhone> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PersonPhone_.id));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), PersonPhone_.phoneNumber));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), PersonPhone_.type));
            }
            if (criteria.getPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPersonId(), root -> root.join(PersonPhone_.person, JoinType.LEFT).get(Person_.id))
                    );
            }
        }
        return specification;
    }
}
