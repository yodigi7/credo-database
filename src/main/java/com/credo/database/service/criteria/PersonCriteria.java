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
 * Criteria class for the {@link com.credo.database.domain.Person} entity. This class is used
 * in {@link com.credo.database.web.rest.PersonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /people?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PersonCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter prefix;

    private StringFilter preferredName;

    private StringFilter firstName;

    private StringFilter middleName;

    private StringFilter lastName;

    private StringFilter suffix;

    private StringFilter nameTag;

    private BooleanFilter currentMember;

    private LocalDateFilter membershipStartDate;

    private LocalDateFilter membershipExpirationDate;

    private BooleanFilter isHeadOfHouse;

    private BooleanFilter isDeceased;

    private LongFilter spouseId;

    private LongFilter membershipLevelId;

    private LongFilter headOfHouseId;

    private LongFilter parishId;

    private LongFilter organizationsId;

    private LongFilter houseDetailsId;

    private LongFilter notesId;

    private LongFilter phonesId;

    private LongFilter transactionsId;

    private LongFilter emailsId;

    private LongFilter personsInHouseId;

    private LongFilter ticketsId;

    public PersonCriteria() {}

    public PersonCriteria(PersonCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.prefix = other.prefix == null ? null : other.prefix.copy();
        this.preferredName = other.preferredName == null ? null : other.preferredName.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.middleName = other.middleName == null ? null : other.middleName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.suffix = other.suffix == null ? null : other.suffix.copy();
        this.nameTag = other.nameTag == null ? null : other.nameTag.copy();
        this.currentMember = other.currentMember == null ? null : other.currentMember.copy();
        this.membershipStartDate = other.membershipStartDate == null ? null : other.membershipStartDate.copy();
        this.membershipExpirationDate = other.membershipExpirationDate == null ? null : other.membershipExpirationDate.copy();
        this.isHeadOfHouse = other.isHeadOfHouse == null ? null : other.isHeadOfHouse.copy();
        this.isDeceased = other.isDeceased == null ? null : other.isDeceased.copy();
        this.spouseId = other.spouseId == null ? null : other.spouseId.copy();
        this.membershipLevelId = other.membershipLevelId == null ? null : other.membershipLevelId.copy();
        this.headOfHouseId = other.headOfHouseId == null ? null : other.headOfHouseId.copy();
        this.parishId = other.parishId == null ? null : other.parishId.copy();
        this.organizationsId = other.organizationsId == null ? null : other.organizationsId.copy();
        this.houseDetailsId = other.houseDetailsId == null ? null : other.houseDetailsId.copy();
        this.notesId = other.notesId == null ? null : other.notesId.copy();
        this.phonesId = other.phonesId == null ? null : other.phonesId.copy();
        this.transactionsId = other.transactionsId == null ? null : other.transactionsId.copy();
        this.emailsId = other.emailsId == null ? null : other.emailsId.copy();
        this.personsInHouseId = other.personsInHouseId == null ? null : other.personsInHouseId.copy();
        this.ticketsId = other.ticketsId == null ? null : other.ticketsId.copy();
    }

    @Override
    public PersonCriteria copy() {
        return new PersonCriteria(this);
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

    public StringFilter getPrefix() {
        return prefix;
    }

    public StringFilter prefix() {
        if (prefix == null) {
            prefix = new StringFilter();
        }
        return prefix;
    }

    public void setPrefix(StringFilter prefix) {
        this.prefix = prefix;
    }

    public StringFilter getPreferredName() {
        return preferredName;
    }

    public StringFilter preferredName() {
        if (preferredName == null) {
            preferredName = new StringFilter();
        }
        return preferredName;
    }

    public void setPreferredName(StringFilter preferredName) {
        this.preferredName = preferredName;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getMiddleName() {
        return middleName;
    }

    public StringFilter middleName() {
        if (middleName == null) {
            middleName = new StringFilter();
        }
        return middleName;
    }

    public void setMiddleName(StringFilter middleName) {
        this.middleName = middleName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getSuffix() {
        return suffix;
    }

    public StringFilter suffix() {
        if (suffix == null) {
            suffix = new StringFilter();
        }
        return suffix;
    }

    public void setSuffix(StringFilter suffix) {
        this.suffix = suffix;
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

    public BooleanFilter getCurrentMember() {
        return currentMember;
    }

    public BooleanFilter currentMember() {
        if (currentMember == null) {
            currentMember = new BooleanFilter();
        }
        return currentMember;
    }

    public void setCurrentMember(BooleanFilter currentMember) {
        this.currentMember = currentMember;
    }

    public LocalDateFilter getMembershipStartDate() {
        return membershipStartDate;
    }

    public LocalDateFilter membershipStartDate() {
        if (membershipStartDate == null) {
            membershipStartDate = new LocalDateFilter();
        }
        return membershipStartDate;
    }

    public void setMembershipStartDate(LocalDateFilter membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }

    public LocalDateFilter getMembershipExpirationDate() {
        return membershipExpirationDate;
    }

    public LocalDateFilter membershipExpirationDate() {
        if (membershipExpirationDate == null) {
            membershipExpirationDate = new LocalDateFilter();
        }
        return membershipExpirationDate;
    }

    public void setMembershipExpirationDate(LocalDateFilter membershipExpirationDate) {
        this.membershipExpirationDate = membershipExpirationDate;
    }

    public BooleanFilter getIsHeadOfHouse() {
        return isHeadOfHouse;
    }

    public BooleanFilter isHeadOfHouse() {
        if (isHeadOfHouse == null) {
            isHeadOfHouse = new BooleanFilter();
        }
        return isHeadOfHouse;
    }

    public void setIsHeadOfHouse(BooleanFilter isHeadOfHouse) {
        this.isHeadOfHouse = isHeadOfHouse;
    }

    public BooleanFilter getIsDeceased() {
        return isDeceased;
    }

    public BooleanFilter isDeceased() {
        if (isDeceased == null) {
            isDeceased = new BooleanFilter();
        }
        return isDeceased;
    }

    public void setIsDeceased(BooleanFilter isDeceased) {
        this.isDeceased = isDeceased;
    }

    public LongFilter getSpouseId() {
        return spouseId;
    }

    public LongFilter spouseId() {
        if (spouseId == null) {
            spouseId = new LongFilter();
        }
        return spouseId;
    }

    public void setSpouseId(LongFilter spouseId) {
        this.spouseId = spouseId;
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

    public LongFilter getOrganizationsId() {
        return organizationsId;
    }

    public LongFilter organizationsId() {
        if (organizationsId == null) {
            organizationsId = new LongFilter();
        }
        return organizationsId;
    }

    public void setOrganizationsId(LongFilter organizationsId) {
        this.organizationsId = organizationsId;
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

    public LongFilter getPersonsInHouseId() {
        return personsInHouseId;
    }

    public LongFilter personsInHouseId() {
        if (personsInHouseId == null) {
            personsInHouseId = new LongFilter();
        }
        return personsInHouseId;
    }

    public void setPersonsInHouseId(LongFilter personsInHouseId) {
        this.personsInHouseId = personsInHouseId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PersonCriteria that = (PersonCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(prefix, that.prefix) &&
            Objects.equals(preferredName, that.preferredName) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(middleName, that.middleName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(suffix, that.suffix) &&
            Objects.equals(nameTag, that.nameTag) &&
            Objects.equals(currentMember, that.currentMember) &&
            Objects.equals(membershipStartDate, that.membershipStartDate) &&
            Objects.equals(membershipExpirationDate, that.membershipExpirationDate) &&
            Objects.equals(isHeadOfHouse, that.isHeadOfHouse) &&
            Objects.equals(isDeceased, that.isDeceased) &&
            Objects.equals(spouseId, that.spouseId) &&
            Objects.equals(membershipLevelId, that.membershipLevelId) &&
            Objects.equals(headOfHouseId, that.headOfHouseId) &&
            Objects.equals(parishId, that.parishId) &&
            Objects.equals(organizationsId, that.organizationsId) &&
            Objects.equals(houseDetailsId, that.houseDetailsId) &&
            Objects.equals(notesId, that.notesId) &&
            Objects.equals(phonesId, that.phonesId) &&
            Objects.equals(transactionsId, that.transactionsId) &&
            Objects.equals(emailsId, that.emailsId) &&
            Objects.equals(personsInHouseId, that.personsInHouseId) &&
            Objects.equals(ticketsId, that.ticketsId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            prefix,
            preferredName,
            firstName,
            middleName,
            lastName,
            suffix,
            nameTag,
            currentMember,
            membershipStartDate,
            membershipExpirationDate,
            isHeadOfHouse,
            isDeceased,
            spouseId,
            membershipLevelId,
            headOfHouseId,
            parishId,
            organizationsId,
            houseDetailsId,
            notesId,
            phonesId,
            transactionsId,
            emailsId,
            personsInHouseId,
            ticketsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (prefix != null ? "prefix=" + prefix + ", " : "") +
            (preferredName != null ? "preferredName=" + preferredName + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (middleName != null ? "middleName=" + middleName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (suffix != null ? "suffix=" + suffix + ", " : "") +
            (nameTag != null ? "nameTag=" + nameTag + ", " : "") +
            (currentMember != null ? "currentMember=" + currentMember + ", " : "") +
            (membershipStartDate != null ? "membershipStartDate=" + membershipStartDate + ", " : "") +
            (membershipExpirationDate != null ? "membershipExpirationDate=" + membershipExpirationDate + ", " : "") +
            (isHeadOfHouse != null ? "isHeadOfHouse=" + isHeadOfHouse + ", " : "") +
            (isDeceased != null ? "isDeceased=" + isDeceased + ", " : "") +
            (spouseId != null ? "spouseId=" + spouseId + ", " : "") +
            (membershipLevelId != null ? "membershipLevelId=" + membershipLevelId + ", " : "") +
            (headOfHouseId != null ? "headOfHouseId=" + headOfHouseId + ", " : "") +
            (parishId != null ? "parishId=" + parishId + ", " : "") +
            (organizationsId != null ? "organizationsId=" + organizationsId + ", " : "") +
            (houseDetailsId != null ? "houseDetailsId=" + houseDetailsId + ", " : "") +
            (notesId != null ? "notesId=" + notesId + ", " : "") +
            (phonesId != null ? "phonesId=" + phonesId + ", " : "") +
            (transactionsId != null ? "transactionsId=" + transactionsId + ", " : "") +
            (emailsId != null ? "emailsId=" + emailsId + ", " : "") +
            (personsInHouseId != null ? "personsInHouseId=" + personsInHouseId + ", " : "") +
            (ticketsId != null ? "ticketsId=" + ticketsId + ", " : "") +
            "}";
    }
}
