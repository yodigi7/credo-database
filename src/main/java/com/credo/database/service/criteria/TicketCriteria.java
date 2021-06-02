package com.credo.database.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.credo.database.domain.Ticket} entity. This class is used
 * in {@link com.credo.database.web.rest.TicketResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tickets?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TicketCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter count;

    private DoubleFilter costPerTicket;

    private BooleanFilter pickedUp;

    private LongFilter personId;

    private LongFilter eventId;

    private LongFilter transactionId;

    private LongFilter nameTagsId;

    public TicketCriteria() {}

    public TicketCriteria(TicketCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.count = other.count == null ? null : other.count.copy();
        this.costPerTicket = other.costPerTicket == null ? null : other.costPerTicket.copy();
        this.pickedUp = other.pickedUp == null ? null : other.pickedUp.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.transactionId = other.transactionId == null ? null : other.transactionId.copy();
        this.nameTagsId = other.nameTagsId == null ? null : other.nameTagsId.copy();
    }

    @Override
    public TicketCriteria copy() {
        return new TicketCriteria(this);
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

    public IntegerFilter getCount() {
        return count;
    }

    public IntegerFilter count() {
        if (count == null) {
            count = new IntegerFilter();
        }
        return count;
    }

    public void setCount(IntegerFilter count) {
        this.count = count;
    }

    public DoubleFilter getCostPerTicket() {
        return costPerTicket;
    }

    public DoubleFilter costPerTicket() {
        if (costPerTicket == null) {
            costPerTicket = new DoubleFilter();
        }
        return costPerTicket;
    }

    public void setCostPerTicket(DoubleFilter costPerTicket) {
        this.costPerTicket = costPerTicket;
    }

    public BooleanFilter getPickedUp() {
        return pickedUp;
    }

    public BooleanFilter pickedUp() {
        if (pickedUp == null) {
            pickedUp = new BooleanFilter();
        }
        return pickedUp;
    }

    public void setPickedUp(BooleanFilter pickedUp) {
        this.pickedUp = pickedUp;
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

    public LongFilter getTransactionId() {
        return transactionId;
    }

    public LongFilter transactionId() {
        if (transactionId == null) {
            transactionId = new LongFilter();
        }
        return transactionId;
    }

    public void setTransactionId(LongFilter transactionId) {
        this.transactionId = transactionId;
    }

    public LongFilter getNameTagsId() {
        return nameTagsId;
    }

    public LongFilter nameTagsId() {
        if (nameTagsId == null) {
            nameTagsId = new LongFilter();
        }
        return nameTagsId;
    }

    public void setNameTagsId(LongFilter nameTagsId) {
        this.nameTagsId = nameTagsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TicketCriteria that = (TicketCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(count, that.count) &&
            Objects.equals(costPerTicket, that.costPerTicket) &&
            Objects.equals(pickedUp, that.pickedUp) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(nameTagsId, that.nameTagsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, count, costPerTicket, pickedUp, personId, eventId, transactionId, nameTagsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TicketCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (count != null ? "count=" + count + ", " : "") +
            (costPerTicket != null ? "costPerTicket=" + costPerTicket + ", " : "") +
            (pickedUp != null ? "pickedUp=" + pickedUp + ", " : "") +
            (personId != null ? "personId=" + personId + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
            (nameTagsId != null ? "nameTagsId=" + nameTagsId + ", " : "") +
            "}";
    }
}
