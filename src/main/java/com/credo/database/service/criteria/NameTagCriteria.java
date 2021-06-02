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
 * Criteria class for the {@link com.credo.database.domain.NameTag} entity. This class is used
 * in {@link com.credo.database.web.rest.NameTagResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /name-tags?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NameTagCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nameTag;

    private LongFilter ticketId;

    public NameTagCriteria() {}

    public NameTagCriteria(NameTagCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nameTag = other.nameTag == null ? null : other.nameTag.copy();
        this.ticketId = other.ticketId == null ? null : other.ticketId.copy();
    }

    @Override
    public NameTagCriteria copy() {
        return new NameTagCriteria(this);
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

    public StringFilter getNameTag() {
        return nameTag;
    }

    public StringFilter nameTag() {
        if (nameTag == null) {
            nameTag = new StringFilter();
        }
        return nameTag;
    }

    public void setNameTag(StringFilter nameTag) {
        this.nameTag = nameTag;
    }

    public LongFilter getTicketId() {
        return ticketId;
    }

    public LongFilter ticketId() {
        if (ticketId == null) {
            ticketId = new LongFilter();
        }
        return ticketId;
    }

    public void setTicketId(LongFilter ticketId) {
        this.ticketId = ticketId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NameTagCriteria that = (NameTagCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(nameTag, that.nameTag) && Objects.equals(ticketId, that.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameTag, ticketId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NameTagCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nameTag != null ? "nameTag=" + nameTag + ", " : "") +
            (ticketId != null ? "ticketId=" + ticketId + ", " : "") +
            "}";
    }
}
