package com.credo.database.service.criteria;

import com.credo.database.domain.enumeration.YesNoEmpty;
import com.credo.database.domain.enumeration.YesNoEmpty;
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
 * Criteria class for the {@link com.credo.database.domain.PersonEmail} entity. This class is used
 * in {@link com.credo.database.web.rest.PersonEmailResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /person-emails?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PersonEmailCriteria implements Serializable, Criteria {

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

    private StringFilter email;

    private StringFilter type;

    private YesNoEmptyFilter emailNewsletterSubscription;

    private YesNoEmptyFilter emailEventNotificationSubscription;

    private LongFilter personId;

    public PersonEmailCriteria() {}

    public PersonEmailCriteria(PersonEmailCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.emailNewsletterSubscription = other.emailNewsletterSubscription == null ? null : other.emailNewsletterSubscription.copy();
        this.emailEventNotificationSubscription =
            other.emailEventNotificationSubscription == null ? null : other.emailEventNotificationSubscription.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
    }

    @Override
    public PersonEmailCriteria copy() {
        return new PersonEmailCriteria(this);
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

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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

    public YesNoEmptyFilter getEmailNewsletterSubscription() {
        return emailNewsletterSubscription;
    }

    public YesNoEmptyFilter emailNewsletterSubscription() {
        if (emailNewsletterSubscription == null) {
            emailNewsletterSubscription = new YesNoEmptyFilter();
        }
        return emailNewsletterSubscription;
    }

    public void setEmailNewsletterSubscription(YesNoEmptyFilter emailNewsletterSubscription) {
        this.emailNewsletterSubscription = emailNewsletterSubscription;
    }

    public YesNoEmptyFilter getEmailEventNotificationSubscription() {
        return emailEventNotificationSubscription;
    }

    public YesNoEmptyFilter emailEventNotificationSubscription() {
        if (emailEventNotificationSubscription == null) {
            emailEventNotificationSubscription = new YesNoEmptyFilter();
        }
        return emailEventNotificationSubscription;
    }

    public void setEmailEventNotificationSubscription(YesNoEmptyFilter emailEventNotificationSubscription) {
        this.emailEventNotificationSubscription = emailEventNotificationSubscription;
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
        final PersonEmailCriteria that = (PersonEmailCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(email, that.email) &&
            Objects.equals(type, that.type) &&
            Objects.equals(emailNewsletterSubscription, that.emailNewsletterSubscription) &&
            Objects.equals(emailEventNotificationSubscription, that.emailEventNotificationSubscription) &&
            Objects.equals(personId, that.personId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, type, emailNewsletterSubscription, emailEventNotificationSubscription, personId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonEmailCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (emailNewsletterSubscription != null ? "emailNewsletterSubscription=" + emailNewsletterSubscription + ", " : "") +
            (emailEventNotificationSubscription != null ? "emailEventNotificationSubscription=" + emailEventNotificationSubscription + ", " : "") +
            (personId != null ? "personId=" + personId + ", " : "") +
            "}";
    }
}
