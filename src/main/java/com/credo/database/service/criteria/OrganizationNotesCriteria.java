package com.credo.database.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.credo.database.domain.OrganizationNotes} entity. This class is used
 * in {@link com.credo.database.web.rest.OrganizationNotesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /organization-notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrganizationNotesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter notes;

    private LongFilter organizationId;

    public OrganizationNotesCriteria() {}

    public OrganizationNotesCriteria(OrganizationNotesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.organizationId = other.organizationId == null ? null : other.organizationId.copy();
    }

    @Override
    public OrganizationNotesCriteria copy() {
        return new OrganizationNotesCriteria(this);
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

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public LongFilter getOrganizationId() {
        return organizationId;
    }

    public LongFilter organizationId() {
        if (organizationId == null) {
            organizationId = new LongFilter();
        }
        return organizationId;
    }

    public void setOrganizationId(LongFilter organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrganizationNotesCriteria that = (OrganizationNotesCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(notes, that.notes) && Objects.equals(organizationId, that.organizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, notes, organizationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationNotesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (organizationId != null ? "organizationId=" + organizationId + ", " : "") +
            "}";
    }
}
