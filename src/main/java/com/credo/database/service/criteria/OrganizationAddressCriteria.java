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
 * Criteria class for the {@link com.credo.database.domain.OrganizationAddress} entity. This class is used
 * in {@link com.credo.database.web.rest.OrganizationAddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /organization-addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrganizationAddressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter streetAddress;

    private StringFilter city;

    private StringFilter state;

    private StringFilter zipcode;

    private LongFilter organizationId;

    public OrganizationAddressCriteria() {}

    public OrganizationAddressCriteria(OrganizationAddressCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.streetAddress = other.streetAddress == null ? null : other.streetAddress.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.zipcode = other.zipcode == null ? null : other.zipcode.copy();
        this.organizationId = other.organizationId == null ? null : other.organizationId.copy();
    }

    @Override
    public OrganizationAddressCriteria copy() {
        return new OrganizationAddressCriteria(this);
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

    public StringFilter getStreetAddress() {
        return streetAddress;
    }

    public StringFilter streetAddress() {
        if (streetAddress == null) {
            streetAddress = new StringFilter();
        }
        return streetAddress;
    }

    public void setStreetAddress(StringFilter streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getState() {
        return state;
    }

    public StringFilter state() {
        if (state == null) {
            state = new StringFilter();
        }
        return state;
    }

    public void setState(StringFilter state) {
        this.state = state;
    }

    public StringFilter getZipcode() {
        return zipcode;
    }

    public StringFilter zipcode() {
        if (zipcode == null) {
            zipcode = new StringFilter();
        }
        return zipcode;
    }

    public void setZipcode(StringFilter zipcode) {
        this.zipcode = zipcode;
    }

    public LongFilter getOrganizationId() {
        return organizationId;
    }

    public LongFilter organizationId() {
        if (organizationId == null) {
            organizationId = new LongFilter();
        }
        return organizationId;
    }

    public void setOrganizationId(LongFilter organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrganizationAddressCriteria that = (OrganizationAddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(city, that.city) &&
            Objects.equals(state, that.state) &&
            Objects.equals(zipcode, that.zipcode) &&
            Objects.equals(organizationId, that.organizationId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, streetAddress, city, state, zipcode, organizationId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationAddressCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (zipcode != null ? "zipcode=" + zipcode + ", " : "") +
            (organizationId != null ? "organizationId=" + organizationId + ", " : "") +
            "}";
    }
}
