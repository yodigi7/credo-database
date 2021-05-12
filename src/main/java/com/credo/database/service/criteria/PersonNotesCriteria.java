package com.credo.database.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.credo.database.domain.PersonNotes} entity. This class is used
 * in {@link com.credo.database.web.rest.PersonNotesResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /person-notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PersonNotesCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter notes;

    private LongFilter personId;

    public PersonNotesCriteria() {}

    public PersonNotesCriteria(PersonNotesCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
    }

    @Override
    public PersonNotesCriteria copy() {
        return new PersonNotesCriteria(this);
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

    public LongFilter getPersonId() {
        return personId;
    }

    public LongFilter personId() {
        if (personId == null) {
            personId = new LongFilter();
        }
        return personId;
    }

    public void setPersonId(LongFilter personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PersonNotesCriteria that = (PersonNotesCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(notes, that.notes) && Objects.equals(personId, that.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, notes, personId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonNotesCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (personId != null ? "personId=" + personId + ", " : "") +
            "}";
    }
}
