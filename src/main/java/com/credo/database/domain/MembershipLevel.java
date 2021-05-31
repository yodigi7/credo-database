package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MembershipLevel.
 */
@Entity
@Table(name = "membership_level")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MembershipLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "level", nullable = false, unique = true)
    private String level;

    @OneToMany(mappedBy = "membershipLevel")
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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MembershipLevel id(Long id) {
        this.id = id;
        return this;
    }

    public String getLevel() {
        return this.level;
    }

    public MembershipLevel level(String level) {
        this.level = level;
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Set<Person> getPeople() {
        return this.people;
    }

    public MembershipLevel people(Set<Person> people) {
        this.setPeople(people);
        return this;
    }

    public MembershipLevel addPeople(Person person) {
        this.people.add(person);
        person.setMembershipLevel(this);
        return this;
    }

    public MembershipLevel removePeople(Person person) {
        this.people.remove(person);
        person.setMembershipLevel(null);
        return this;
    }

    public void setPeople(Set<Person> people) {
        if (this.people != null) {
            this.people.forEach(i -> i.setMembershipLevel(null));
        }
        if (people != null) {
            people.forEach(i -> i.setMembershipLevel(this));
        }
        this.people = people;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MembershipLevel)) {
            return false;
        }
        return id != null && id.equals(((MembershipLevel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MembershipLevel{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
