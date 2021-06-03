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
 * Criteria class for the {@link com.credo.database.domain.MembershipLevel} entity. This class is used
 * in {@link com.credo.database.web.rest.MembershipLevelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /membership-levels?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MembershipLevelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter level;

    private DoubleFilter cost;

    private LongFilter peopleId;

    public MembershipLevelCriteria() {}

    public MembershipLevelCriteria(MembershipLevelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.level = other.level == null ? null : other.level.copy();
        this.cost = other.cost == null ? null : other.cost.copy();
        this.peopleId = other.peopleId == null ? null : other.peopleId.copy();
    }

    @Override
    public MembershipLevelCriteria copy() {
        return new MembershipLevelCriteria(this);
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

    public StringFilter getLevel() {
        return level;
    }

    public StringFilter level() {
        if (level == null) {
            level = new StringFilter();
        }
        return level;
    }

    public void setLevel(StringFilter level) {
        this.level = level;
    }

    public DoubleFilter getCost() {
        return cost;
    }

    public DoubleFilter cost() {
        if (cost == null) {
            cost = new DoubleFilter();
        }
        return cost;
    }

    public void setCost(DoubleFilter cost) {
        this.cost = cost;
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
        final MembershipLevelCriteria that = (MembershipLevelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(level, that.level) &&
            Objects.equals(cost, that.cost) &&
            Objects.equals(peopleId, that.peopleId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, level, cost, peopleId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipLevelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (level != null ? "level=" + level + ", " : "") +
            (cost != null ? "cost=" + cost + ", " : "") +
            (peopleId != null ? "peopleId=" + peopleId + ", " : "") +
            "}";
    }
}
