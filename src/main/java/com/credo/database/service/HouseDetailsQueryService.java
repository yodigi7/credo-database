package com.credo.database.service;

import com.credo.database.domain.*; // for static metamodels
import com.credo.database.domain.HouseDetails;
import com.credo.database.repository.HouseDetailsRepository;
import com.credo.database.service.criteria.HouseDetailsCriteria;
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
 * Service for executing complex queries for {@link HouseDetails} entities in the database.
 * The main input is a {@link HouseDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HouseDetails} or a {@link Page} of {@link HouseDetails} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HouseDetailsQueryService extends QueryService<HouseDetails> {

    private final Logger log = LoggerFactory.getLogger(HouseDetailsQueryService.class);

    private final HouseDetailsRepository houseDetailsRepository;

    public HouseDetailsQueryService(HouseDetailsRepository houseDetailsRepository) {
        this.houseDetailsRepository = houseDetailsRepository;
    }

    /**
     * Return a {@link List} of {@link HouseDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HouseDetails> findByCriteria(HouseDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HouseDetails> specification = createSpecification(criteria);
        return houseDetailsRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link HouseDetails} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HouseDetails> findByCriteria(HouseDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HouseDetails> specification = createSpecification(criteria);
        return houseDetailsRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HouseDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HouseDetails> specification = createSpecification(criteria);
        return houseDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link HouseDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HouseDetails> createSpecification(HouseDetailsCriteria criteria) {
        Specification<HouseDetails> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HouseDetails_.id));
            }
            if (criteria.getMailingLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMailingLabel(), HouseDetails_.mailingLabel));
            }
            if (criteria.getHeadOfHouseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getHeadOfHouseId(),
                            root -> root.join(HouseDetails_.headOfHouse, JoinType.LEFT).get(Person_.id)
                        )
                    );
            }
            if (criteria.getAddressesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAddressesId(),
                            root -> root.join(HouseDetails_.addresses, JoinType.LEFT).get(HouseAddress_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
