package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrganizationNotes.
 */
@Entity
@Table(name = "organization_notes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OrganizationNotes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "notes")
    private String notes;

    @JsonIgnoreProperties(value = { "parish", "notes", "addresses", "phones", "emails", "persons" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrganizationNotes id(Long id) {
        this.id = id;
        return this;
    }

    public String getNotes() {
        return this.notes;
    }

    public OrganizationNotes notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public OrganizationNotes organization(Organization organization) {
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
        if (!(o instanceof OrganizationNotes)) {
            return false;
        }
        return id != null && id.equals(((OrganizationNotes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationNotes{" +
            "id=" + getId() +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
