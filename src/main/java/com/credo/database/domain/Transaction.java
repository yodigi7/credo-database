package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @DecimalMin(value = "0")
    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "generic_sub_items_purchased")
    private String genericSubItemsPurchased;

    @DecimalMin(value = "0")
    @Column(name = "cost_sub_items_purchased")
    private Double costSubItemsPurchased;

    @Min(value = 0)
    @Column(name = "number_of_memberships")
    private Integer numberOfMemberships;

    @DecimalMin(value = "0")
    @Column(name = "donation")
    private Double donation;

    @DecimalMin(value = "0")
    @Column(name = "event_donation")
    private Double eventDonation;

    @Column(name = "notes")
    private String notes;

    @JsonIgnoreProperties(value = { "person", "event", "transaction", "nameTags" }, allowSetters = true)
    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
    @JoinColumn(unique = true)
    private Ticket tickets;

    @ManyToOne
    @JsonIgnoreProperties(value = { "people" }, allowSetters = true)
    private MembershipLevel membershipLevel;

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
    @JsonIgnoreProperties(value = { "transactions", "tickets" }, allowSetters = true)
    private Event event;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction id(Long id) {
        this.id = id;
        return this;
    }

    public Double getTotalAmount() {
        return this.totalAmount;
    }

    public Transaction totalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Transaction date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getGenericSubItemsPurchased() {
        return this.genericSubItemsPurchased;
    }

    public Transaction genericSubItemsPurchased(String genericSubItemsPurchased) {
        this.genericSubItemsPurchased = genericSubItemsPurchased;
        return this;
    }

    public void setGenericSubItemsPurchased(String genericSubItemsPurchased) {
        this.genericSubItemsPurchased = genericSubItemsPurchased;
    }

    public Double getCostSubItemsPurchased() {
        return this.costSubItemsPurchased;
    }

    public Transaction costSubItemsPurchased(Double costSubItemsPurchased) {
        this.costSubItemsPurchased = costSubItemsPurchased;
        return this;
    }

    public void setCostSubItemsPurchased(Double costSubItemsPurchased) {
        this.costSubItemsPurchased = costSubItemsPurchased;
    }

    public Integer getNumberOfMemberships() {
        return this.numberOfMemberships;
    }

    public Transaction numberOfMemberships(Integer numberOfMemberships) {
        this.numberOfMemberships = numberOfMemberships;
        return this;
    }

    public void setNumberOfMemberships(Integer numberOfMemberships) {
        this.numberOfMemberships = numberOfMemberships;
    }

    public Double getDonation() {
        return this.donation;
    }

    public Transaction donation(Double donation) {
        this.donation = donation;
        return this;
    }

    public void setDonation(Double donation) {
        this.donation = donation;
    }

    public Double getEventDonation() {
        return this.eventDonation;
    }

    public Transaction eventDonation(Double eventDonation) {
        this.eventDonation = eventDonation;
        return this;
    }

    public void setEventDonation(Double eventDonation) {
        this.eventDonation = eventDonation;
    }

    public String getNotes() {
        return this.notes;
    }

    public Transaction notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Ticket getTickets() {
        return this.tickets;
    }

    public Transaction tickets(Ticket ticket) {
        this.setTickets(ticket);
        return this;
    }

    public void setTickets(Ticket ticket) {
        this.tickets = ticket;
    }

    public MembershipLevel getMembershipLevel() {
        return this.membershipLevel;
    }

    public Transaction membershipLevel(MembershipLevel membershipLevel) {
        this.setMembershipLevel(membershipLevel);
        return this;
    }

    public void setMembershipLevel(MembershipLevel membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public Person getPerson() {
        return this.person;
    }

    public Transaction person(Person person) {
        this.setPerson(person);
        return this;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Event getEvent() {
        return this.event;
    }

    public Transaction event(Event event) {
        this.setEvent(event);
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", totalAmount=" + getTotalAmount() +
            ", date='" + getDate() + "'" +
            ", genericSubItemsPurchased='" + getGenericSubItemsPurchased() + "'" +
            ", costSubItemsPurchased=" + getCostSubItemsPurchased() +
            ", numberOfMemberships=" + getNumberOfMemberships() +
            ", donation=" + getDonation() +
            ", eventDonation=" + getEventDonation() +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
