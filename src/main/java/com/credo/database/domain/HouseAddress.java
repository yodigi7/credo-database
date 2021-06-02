package com.credo.database.domain;

import com.credo.database.domain.enumeration.YesNoEmpty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HouseAddress.
 */
@Entity
@Table(name = "house_address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HouseAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "type")
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "mail_newsletter_subscription")
    private YesNoEmpty mailNewsletterSubscription;

    @Enumerated(EnumType.STRING)
    @Column(name = "mail_event_notification_subscription")
    private YesNoEmpty mailEventNotificationSubscription;

    @ManyToOne
    @JsonIgnoreProperties(value = { "headOfHouse", "addresses" }, allowSetters = true)
    private HouseDetails houseDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public HouseAddress id(Long id) {
        this.id = id;
        return this;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public HouseAddress streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return this.city;
    }

    public HouseAddress city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public HouseAddress state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return this.zipcode;
    }

    public HouseAddress zipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getType() {
        return this.type;
    }

    public HouseAddress type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public YesNoEmpty getMailNewsletterSubscription() {
        return this.mailNewsletterSubscription;
    }

    public HouseAddress mailNewsletterSubscription(YesNoEmpty mailNewsletterSubscription) {
        this.mailNewsletterSubscription = mailNewsletterSubscription;
        return this;
    }

    public void setMailNewsletterSubscription(YesNoEmpty mailNewsletterSubscription) {
        this.mailNewsletterSubscription = mailNewsletterSubscription;
    }

    public YesNoEmpty getMailEventNotificationSubscription() {
        return this.mailEventNotificationSubscription;
    }

    public HouseAddress mailEventNotificationSubscription(YesNoEmpty mailEventNotificationSubscription) {
        this.mailEventNotificationSubscription = mailEventNotificationSubscription;
        return this;
    }

    public void setMailEventNotificationSubscription(YesNoEmpty mailEventNotificationSubscription) {
        this.mailEventNotificationSubscription = mailEventNotificationSubscription;
    }

    public HouseDetails getHouseDetails() {
        return this.houseDetails;
    }

    public HouseAddress houseDetails(HouseDetails houseDetails) {
        this.setHouseDetails(houseDetails);
        return this;
    }

    public void setHouseDetails(HouseDetails houseDetails) {
        this.houseDetails = houseDetails;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HouseAddress)) {
            return false;
        }
        return id != null && id.equals(((HouseAddress) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HouseAddress{" +
            "id=" + getId() +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", zipcode='" + getZipcode() + "'" +
            ", type='" + getType() + "'" +
            ", mailNewsletterSubscription='" + getMailNewsletterSubscription() + "'" +
            ", mailEventNotificationSubscription='" + getMailEventNotificationSubscription() + "'" +
            "}";
    }
}
