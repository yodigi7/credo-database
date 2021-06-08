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
 * Criteria class for the {@link com.credo.database.domain.EventPerk} entity. This class is used
 * in {@link com.credo.database.web.rest.EventPerkResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /event-perks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EventPerkCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private DoubleFilter minimumPrice;

    private LongFilter eventId;

    private LongFilter personId;

    public EventPerkCriteria() {}

    public EventPerkCriteria(EventPerkCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.minimumPrice = other.minimumPrice == null ? null : other.minimumPrice.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
    }

    @Override
    public EventPerkCriteria copy() {
        return new EventPerkCriteria(this);
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

    public DoubleFilter getMinimumPrice() {
        return minimumPrice;
    }

    public DoubleFilter minimumPrice() {
        if (minimumPrice == null) {
            minimumPrice = new DoubleFilter();
        }
        return minimumPrice;
    }

    public void setMinimumPrice(DoubleFilter minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public LongFilter eventId() {
        if (eventId == null) {
            eventId = new LongFilter();
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
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
        final EventPerkCriteria that = (EventPerkCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(minimumPrice, that.minimumPrice) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(personId, that.personId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, minimumPrice, eventId, personId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventPerkCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (minimumPrice != null ? "minimumPrice=" + minimumPrice + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (personId != null ? "personId=" + personId + ", " : "") +
            "}";
    }
}
