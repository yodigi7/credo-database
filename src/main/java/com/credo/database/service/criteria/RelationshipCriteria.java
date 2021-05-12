package com.credo.database.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.credo.database.domain.Relationship} entity. This class is used
 * in {@link com.credo.database.web.rest.RelationshipResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /relationships?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RelationshipCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter relationship;

    private LongFilter peopleId;

    public RelationshipCriteria() {}

    public RelationshipCriteria(RelationshipCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.relationship = other.relationship == null ? null : other.relationship.copy();
        this.peopleId = other.peopleId == null ? null : other.peopleId.copy();
    }

    @Override
    public RelationshipCriteria copy() {
        return new RelationshipCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRelationship() {
        return relationship;
    }

    public StringFilter relationship() {
        if (relationship == null) {
            relationship = new StringFilter();
        }
        return relationship;
    }

    public void setRelationship(StringFilter relationship) {
        this.relationship = relationship;
    }

    public LongFilter getPeopleId() {
        return peopleId;
    }

    public LongFilter peopleId() {
        if (peopleId == null) {
            peopleId = new LongFilter();
        }
        return peopleId;
    }

    public void setPeopleId(LongFilter peopleId) {
        this.peopleId = peopleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RelationshipCriteria that = (RelationshipCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(relationship, that.relationship) && Objects.equals(peopleId, that.peopleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, relationship, peopleId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RelationshipCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (relationship != null ? "relationship=" + relationship + ", " : "") +
            (peopleId != null ? "peopleId=" + peopleId + ", " : "") +
            "}";
    }
}
