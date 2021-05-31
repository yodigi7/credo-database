package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
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

    @DecimalMin(value = "0")
    @Column(name = "cost_per_ticket")
    private Double costPerTicket;

    @ManyToOne
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
    private Person person;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tickets" }, allowSetters = true)
    private Event event;

    @JsonIgnoreProperties(value = { "tickets", "membershipLevel", "person" }, allowSetters = true)
    @OneToOne(mappedBy = "tickets")
    private Transaction transaction;

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

    public Double getCostPerTicket() {
        return this.costPerTicket;
    }

    public Ticket costPerTicket(Double costPerTicket) {
        this.costPerTicket = costPerTicket;
        return this;
    }

    public void setCostPerTicket(Double costPerTicket) {
        this.costPerTicket = costPerTicket;
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

    public Event getEvent() {
        return this.event;
    }

    public Ticket event(Event event) {
        this.setEvent(event);
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public Ticket transaction(Transaction transaction) {
        this.setTransaction(transaction);
        return this;
    }

    public void setTransaction(Transaction transaction) {
        if (this.transaction != null) {
            this.transaction.setTickets(null);
        }
        if (transaction != null) {
            transaction.setTickets(this);
        }
        this.transaction = transaction;
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
            ", costPerTicket=" + getCostPerTicket() +
            "}";
    }
}
