package com.credo.database.domain;

import com.credo.database.domain.enumeration.YesNoEmpty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrganizationEmail.
 */
@Entity
@Table(name = "organization_email")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrganizationEmail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "type")
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_newsletter_subscription")
    private YesNoEmpty emailNewsletterSubscription;

    @Enumerated(EnumType.STRING)
    @Column(name = "email_event_notification_subscription")
    private YesNoEmpty emailEventNotificationSubscription;

    @ManyToOne
    @JsonIgnoreProperties(value = { "parish", "notes", "addresses", "phones", "emails", "persons" }, allowSetters = true)
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrganizationEmail id(Long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public OrganizationEmail email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return this.type;
    }

    public OrganizationEmail type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public YesNoEmpty getEmailNewsletterSubscription() {
        return this.emailNewsletterSubscription;
    }

    public OrganizationEmail emailNewsletterSubscription(YesNoEmpty emailNewsletterSubscription) {
        this.emailNewsletterSubscription = emailNewsletterSubscription;
        return this;
    }

    public void setEmailNewsletterSubscription(YesNoEmpty emailNewsletterSubscription) {
        this.emailNewsletterSubscription = emailNewsletterSubscription;
    }

    public YesNoEmpty getEmailEventNotificationSubscription() {
        return this.emailEventNotificationSubscription;
    }

    public OrganizationEmail emailEventNotificationSubscription(YesNoEmpty emailEventNotificationSubscription) {
        this.emailEventNotificationSubscription = emailEventNotificationSubscription;
        return this;
    }

    public void setEmailEventNotificationSubscription(YesNoEmpty emailEventNotificationSubscription) {
        this.emailEventNotificationSubscription = emailEventNotificationSubscription;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public OrganizationEmail organization(Organization organization) {
        this.setOrganization(organization);
        return this;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationEmail)) {
            return false;
        }
        return id != null && id.equals(((OrganizationEmail) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationEmail{" +
            "id=" + getId() +
            ", email='" + getEmail() + "'" +
            ", type='" + getType() + "'" +
            ", emailNewsletterSubscription='" + getEmailNewsletterSubscription() + "'" +
            ", emailEventNotificationSubscription='" + getEmailEventNotificationSubscription() + "'" +
            "}";
    }
}
