package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Parish.
 */
@Entity
@Table(name = "parish")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Parish implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "parish")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parish", "notes", "addresses", "phones", "emails", "persons" }, allowSetters = true)
    private Set<Organization> organizations = new HashSet<>();

    @OneToMany(mappedBy = "parish")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parish" }, allowSetters = true)
    private Set<ParishPhone> phones = new HashSet<>();

    @OneToMany(mappedBy = "parish")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "spouse",
            "membershipLevel",
            "headOfHouse",
            "parish",
            "organizations",
            "houseDetails",
            "notes",
            "phones",
            "transactions",
            "emails",
            "personsInHouses",
            "tickets",
        },
        allowSetters = true
    )
    private Set<Person> people = new HashSet<>();

    @OneToMany(mappedBy = "parish")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "parish" }, allowSetters = true)
    private Set<ParishEmail> emails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parish id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Parish name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Organization> getOrganizations() {
        return this.organizations;
    }

    public Parish organizations(Set<Organization> organizations) {
        this.setOrganizations(organizations);
        return this;
    }

    public Parish addOrganizations(Organization organization) {
        this.organizations.add(organization);
        organization.setParish(this);
        return this;
    }

    public Parish removeOrganizations(Organization organization) {
        this.organizations.remove(organization);
        organization.setParish(null);
        return this;
    }

    public void setOrganizations(Set<Organization> organizations) {
        if (this.organizations != null) {
            this.organizations.forEach(i -> i.setParish(null));
        }
        if (organizations != null) {
            organizations.forEach(i -> i.setParish(this));
        }
        this.organizations = organizations;
    }

    public Set<ParishPhone> getPhones() {
        return this.phones;
    }

    public Parish phones(Set<ParishPhone> parishPhones) {
        this.setPhones(parishPhones);
        return this;
    }

    public Parish addPhones(ParishPhone parishPhone) {
        this.phones.add(parishPhone);
        parishPhone.setParish(this);
        return this;
    }

    public Parish removePhones(ParishPhone parishPhone) {
        this.phones.remove(parishPhone);
        parishPhone.setParish(null);
        return this;
    }

    public void setPhones(Set<ParishPhone> parishPhones) {
        if (this.phones != null) {
            this.phones.forEach(i -> i.setParish(null));
        }
        if (parishPhones != null) {
            parishPhones.forEach(i -> i.setParish(this));
        }
        this.phones = parishPhones;
    }

    public Set<Person> getPeople() {
        return this.people;
    }

    public Parish people(Set<Person> people) {
        this.setPeople(people);
        return this;
    }

    public Parish addPeople(Person person) {
        this.people.add(person);
        person.setParish(this);
        return this;
    }

    public Parish removePeople(Person person) {
        this.people.remove(person);
        person.setParish(null);
        return this;
    }

    public void setPeople(Set<Person> people) {
        if (this.people != null) {
            this.people.forEach(i -> i.setParish(null));
        }
        if (people != null) {
            people.forEach(i -> i.setParish(this));
        }
        this.people = people;
    }

    public Set<ParishEmail> getEmails() {
        return this.emails;
    }

    public Parish emails(Set<ParishEmail> parishEmails) {
        this.setEmails(parishEmails);
        return this;
    }

    public Parish addEmails(ParishEmail parishEmail) {
        this.emails.add(parishEmail);
        parishEmail.setParish(this);
        return this;
    }

    public Parish removeEmails(ParishEmail parishEmail) {
        this.emails.remove(parishEmail);
        parishEmail.setParish(null);
        return this;
    }

    public void setEmails(Set<ParishEmail> parishEmails) {
        if (this.emails != null) {
            this.emails.forEach(i -> i.setParish(null));
        }
        if (parishEmails != null) {
            parishEmails.forEach(i -> i.setParish(this));
        }
        this.emails = parishEmails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Parish)) {
            return false;
        }
        return id != null && id.equals(((Parish) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Parish{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
