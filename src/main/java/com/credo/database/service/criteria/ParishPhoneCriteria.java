package com.credo.database.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.credo.database.domain.ParishPhone} entity. This class is used
 * in {@link com.credo.database.web.rest.ParishPhoneResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parish-phones?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ParishPhoneCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter phoneNumber;

    private StringFilter type;

    private LongFilter parishId;

    public ParishPhoneCriteria() {}

    public ParishPhoneCriteria(ParishPhoneCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.parishId = other.parishId == null ? null : other.parishId.copy();
    }

    @Override
    public ParishPhoneCriteria copy() {
        return new ParishPhoneCriteria(this);
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

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            phoneNumber = new StringFilter();
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getParishId() {
        return parishId;
    }

    public LongFilter parishId() {
        if (parishId == null) {
            parishId = new LongFilter();
        }
        return parishId;
    }

    public void setParishId(LongFilter parishId) {
        this.parishId = parishId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ParishPhoneCriteria that = (ParishPhoneCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(type, that.type) &&
            Objects.equals(parishId, that.parishId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phoneNumber, type, parishId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParishPhoneCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (parishId != null ? "parishId=" + parishId + ", " : "") +
            "}";
    }
}
