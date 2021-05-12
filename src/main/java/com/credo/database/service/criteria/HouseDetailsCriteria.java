package com.credo.database.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.credo.database.domain.HouseDetails} entity. This class is used
 * in {@link com.credo.database.web.rest.HouseDetailsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /house-details?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HouseDetailsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter mailingLabel;

    private LongFilter headOfHouseId;

    private LongFilter addressesId;

    public HouseDetailsCriteria() {}

    public HouseDetailsCriteria(HouseDetailsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.mailingLabel = other.mailingLabel == null ? null : other.mailingLabel.copy();
        this.headOfHouseId = other.headOfHouseId == null ? null : other.headOfHouseId.copy();
        this.addressesId = other.addressesId == null ? null : other.addressesId.copy();
    }

    @Override
    public HouseDetailsCriteria copy() {
        return new HouseDetailsCriteria(this);
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

    public StringFilter getMailingLabel() {
        return mailingLabel;
    }

    public StringFilter mailingLabel() {
        if (mailingLabel == null) {
            mailingLabel = new StringFilter();
        }
        return mailingLabel;
    }

    public void setMailingLabel(StringFilter mailingLabel) {
        this.mailingLabel = mailingLabel;
    }

    public LongFilter getHeadOfHouseId() {
        return headOfHouseId;
    }

    public LongFilter headOfHouseId() {
        if (headOfHouseId == null) {
            headOfHouseId = new LongFilter();
        }
        return headOfHouseId;
    }

    public void setHeadOfHouseId(LongFilter headOfHouseId) {
        this.headOfHouseId = headOfHouseId;
    }

    public LongFilter getAddressesId() {
        return addressesId;
    }

    public LongFilter addressesId() {
        if (addressesId == null) {
            addressesId = new LongFilter();
        }
        return addressesId;
    }

    public void setAddressesId(LongFilter addressesId) {
        this.addressesId = addressesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HouseDetailsCriteria that = (HouseDetailsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(mailingLabel, that.mailingLabel) &&
            Objects.equals(headOfHouseId, that.headOfHouseId) &&
            Objects.equals(addressesId, that.addressesId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mailingLabel, headOfHouseId, addressesId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HouseDetailsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (mailingLabel != null ? "mailingLabel=" + mailingLabel + ", " : "") +
            (headOfHouseId != null ? "headOfHouseId=" + headOfHouseId + ", " : "") +
            (addressesId != null ? "addressesId=" + addressesId + ", " : "") +
            "}";
    }
}
