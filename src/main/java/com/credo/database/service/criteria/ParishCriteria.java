package com.credo.database.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.credo.database.domain.Parish} entity. This class is used
 * in {@link com.credo.database.web.rest.ParishResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parishes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ParishCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter organizationsId;

    private LongFilter phonesId;

    private LongFilter peopleId;

    private LongFilter emailsId;

    public ParishCriteria() {}

    public ParishCriteria(ParishCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.organizationsId = other.organizationsId == null ? null : other.organizationsId.copy();
        this.phonesId = other.phonesId == null ? null : other.phonesId.copy();
        this.peopleId = other.peopleId == null ? null : other.peopleId.copy();
        this.emailsId = other.emailsId == null ? null : other.emailsId.copy();
    }

    @Override
    public ParishCriteria copy() {
        return new ParishCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public LongFilter getOrganizationsId() {
        return organizationsId;
    }

    public LongFilter organizationsId() {
        if (organizationsId == null) {
            organizationsId = new LongFilter();
        }
        return organizationsId;
    }

    public void setOrganizationsId(LongFilter organizationsId) {
        this.organizationsId = organizationsId;
    }

    public LongFilter getPhonesId() {
        return phonesId;
    }

    public LongFilter phonesId() {
        if (phonesId == null) {
            phonesId = new LongFilter();
        }
        return phonesId;
    }

    public void setPhonesId(LongFilter phonesId) {
        this.phonesId = phonesId;
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

    public LongFilter getEmailsId() {
        return emailsId;
    }

    public LongFilter emailsId() {
        if (emailsId == null) {
            emailsId = new LongFilter();
        }
        return emailsId;
    }

    public void setEmailsId(LongFilter emailsId) {
        this.emailsId = emailsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ParishCriteria that = (ParishCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(organizationsId, that.organizationsId) &&
            Objects.equals(phonesId, that.phonesId) &&
            Objects.equals(peopleId, that.peopleId) &&
            Objects.equals(emailsId, that.emailsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, organizationsId, phonesId, peopleId, emailsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParishCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (organizationsId != null ? "organizationsId=" + organizationsId + ", " : "") +
            (phonesId != null ? "phonesId=" + phonesId + ", " : "") +
            (peopleId != null ? "peopleId=" + peopleId + ", " : "") +
            (emailsId != null ? "emailsId=" + emailsId + ", " : "") +
            "}";
    }
}
