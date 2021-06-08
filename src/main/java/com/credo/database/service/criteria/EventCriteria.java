package com.credo.database.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.credo.database.domain.Event} entity. This class is used
 * in {@link com.credo.database.web.rest.EventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private LocalDateFilter date;

    private LongFilter transactionsId;

    private LongFilter ticketsId;

    private LongFilter perksId;

    public EventCriteria() {}

    public EventCriteria(EventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.transactionsId = other.transactionsId == null ? null : other.transactionsId.copy();
        this.ticketsId = other.ticketsId == null ? null : other.ticketsId.copy();
        this.perksId = other.perksId == null ? null : other.perksId.copy();
    }

    @Override
    public EventCriteria copy() {
        return new EventCriteria(this);
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

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public LongFilter getTransactionsId() {
        return transactionsId;
    }

    public LongFilter transactionsId() {
        if (transactionsId == null) {
            transactionsId = new LongFilter();
        }
        return transactionsId;
    }

    public void setTransactionsId(LongFilter transactionsId) {
        this.transactionsId = transactionsId;
    }

    public LongFilter getTicketsId() {
        return ticketsId;
    }

    public LongFilter ticketsId() {
        if (ticketsId == null) {
            ticketsId = new LongFilter();
        }
        return ticketsId;
    }

    public void setTicketsId(LongFilter ticketsId) {
        this.ticketsId = ticketsId;
    }

    public LongFilter getPerksId() {
        return perksId;
    }

    public LongFilter perksId() {
        if (perksId == null) {
            perksId = new LongFilter();
        }
        return perksId;
    }

    public void setPerksId(LongFilter perksId) {
        this.perksId = perksId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventCriteria that = (EventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(date, that.date) &&
            Objects.equals(transactionsId, that.transactionsId) &&
            Objects.equals(ticketsId, that.ticketsId) &&
            Objects.equals(perksId, that.perksId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date, transactionsId, ticketsId, perksId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (transactionsId != null ? "transactionsId=" + transactionsId + ", " : "") +
            (ticketsId != null ? "ticketsId=" + ticketsId + ", " : "") +
            (perksId != null ? "perksId=" + perksId + ", " : "") +
            "}";
    }
}
