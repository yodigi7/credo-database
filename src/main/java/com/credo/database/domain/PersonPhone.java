package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PersonPhone.
 */
@Entity
@Table(name = "person_phone")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PersonPhone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 10)
    @Pattern(regexp = "^\\([0-9]{3}\\) [0-9]{3}-[0-9]{4}$")
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "type")
    private String type;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonPhone id(Long id) {
        this.id = id;
        return this;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public PersonPhone phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return this.type;
    }

    public PersonPhone type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Person getPerson() {
        return this.person;
    }

    public PersonPhone person(Person person) {
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
        if (!(o instanceof PersonPhone)) {
            return false;
        }
        return id != null && id.equals(((PersonPhone) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonPhone{" +
            "id=" + getId() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
