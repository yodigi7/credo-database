package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tickets", "membershipLevel", "person", "event" }, allowSetters = true)
    private Set<Transaction> transactions = new HashSet<>();

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "person", "event", "transaction", "nameTags" }, allowSetters = true)
    private Set<Ticket> tickets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Event name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Event date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    public Event transactions(Set<Transaction> transactions) {
        this.setTransactions(transactions);
        return this;
    }

    public Event addTransactions(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setEvent(this);
        return this;
    }

    public Event removeTransactions(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setEvent(null);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        if (this.transactions != null) {
            this.transactions.forEach(i -> i.setEvent(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setEvent(this));
        }
        this.transactions = transactions;
    }

    public Set<Ticket> getTickets() {
        return this.tickets;
    }

    public Event tickets(Set<Ticket> tickets) {
        this.setTickets(tickets);
        return this;
    }

    public Event addTickets(Ticket ticket) {
        this.tickets.add(ticket);
        ticket.setEvent(this);
        return this;
    }

    public Event removeTickets(Ticket ticket) {
        this.tickets.remove(ticket);
        ticket.setEvent(null);
        return this;
    }

    public void setTickets(Set<Ticket> tickets) {
        if (this.tickets != null) {
            this.tickets.forEach(i -> i.setEvent(null));
        }
        if (tickets != null) {
            tickets.forEach(i -> i.setEvent(this));
        }
        this.tickets = tickets;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
