package com.credo.database.service;

import com.credo.database.domain.*; // for static metamodels
import com.credo.database.domain.PersonNotes;
import com.credo.database.repository.PersonNotesRepository;
import com.credo.database.service.criteria.PersonNotesCriteria;
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
 * Service for executing complex queries for {@link PersonNotes} entities in the database.
 * The main input is a {@link PersonNotesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonNotes} or a {@link Page} of {@link PersonNotes} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonNotesQueryService extends QueryService<PersonNotes> {

    private final Logger log = LoggerFactory.getLogger(PersonNotesQueryService.class);

    private final PersonNotesRepository personNotesRepository;

    public PersonNotesQueryService(PersonNotesRepository personNotesRepository) {
        this.personNotesRepository = personNotesRepository;
    }

    /**
     * Return a {@link List} of {@link PersonNotes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonNotes> findByCriteria(PersonNotesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PersonNotes> specification = createSpecification(criteria);
        return personNotesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PersonNotes} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonNotes> findByCriteria(PersonNotesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PersonNotes> specification = createSpecification(criteria);
        return personNotesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonNotesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PersonNotes> specification = createSpecification(criteria);
        return personNotesRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonNotesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PersonNotes> createSpecification(PersonNotesCriteria criteria) {
        Specification<PersonNotes> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PersonNotes_.id));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), PersonNotes_.notes));
            }
            if (criteria.getPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPersonId(), root -> root.join(PersonNotes_.person, JoinType.LEFT).get(Person_.id))
                    );
            }
        }
        return specification;
    }
}
