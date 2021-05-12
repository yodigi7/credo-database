package com.credo.database.service;

import com.credo.database.domain.PersonEmail;
import com.credo.database.domain.PersonEmail_;
import com.credo.database.domain.Person_;
import com.credo.database.repository.PersonEmailRepository;
import com.credo.database.service.criteria.PersonEmailCriteria;
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
 * Service for executing complex queries for {@link PersonEmail} entities in the database.
 * The main input is a {@link PersonEmailCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonEmail} or a {@link Page} of {@link PersonEmail} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonEmailQueryService extends QueryService<PersonEmail> {

    private final Logger log = LoggerFactory.getLogger(PersonEmailQueryService.class);

    private final PersonEmailRepository personEmailRepository;

    public PersonEmailQueryService(PersonEmailRepository personEmailRepository) {
        this.personEmailRepository = personEmailRepository;
    }

    /**
     * Return a {@link List} of {@link PersonEmail} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonEmail> findByCriteria(PersonEmailCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PersonEmail> specification = createSpecification(criteria);
        return personEmailRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PersonEmail} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonEmail> findByCriteria(PersonEmailCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PersonEmail> specification = createSpecification(criteria);
        return personEmailRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonEmailCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PersonEmail> specification = createSpecification(criteria);
        return personEmailRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonEmailCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PersonEmail> createSpecification(PersonEmailCriteria criteria) {
        Specification<PersonEmail> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PersonEmail_.id));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), PersonEmail_.email));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), PersonEmail_.type));
            }
            if (criteria.getEmailNewsletterSubscription() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEmailNewsletterSubscription(), PersonEmail_.emailNewsletterSubscription)
                    );
            }
            if (criteria.getEmailEventNotificationSubscription() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmailEventNotificationSubscription(),
                            PersonEmail_.emailEventNotificationSubscription
                        )
                    );
            }
            if (criteria.getPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPersonId(), root -> root.join(PersonEmail_.person, JoinType.LEFT).get(Person_.id))
                    );
            }
        }
        return specification;
    }
}
