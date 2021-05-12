package com.credo.database.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.credo.database.domain.Organization} entity. This class is used
 * in {@link com.credo.database.web.rest.OrganizationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /organizations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrganizationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter mailingLabel;

    private LongFilter parishId;

    private LongFilter notesId;

    private LongFilter addressesId;

    private LongFilter phonesId;

    private LongFilter emailsId;

    private LongFilter personsId;

    public OrganizationCriteria() {}

    public OrganizationCriteria(OrganizationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.mailingLabel = other.mailingLabel == null ? null : other.mailingLabel.copy();
        this.parishId = other.parishId == null ? null : other.parishId.copy();
        this.notesId = other.notesId == null ? null : other.notesId.copy();
        this.addressesId = other.addressesId == null ? null : other.addressesId.copy();
        this.phonesId = other.phonesId == null ? null : other.phonesId.copy();
        this.emailsId = other.emailsId == null ? null : other.emailsId.copy();
        this.personsId = other.personsId == null ? null : other.personsId.copy();
    }

    @Override
    public OrganizationCriteria copy() {
        return new OrganizationCriteria(this);
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

    public LongFilter getParishId() {
        return parishId;
    }

    public LongFilter parishId() {
        if (parishId == null) {
            parishId = new LongFilter();
        }
        return parishId;
    }

    public void setParishId(LongFilter parishId) {
        this.parishId = parishId;
    }

    public LongFilter getNotesId() {
        return notesId;
    }

    public LongFilter notesId() {
        if (notesId == null) {
            notesId = new LongFilter();
        }
        return notesId;
    }

    public void setNotesId(LongFilter notesId) {
        this.notesId = notesId;
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

    public LongFilter getPhonesId() {
        return phonesId;
    }

    public LongFilter phonesId() {
        if (phonesId == null) {
            phonesId = new LongFilter();
        }
        return phonesId;
    }

    public void setPhonesId(LongFilter phonesId) {
        this.phonesId = phonesId;
    }

    public LongFilter getEmailsId() {
        return emailsId;
    }

    public LongFilter emailsId() {
        if (emailsId == null) {
            emailsId = new LongFilter();
        }
        return emailsId;
    }

    public void setEmailsId(LongFilter emailsId) {
        this.emailsId = emailsId;
    }

    public LongFilter getPersonsId() {
        return personsId;
    }

    public LongFilter personsId() {
        if (personsId == null) {
            personsId = new LongFilter();
        }
        return personsId;
    }

    public void setPersonsId(LongFilter personsId) {
        this.personsId = personsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrganizationCriteria that = (OrganizationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(mailingLabel, that.mailingLabel) &&
            Objects.equals(parishId, that.parishId) &&
            Objects.equals(notesId, that.notesId) &&
            Objects.equals(addressesId, that.addressesId) &&
            Objects.equals(phonesId, that.phonesId) &&
            Objects.equals(emailsId, that.emailsId) &&
            Objects.equals(personsId, that.personsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mailingLabel, parishId, notesId, addressesId, phonesId, emailsId, personsId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (mailingLabel != null ? "mailingLabel=" + mailingLabel + ", " : "") +
            (parishId != null ? "parishId=" + parishId + ", " : "") +
            (notesId != null ? "notesId=" + notesId + ", " : "") +
            (addressesId != null ? "addressesId=" + addressesId + ", " : "") +
            (phonesId != null ? "phonesId=" + phonesId + ", " : "") +
            (emailsId != null ? "emailsId=" + emailsId + ", " : "") +
            (personsId != null ? "personsId=" + personsId + ", " : "") +
            "}";
    }
}
