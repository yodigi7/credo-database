package com.credo.database.service;

import com.credo.database.domain.ParishPhone;
import com.credo.database.domain.ParishPhone_;
import com.credo.database.domain.Parish_;
import com.credo.database.repository.ParishPhoneRepository;
import com.credo.database.service.criteria.ParishPhoneCriteria;
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
 * Service for executing complex queries for {@link ParishPhone} entities in the database.
 * The main input is a {@link ParishPhoneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParishPhone} or a {@link Page} of {@link ParishPhone} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParishPhoneQueryService extends QueryService<ParishPhone> {

    private final Logger log = LoggerFactory.getLogger(ParishPhoneQueryService.class);

    private final ParishPhoneRepository parishPhoneRepository;

    public ParishPhoneQueryService(ParishPhoneRepository parishPhoneRepository) {
        this.parishPhoneRepository = parishPhoneRepository;
    }

    /**
     * Return a {@link List} of {@link ParishPhone} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParishPhone> findByCriteria(ParishPhoneCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ParishPhone> specification = createSpecification(criteria);
        return parishPhoneRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ParishPhone} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParishPhone> findByCriteria(ParishPhoneCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ParishPhone> specification = createSpecification(criteria);
        return parishPhoneRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParishPhoneCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ParishPhone> specification = createSpecification(criteria);
        return parishPhoneRepository.count(specification);
    }

    /**
     * Function to convert {@link ParishPhoneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ParishPhone> createSpecification(ParishPhoneCriteria criteria) {
        Specification<ParishPhone> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ParishPhone_.id));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), ParishPhone_.phoneNumber));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ParishPhone_.type));
            }
            if (criteria.getParishId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParishId(), root -> root.join(ParishPhone_.parish, JoinType.LEFT).get(Parish_.id))
                    );
            }
        }
        return specification;
    }
}
