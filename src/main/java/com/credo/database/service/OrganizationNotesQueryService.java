package com.credo.database.service;

import com.credo.database.domain.*; // for static metamodels
import com.credo.database.domain.OrganizationNotes;
import com.credo.database.repository.OrganizationNotesRepository;
import com.credo.database.service.criteria.OrganizationNotesCriteria;
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
 * Service for executing complex queries for {@link OrganizationNotes} entities in the database.
 * The main input is a {@link OrganizationNotesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganizationNotes} or a {@link Page} of {@link OrganizationNotes} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganizationNotesQueryService extends QueryService<OrganizationNotes> {

    private final Logger log = LoggerFactory.getLogger(OrganizationNotesQueryService.class);

    private final OrganizationNotesRepository organizationNotesRepository;

    public OrganizationNotesQueryService(OrganizationNotesRepository organizationNotesRepository) {
        this.organizationNotesRepository = organizationNotesRepository;
    }

    /**
     * Return a {@link List} of {@link OrganizationNotes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganizationNotes> findByCriteria(OrganizationNotesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OrganizationNotes> specification = createSpecification(criteria);
        return organizationNotesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OrganizationNotes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizationNotes> findByCriteria(OrganizationNotesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OrganizationNotes> specification = createSpecification(criteria);
        return organizationNotesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrganizationNotesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OrganizationNotes> specification = createSpecification(criteria);
        return organizationNotesRepository.count(specification);
    }

    /**
     * Function to convert {@link OrganizationNotesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OrganizationNotes> createSpecification(OrganizationNotesCriteria criteria) {
        Specification<OrganizationNotes> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OrganizationNotes_.id));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), OrganizationNotes_.notes));
            }
            if (criteria.getOrganizationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOrganizationId(),
                            root -> root.join(OrganizationNotes_.organization, JoinType.LEFT).get(Organization_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
