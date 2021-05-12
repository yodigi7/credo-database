package com.credo.database.service.criteria;

import com.credo.database.domain.enumeration.YesNoEmpty;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.credo.database.domain.HouseAddress} entity. This class is used
 * in {@link com.credo.database.web.rest.HouseAddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /house-addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HouseAddressCriteria implements Serializable, Criteria {

    /**
     * Class for filtering YesNoEmpty
     */
    public static class YesNoEmptyFilter extends Filter<YesNoEmpty> {

        public YesNoEmptyFilter() {}

        public YesNoEmptyFilter(YesNoEmptyFilter filter) {
            super(filter);
        }

        @Override
        public YesNoEmptyFilter copy() {
            return new YesNoEmptyFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter streetAddress;

    private StringFilter city;

    private StringFilter state;

    private StringFilter zipcode;

    private StringFilter type;

    private YesNoEmptyFilter mailNewsletterSubscription;

    private YesNoEmptyFilter mailEventNotificationSubscription;

    private LongFilter houseDetailsId;

    public HouseAddressCriteria() {}

    public HouseAddressCriteria(HouseAddressCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.streetAddress = other.streetAddress == null ? null : other.streetAddress.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.zipcode = other.zipcode == null ? null : other.zipcode.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.mailNewsletterSubscription = other.mailNewsletterSubscription == null ? null : other.mailNewsletterSubscription.copy();
        this.mailEventNotificationSubscription =
            other.mailEventNotificationSubscription == null ? null : other.mailEventNotificationSubscription.copy();
        this.houseDetailsId = other.houseDetailsId == null ? null : other.houseDetailsId.copy();
    }

    @Override
    public HouseAddressCriteria copy() {
        return new HouseAddressCriteria(this);
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

    public YesNoEmptyFilter getMailNewsletterSubscription() {
        return mailNewsletterSubscription;
    }

    public YesNoEmptyFilter mailNewsletterSubscription() {
        if (mailNewsletterSubscription == null) {
            mailNewsletterSubscription = new YesNoEmptyFilter();
        }
        return mailNewsletterSubscription;
    }

    public void setMailNewsletterSubscription(YesNoEmptyFilter mailNewsletterSubscription) {
        this.mailNewsletterSubscription = mailNewsletterSubscription;
    }

    public YesNoEmptyFilter getMailEventNotificationSubscription() {
        return mailEventNotificationSubscription;
    }

    public YesNoEmptyFilter mailEventNotificationSubscription() {
        if (mailEventNotificationSubscription == null) {
            mailEventNotificationSubscription = new YesNoEmptyFilter();
        }
        return mailEventNotificationSubscription;
    }

    public void setMailEventNotificationSubscription(YesNoEmptyFilter mailEventNotificationSubscription) {
        this.mailEventNotificationSubscription = mailEventNotificationSubscription;
    }

    public LongFilter getHouseDetailsId() {
        return houseDetailsId;
    }

    public LongFilter houseDetailsId() {
        if (houseDetailsId == null) {
            houseDetailsId = new LongFilter();
        }
        return houseDetailsId;
    }

    public void setHouseDetailsId(LongFilter houseDetailsId) {
        this.houseDetailsId = houseDetailsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HouseAddressCriteria that = (HouseAddressCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(city, that.city) &&
            Objects.equals(state, that.state) &&
            Objects.equals(zipcode, that.zipcode) &&
            Objects.equals(type, that.type) &&
            Objects.equals(mailNewsletterSubscription, that.mailNewsletterSubscription) &&
            Objects.equals(mailEventNotificationSubscription, that.mailEventNotificationSubscription) &&
            Objects.equals(houseDetailsId, that.houseDetailsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            streetAddress,
            city,
            state,
            zipcode,
            type,
            mailNewsletterSubscription,
            mailEventNotificationSubscription,
            houseDetailsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HouseAddressCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (streetAddress != null ? "streetAddress=" + streetAddress + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (state != null ? "state=" + state + ", " : "") +
            (zipcode != null ? "zipcode=" + zipcode + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (mailNewsletterSubscription != null ? "mailNewsletterSubscription=" + mailNewsletterSubscription + ", " : "") +
            (mailEventNotificationSubscription != null ? "mailEventNotificationSubscription=" + mailEventNotificationSubscription + ", " : "") +
            (houseDetailsId != null ? "houseDetailsId=" + houseDetailsId + ", " : "") +
            "}";
    }
}
