package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "preferred_name")
    private String preferredName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "suffix")
    private String suffix;

    @Column(name = "name_tag")
    private String nameTag;

    @Column(name = "current_member")
    private Boolean currentMember;

    @Column(name = "membership_start_date")
    private LocalDate membershipStartDate;

    @Column(name = "membership_expiration_date")
    private LocalDate membershipExpirationDate;

    @NotNull
    @Column(name = "is_head_of_house", nullable = false)
    private Boolean isHeadOfHouse;

    @NotNull
    @Column(name = "is_deceased", nullable = false)
    private Boolean isDeceased;

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
    @OneToOne
    @JoinColumn(unique = true)
    private Person spouse;

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
    private Person headOfHouse;

    @ManyToOne
    @JsonIgnoreProperties(value = { "organizations", "phones", "people", "emails" }, allowSetters = true)
    private Parish parish;

    @ManyToOne
    @JsonIgnoreProperties(value = { "people" }, allowSetters = true)
    private Relationship relationship;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_person__organizations",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "organizations_id")
    )
    @JsonIgnoreProperties(value = { "parish", "notes", "addresses", "phones", "emails", "persons" }, allowSetters = true)
    private Set<Organization> organizations = new HashSet<>();

    @JsonIgnoreProperties(value = { "headOfHouse", "addresses" }, allowSetters = true)
    @OneToOne(mappedBy = "headOfHouse")
    private HouseDetails houseDetails;

    @JsonIgnoreProperties(value = { "person" }, allowSetters = true)
    @OneToOne(mappedBy = "person")
    private PersonNotes notes;

    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "person" }, allowSetters = true)
    private Set<PersonPhone> phones = new HashSet<>();

    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tickets", "person" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "person" }, allowSetters = true)
    private Set<PersonEmail> emails = new HashSet<>();

    @OneToMany(mappedBy = "headOfHouse")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Set<Person> personsInHouses = new HashSet<>();

    @OneToMany(mappedBy = "person")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "person", "payments", "events" }, allowSetters = true)
    private Set<Ticket> tickets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person id(Long id) {
        this.id = id;
        return this;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Person prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPreferredName() {
        return this.preferredName;
    }

    public Person preferredName(String preferredName) {
        this.preferredName = preferredName;
        return this;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Person firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public Person middleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Person lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public Person suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getNameTag() {
        return this.nameTag;
    }

    public Person nameTag(String nameTag) {
        this.nameTag = nameTag;
        return this;
    }

    public void setNameTag(String nameTag) {
        this.nameTag = nameTag;
    }

    public Boolean getCurrentMember() {
        return this.currentMember;
    }

    public Person currentMember(Boolean currentMember) {
        this.currentMember = currentMember;
        return this;
    }

    public void setCurrentMember(Boolean currentMember) {
        this.currentMember = currentMember;
    }

    public LocalDate getMembershipStartDate() {
        return this.membershipStartDate;
    }

    public Person membershipStartDate(LocalDate membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
        return this;
    }

    public void setMembershipStartDate(LocalDate membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    }

    public LocalDate getMembershipExpirationDate() {
        return this.membershipExpirationDate;
    }

    public Person membershipExpirationDate(LocalDate membershipExpirationDate) {
        this.membershipExpirationDate = membershipExpirationDate;
        return this;
    }

    public void setMembershipExpirationDate(LocalDate membershipExpirationDate) {
        this.membershipExpirationDate = membershipExpirationDate;
    }

    public Boolean getIsHeadOfHouse() {
        return this.isHeadOfHouse;
    }

    public Person isHeadOfHouse(Boolean isHeadOfHouse) {
        this.isHeadOfHouse = isHeadOfHouse;
        return this;
    }

    public void setIsHeadOfHouse(Boolean isHeadOfHouse) {
        this.isHeadOfHouse = isHeadOfHouse;
    }

    public Boolean getIsDeceased() {
        return this.isDeceased;
    }

    public Person isDeceased(Boolean isDeceased) {
        this.isDeceased = isDeceased;
        return this;
    }

    public void setIsDeceased(Boolean isDeceased) {
        this.isDeceased = isDeceased;
    }

    public Person getSpouse() {
        return this.spouse;
    }

    public Person spouse(Person person) {
        this.setSpouse(person);
        return this;
    }

    public void setSpouse(Person person) {
        this.spouse = person;
    }

    public MembershipLevel getMembershipLevel() {
        return this.membershipLevel;
    }

    public Person membershipLevel(MembershipLevel membershipLevel) {
        this.setMembershipLevel(membershipLevel);
        return this;
    }

    public void setMembershipLevel(MembershipLevel membershipLevel) {
        this.membershipLevel = membershipLevel;
    }

    public Person getHeadOfHouse() {
        return this.headOfHouse;
    }

    public Person headOfHouse(Person person) {
        this.setHeadOfHouse(person);
        return this;
    }

    public void setHeadOfHouse(Person person) {
        this.headOfHouse = person;
    }

    public Parish getParish() {
        return this.parish;
    }

    public Person parish(Parish parish) {
        this.setParish(parish);
        return this;
    }

    public void setParish(Parish parish) {
        this.parish = parish;
    }

    public Relationship getRelationship() {
        return this.relationship;
    }

    public Person relationship(Relationship relationship) {
        this.setRelationship(relationship);
        return this;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    public Set<Organization> getOrganizations() {
        return this.organizations;
    }

    public Person organizations(Set<Organization> organizations) {
        this.setOrganizations(organizations);
        return this;
    }

    public Person addOrganizations(Organization organization) {
        this.organizations.add(organization);
        organization.getPersons().add(this);
        return this;
    }

    public Person removeOrganizations(Organization organization) {
        this.organizations.remove(organization);
        organization.getPersons().remove(this);
        return this;
    }

    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }

    public HouseDetails getHouseDetails() {
        return this.houseDetails;
    }

    public Person houseDetails(HouseDetails houseDetails) {
        this.setHouseDetails(houseDetails);
        return this;
    }

    public void setHouseDetails(HouseDetails houseDetails) {
        if (this.houseDetails != null) {
            this.houseDetails.setHeadOfHouse(null);
        }
        if (houseDetails != null) {
            houseDetails.setHeadOfHouse(this);
        }
        this.houseDetails = houseDetails;
    }

    public PersonNotes getNotes() {
        return this.notes;
    }

    public Person notes(PersonNotes personNotes) {
        this.setNotes(personNotes);
        return this;
    }

    public void setNotes(PersonNotes personNotes) {
        if (this.notes != null) {
            this.notes.setPerson(null);
        }
        if (notes != null) {
            notes.setPerson(this);
        }
        this.notes = personNotes;
    }

    public Set<PersonPhone> getPhones() {
        return this.phones;
    }

    public Person phones(Set<PersonPhone> personPhones) {
        this.setPhones(personPhones);
        return this;
    }

    public Person addPhones(PersonPhone personPhone) {
        this.phones.add(personPhone);
        personPhone.setPerson(this);
        return this;
    }

    public Person removePhones(PersonPhone personPhone) {
        this.phones.remove(personPhone);
        personPhone.setPerson(null);
        return this;
    }

    public void setPhones(Set<PersonPhone> personPhones) {
        if (this.phones != null) {
            this.phones.forEach(i -> i.setPerson(null));
        }
        if (personPhones != null) {
            personPhones.forEach(i -> i.setPerson(this));
        }
        this.phones = personPhones;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public Person payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Person addPayments(Payment payment) {
        this.payments.add(payment);
        payment.setPerson(this);
        return this;
    }

    public Person removePayments(Payment payment) {
        this.payments.remove(payment);
        payment.setPerson(null);
        return this;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setPerson(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setPerson(this));
        }
        this.payments = payments;
    }

    public Set<PersonEmail> getEmails() {
        return this.emails;
    }

    public Person emails(Set<PersonEmail> personEmails) {
        this.setEmails(personEmails);
        return this;
    }

    public Person addEmails(PersonEmail personEmail) {
        this.emails.add(personEmail);
        personEmail.setPerson(this);
        return this;
    }

    public Person removeEmails(PersonEmail personEmail) {
        this.emails.remove(personEmail);
        personEmail.setPerson(null);
        return this;
    }

    public void setEmails(Set<PersonEmail> personEmails) {
        if (this.emails != null) {
            this.emails.forEach(i -> i.setPerson(null));
        }
        if (personEmails != null) {
            personEmails.forEach(i -> i.setPerson(this));
        }
        this.emails = personEmails;
    }

    public Set<Person> getPersonsInHouses() {
        return this.personsInHouses;
    }

    public Person personsInHouses(Set<Person> people) {
        this.setPersonsInHouses(people);
        return this;
    }

    public Person addPersonsInHouse(Person person) {
        this.personsInHouses.add(person);
        person.setHeadOfHouse(this);
        return this;
    }

    public Person removePersonsInHouse(Person person) {
        this.personsInHouses.remove(person);
        person.setHeadOfHouse(null);
        return this;
    }

    public void setPersonsInHouses(Set<Person> people) {
        if (this.personsInHouses != null) {
            this.personsInHouses.forEach(i -> i.setHeadOfHouse(null));
        }
        if (people != null) {
            people.forEach(i -> i.setHeadOfHouse(this));
        }
        this.personsInHouses = people;
    }

    public Set<Ticket> getTickets() {
        return this.tickets;
    }

    public Person tickets(Set<Ticket> tickets) {
        this.setTickets(tickets);
        return this;
    }

    public Person addTickets(Ticket ticket) {
        this.tickets.add(ticket);
        ticket.setPerson(this);
        return this;
    }

    public Person removeTickets(Ticket ticket) {
        this.tickets.remove(ticket);
        ticket.setPerson(null);
        return this;
    }

    public void setTickets(Set<Ticket> tickets) {
        if (this.tickets != null) {
            this.tickets.forEach(i -> i.setPerson(null));
        }
        if (tickets != null) {
            tickets.forEach(i -> i.setPerson(this));
        }
        this.tickets = tickets;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", prefix='" + getPrefix() + "'" +
            ", preferredName='" + getPreferredName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", middleName='" + getMiddleName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", suffix='" + getSuffix() + "'" +
            ", nameTag='" + getNameTag() + "'" +
            ", currentMember='" + getCurrentMember() + "'" +
            ", membershipStartDate='" + getMembershipStartDate() + "'" +
            ", membershipExpirationDate='" + getMembershipExpirationDate() + "'" +
            ", isHeadOfHouse='" + getIsHeadOfHouse() + "'" +
            ", isDeceased='" + getIsDeceased() + "'" +
            "}";
    }
}
