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
 * Criteria class for the {@link com.credo.database.domain.Ribbon} entity. This class is used
 * in {@link com.credo.database.web.rest.RibbonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ribbons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RibbonCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LongFilter peopleId;

    public RibbonCriteria() {}

    public RibbonCriteria(RibbonCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.peopleId = other.peopleId == null ? null : other.peopleId.copy();
    }

    @Override
    public RibbonCriteria copy() {
        return new RibbonCriteria(this);
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
        final RibbonCriteria that = (RibbonCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(peopleId, that.peopleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, peopleId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RibbonCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (peopleId != null ? "peopleId=" + peopleId + ", " : "") +
            "}";
    }
}
