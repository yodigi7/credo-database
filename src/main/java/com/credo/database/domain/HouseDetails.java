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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HouseDetails.
 */
@Entity
@Table(name = "house_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HouseDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "mailing_label")
    private String mailingLabel;

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
    @OneToOne
    @JoinColumn(unique = true)
    private Person headOfHouse;

    @OneToMany(mappedBy = "houseDetails")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "houseDetails" }, allowSetters = true)
    private Set<HouseAddress> addresses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HouseDetails id(Long id) {
        this.id = id;
        return this;
    }

    public String getMailingLabel() {
        return this.mailingLabel;
    }

    public HouseDetails mailingLabel(String mailingLabel) {
        this.mailingLabel = mailingLabel;
        return this;
    }

    public void setMailingLabel(String mailingLabel) {
        this.mailingLabel = mailingLabel;
    }

    public Person getHeadOfHouse() {
        return this.headOfHouse;
    }

    public HouseDetails headOfHouse(Person person) {
        this.setHeadOfHouse(person);
        return this;
    }

    public void setHeadOfHouse(Person person) {
        this.headOfHouse = person;
    }

    public Set<HouseAddress> getAddresses() {
        return this.addresses;
    }

    public HouseDetails addresses(Set<HouseAddress> houseAddresses) {
        this.setAddresses(houseAddresses);
        return this;
    }

    public HouseDetails addAddresses(HouseAddress houseAddress) {
        this.addresses.add(houseAddress);
        houseAddress.setHouseDetails(this);
        return this;
    }

    public HouseDetails removeAddresses(HouseAddress houseAddress) {
        this.addresses.remove(houseAddress);
        houseAddress.setHouseDetails(null);
        return this;
    }

    public void setAddresses(Set<HouseAddress> houseAddresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setHouseDetails(null));
        }
        if (houseAddresses != null) {
            houseAddresses.forEach(i -> i.setHouseDetails(this));
        }
        this.addresses = houseAddresses;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HouseDetails)) {
            return false;
        }
        return id != null && id.equals(((HouseDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HouseDetails{" +
            "id=" + getId() +
            ", mailingLabel='" + getMailingLabel() + "'" +
            "}";
    }
}
