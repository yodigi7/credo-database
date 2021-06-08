package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventPerk.
 */
@Entity
@Table(name = "event_perk")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EventPerk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "minimum_price")
    private Double minimumPrice;

    @ManyToOne
    @JsonIgnoreProperties(value = { "transactions", "tickets", "perks" }, allowSetters = true)
    private Event event;

    @ManyToOne
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
    private Person person;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventPerk id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public EventPerk name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMinimumPrice() {
        return this.minimumPrice;
    }

    public EventPerk minimumPrice(Double minimumPrice) {
        this.minimumPrice = minimumPrice;
        return this;
    }

    public void setMinimumPrice(Double minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public Event getEvent() {
        return this.event;
    }

    public EventPerk event(Event event) {
        this.setEvent(event);
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Person getPerson() {
        return this.person;
    }

    public EventPerk person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventPerk)) {
            return false;
        }
        return id != null && id.equals(((EventPerk) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventPerk{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", minimumPrice=" + getMinimumPrice() +
            "}";
    }
}
