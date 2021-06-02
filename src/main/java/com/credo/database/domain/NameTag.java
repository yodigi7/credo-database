package com.credo.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NameTag.
 */
@Entity
@Table(name = "name_tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NameTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name_tag")
    private String nameTag;

    @ManyToOne
    @JsonIgnoreProperties(value = { "person", "event", "transaction", "nameTags" }, allowSetters = true)
    private Ticket ticket;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NameTag id(Long id) {
        this.id = id;
        return this;
    }

    public String getNameTag() {
        return this.nameTag;
    }

    public NameTag nameTag(String nameTag) {
        this.nameTag = nameTag;
        return this;
    }

    public void setNameTag(String nameTag) {
        this.nameTag = nameTag;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public NameTag ticket(Ticket ticket) {
        this.setTicket(ticket);
        return this;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NameTag)) {
            return false;
        }
        return id != null && id.equals(((NameTag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NameTag{" +
            "id=" + getId() +
            ", nameTag='" + getNameTag() + "'" +
            "}";
    }
}
