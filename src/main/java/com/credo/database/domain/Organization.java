package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mailing_label")
    private String mailingLabel;

    @ManyToOne
    @JsonIgnoreProperties(value = { "organizations", "phones", "people", "emails" }, allowSetters = true)
    private Parish parish;

    @JsonIgnoreProperties(value = { "organization" }, allowSetters = true)
    @OneToOne(mappedBy = "organization")
    private OrganizationNotes notes;

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "organization" }, allowSetters = true)
    private Set<OrganizationAddress> addresses = new HashSet<>();

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "organization" }, allowSetters = true)
    private Set<OrganizationPhone> phones = new HashSet<>();

    @OneToMany(mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "organization" }, allowSetters = true)
    private Set<OrganizationEmail> emails = new HashSet<>();

    @ManyToMany(mappedBy = "organizations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "spouse",
            "membershipLevel",
            "headOfHouse",
            "ribbon",
            "parish",
            "organizations",
            "houseDetails",
            "notes",
            "phones",
            "transactions",
            "emails",
            "personsInHouses",
            "tickets",
            "perks",
        },
        allowSetters = true
    )
    private Set<Person> persons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Organization id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Organization name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailingLabel() {
        return this.mailingLabel;
    }

    public Organization mailingLabel(String mailingLabel) {
        this.mailingLabel = mailingLabel;
        return this;
    }

    public void setMailingLabel(String mailingLabel) {
        this.mailingLabel = mailingLabel;
    }

    public Parish getParish() {
        return this.parish;
    }

    public Organization parish(Parish parish) {
        this.setParish(parish);
        return this;
    }

    public void setParish(Parish parish) {
        this.parish = parish;
    }

    public OrganizationNotes getNotes() {
        return this.notes;
    }

    public Organization notes(OrganizationNotes organizationNotes) {
        this.setNotes(organizationNotes);
        return this;
    }

    public void setNotes(OrganizationNotes organizationNotes) {
        if (this.notes != null) {
            this.notes.setOrganization(null);
        }
        if (notes != null) {
            notes.setOrganization(this);
        }
        this.notes = organizationNotes;
    }

    public Set<OrganizationAddress> getAddresses() {
        return this.addresses;
    }

    public Organization addresses(Set<OrganizationAddress> organizationAddresses) {
        this.setAddresses(organizationAddresses);
        return this;
    }

    public Organization addAddresses(OrganizationAddress organizationAddress) {
        this.addresses.add(organizationAddress);
        organizationAddress.setOrganization(this);
        return this;
    }

    public Organization removeAddresses(OrganizationAddress organizationAddress) {
        this.addresses.remove(organizationAddress);
        organizationAddress.setOrganization(null);
        return this;
    }

    public void setAddresses(Set<OrganizationAddress> organizationAddresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setOrganization(null));
        }
        if (organizationAddresses != null) {
            organizationAddresses.forEach(i -> i.setOrganization(this));
        }
        this.addresses = organizationAddresses;
    }

    public Set<OrganizationPhone> getPhones() {
        return this.phones;
    }

    public Organization phones(Set<OrganizationPhone> organizationPhones) {
        this.setPhones(organizationPhones);
        return this;
    }

    public Organization addPhones(OrganizationPhone organizationPhone) {
        this.phones.add(organizationPhone);
        organizationPhone.setOrganization(this);
        return this;
    }

    public Organization removePhones(OrganizationPhone organizationPhone) {
        this.phones.remove(organizationPhone);
        organizationPhone.setOrganization(null);
        return this;
    }

    public void setPhones(Set<OrganizationPhone> organizationPhones) {
        if (this.phones != null) {
            this.phones.forEach(i -> i.setOrganization(null));
        }
        if (organizationPhones != null) {
            organizationPhones.forEach(i -> i.setOrganization(this));
        }
        this.phones = organizationPhones;
    }

    public Set<OrganizationEmail> getEmails() {
        return this.emails;
    }

    public Organization emails(Set<OrganizationEmail> organizationEmails) {
        this.setEmails(organizationEmails);
        return this;
    }

    public Organization addEmails(OrganizationEmail organizationEmail) {
        this.emails.add(organizationEmail);
        organizationEmail.setOrganization(this);
        return this;
    }

    public Organization removeEmails(OrganizationEmail organizationEmail) {
        this.emails.remove(organizationEmail);
        organizationEmail.setOrganization(null);
        return this;
    }

    public void setEmails(Set<OrganizationEmail> organizationEmails) {
        if (this.emails != null) {
            this.emails.forEach(i -> i.setOrganization(null));
        }
        if (organizationEmails != null) {
            organizationEmails.forEach(i -> i.setOrganization(this));
        }
        this.emails = organizationEmails;
    }

    public Set<Person> getPersons() {
        return this.persons;
    }

    public Organization persons(Set<Person> people) {
        this.setPersons(people);
        return this;
    }

    public Organization addPersons(Person person) {
        this.persons.add(person);
        person.getOrganizations().add(this);
        return this;
    }

    public Organization removePersons(Person person) {
        this.persons.remove(person);
        person.getOrganizations().remove(this);
        return this;
    }

    public void setPersons(Set<Person> people) {
        if (this.persons != null) {
            this.persons.forEach(i -> i.removeOrganizations(this));
        }
        if (people != null) {
            people.forEach(i -> i.addOrganizations(this));
        }
        this.persons = people;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return id != null && id.equals(((Organization) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", mailingLabel='" + getMailingLabel() + "'" +
            "}";
    }
}
