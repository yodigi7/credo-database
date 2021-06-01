package com.credo.database.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.credo.database.domain.Transaction} entity. This class is used
 * in {@link com.credo.database.web.rest.TransactionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transactions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TransactionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter totalAmount;

    private LocalDateFilter date;

    private StringFilter genericSubItemsPurchased;

    private DoubleFilter costSubItemsPurchased;

    private IntegerFilter numberOfMemberships;

    private DoubleFilter donation;

    private DoubleFilter eventDonation;

    private StringFilter notes;

    private LongFilter ticketsId;

    private LongFilter membershipLevelId;

    private LongFilter personId;

    private LongFilter eventId;

    public TransactionCriteria() {}

    public TransactionCriteria(TransactionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.totalAmount = other.totalAmount == null ? null : other.totalAmount.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.genericSubItemsPurchased = other.genericSubItemsPurchased == null ? null : other.genericSubItemsPurchased.copy();
        this.costSubItemsPurchased = other.costSubItemsPurchased == null ? null : other.costSubItemsPurchased.copy();
        this.numberOfMemberships = other.numberOfMemberships == null ? null : other.numberOfMemberships.copy();
        this.donation = other.donation == null ? null : other.donation.copy();
        this.eventDonation = other.eventDonation == null ? null : other.eventDonation.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.ticketsId = other.ticketsId == null ? null : other.ticketsId.copy();
        this.membershipLevelId = other.membershipLevelId == null ? null : other.membershipLevelId.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
    }

    @Override
    public TransactionCriteria copy() {
        return new TransactionCriteria(this);
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

    public DoubleFilter getTotalAmount() {
        return totalAmount;
    }

    public DoubleFilter totalAmount() {
        if (totalAmount == null) {
            totalAmount = new DoubleFilter();
        }
        return totalAmount;
    }

    public void setTotalAmount(DoubleFilter totalAmount) {
        this.totalAmount = totalAmount;
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

    public StringFilter getGenericSubItemsPurchased() {
        return genericSubItemsPurchased;
    }

    public StringFilter genericSubItemsPurchased() {
        if (genericSubItemsPurchased == null) {
            genericSubItemsPurchased = new StringFilter();
        }
        return genericSubItemsPurchased;
    }

    public void setGenericSubItemsPurchased(StringFilter genericSubItemsPurchased) {
        this.genericSubItemsPurchased = genericSubItemsPurchased;
    }

    public DoubleFilter getCostSubItemsPurchased() {
        return costSubItemsPurchased;
    }

    public DoubleFilter costSubItemsPurchased() {
        if (costSubItemsPurchased == null) {
            costSubItemsPurchased = new DoubleFilter();
        }
        return costSubItemsPurchased;
    }

    public void setCostSubItemsPurchased(DoubleFilter costSubItemsPurchased) {
        this.costSubItemsPurchased = costSubItemsPurchased;
    }

    public IntegerFilter getNumberOfMemberships() {
        return numberOfMemberships;
    }

    public IntegerFilter numberOfMemberships() {
        if (numberOfMemberships == null) {
            numberOfMemberships = new IntegerFilter();
        }
        return numberOfMemberships;
    }

    public void setNumberOfMemberships(IntegerFilter numberOfMemberships) {
        this.numberOfMemberships = numberOfMemberships;
    }

    public DoubleFilter getDonation() {
        return donation;
    }

    public DoubleFilter donation() {
        if (donation == null) {
            donation = new DoubleFilter();
        }
        return donation;
    }

    public void setDonation(DoubleFilter donation) {
        this.donation = donation;
    }

    public DoubleFilter getEventDonation() {
        return eventDonation;
    }

    public DoubleFilter eventDonation() {
        if (eventDonation == null) {
            eventDonation = new DoubleFilter();
        }
        return eventDonation;
    }

    public void setEventDonation(DoubleFilter eventDonation) {
        this.eventDonation = eventDonation;
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

    public LongFilter getMembershipLevelId() {
        return membershipLevelId;
    }

    public LongFilter membershipLevelId() {
        if (membershipLevelId == null) {
            membershipLevelId = new LongFilter();
        }
        return membershipLevelId;
    }

    public void setMembershipLevelId(LongFilter membershipLevelId) {
        this.membershipLevelId = membershipLevelId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TransactionCriteria that = (TransactionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(totalAmount, that.totalAmount) &&
            Objects.equals(date, that.date) &&
            Objects.equals(genericSubItemsPurchased, that.genericSubItemsPurchased) &&
            Objects.equals(costSubItemsPurchased, that.costSubItemsPurchased) &&
            Objects.equals(numberOfMemberships, that.numberOfMemberships) &&
            Objects.equals(donation, that.donation) &&
            Objects.equals(eventDonation, that.eventDonation) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(ticketsId, that.ticketsId) &&
            Objects.equals(membershipLevelId, that.membershipLevelId) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(eventId, that.eventId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            totalAmount,
            date,
            genericSubItemsPurchased,
            costSubItemsPurchased,
            numberOfMemberships,
            donation,
            eventDonation,
            notes,
            ticketsId,
            membershipLevelId,
            personId,
            eventId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (totalAmount != null ? "totalAmount=" + totalAmount + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (genericSubItemsPurchased != null ? "genericSubItemsPurchased=" + genericSubItemsPurchased + ", " : "") +
            (costSubItemsPurchased != null ? "costSubItemsPurchased=" + costSubItemsPurchased + ", " : "") +
            (numberOfMemberships != null ? "numberOfMemberships=" + numberOfMemberships + ", " : "") +
            (donation != null ? "donation=" + donation + ", " : "") +
            (eventDonation != null ? "eventDonation=" + eventDonation + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (ticketsId != null ? "ticketsId=" + ticketsId + ", " : "") +
            (membershipLevelId != null ? "membershipLevelId=" + membershipLevelId + ", " : "") +
            (personId != null ? "personId=" + personId + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            "}";
    }
}
