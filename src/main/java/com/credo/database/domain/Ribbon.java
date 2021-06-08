package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ribbon.
 */
@Entity
@Table(name = "ribbon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ribbon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "ribbon")
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
    private Set<Person> people = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ribbon id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Ribbon name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Person> getPeople() {
        return this.people;
    }

    public Ribbon people(Set<Person> people) {
        this.setPeople(people);
        return this;
    }

    public Ribbon addPeople(Person person) {
        this.people.add(person);
        person.setRibbon(this);
        return this;
    }

    public Ribbon removePeople(Person person) {
        this.people.remove(person);
        person.setRibbon(null);
        return this;
    }

    public void setPeople(Set<Person> people) {
        if (this.people != null) {
            this.people.forEach(i -> i.setRibbon(null));
        }
        if (people != null) {
            people.forEach(i -> i.setRibbon(this));
        }
        this.people = people;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ribbon)) {
            return false;
        }
        return id != null && id.equals(((Ribbon) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ribbon{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
