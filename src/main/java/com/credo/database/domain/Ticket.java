package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ticket.
 */
@Entity
@Table(name = "ticket")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Min(value = 0)
    @Column(name = "count")
    private Integer count;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "spouse",
            "membershipLevel",
            "headOfHouse",
            "parish",
            "relationship",
            "organizations",
            "houseDetails",
            "notes",
            "phones",
            "payments",
            "emails",
            "personsInHouses",
            "tickets",
        },
        allowSetters = true
    )
    private Person person;

    @OneToMany(mappedBy = "tickets", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tickets", "person" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(mappedBy = "tickets", cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tickets" }, allowSetters = true)
    private Set<Event> events = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ticket id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getCount() {
        return this.count;
    }

    public Ticket count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Person getPerson() {
        return this.person;
    }

    public Ticket person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public Ticket payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Ticket addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setTickets(this);
        return this;
    }

    public Ticket removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setTickets(null);
        return this;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setTickets(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setTickets(this));
        }
        this.payments = payments;
    }

    public Set<Event> getEvents() {
        return this.events;
    }

    public Ticket events(Set<Event> events) {
        this.setEvents(events);
        return this;
    }

    public Ticket addEvent(Event event) {
        this.events.add(event);
        event.setTickets(this);
        return this;
    }

    public Ticket removeEvent(Event event) {
        this.events.remove(event);
        event.setTickets(null);
        return this;
    }

    public void setEvents(Set<Event> events) {
        if (this.events != null) {
            this.events.forEach(i -> i.setTickets(null));
        }
        if (events != null) {
            events.forEach(i -> i.setTickets(this));
        }
        this.events = events;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        return id != null && id.equals(((Ticket) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ticket{" +
            "id=" + getId() +
            ", count=" + getCount() +
            "}";
    }
}
