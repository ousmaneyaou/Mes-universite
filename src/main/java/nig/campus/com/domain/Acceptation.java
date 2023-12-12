package nig.campus.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Acceptation.
 */
@Entity
@Table(name = "acceptation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Acceptation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "filiere")
    private String filiere;

    @Column(name = "date_acceptation")
    private LocalDate dateAcceptation;

    @ManyToOne
    @JsonIgnoreProperties(value = { "session" }, allowSetters = true)
    private Inscription inscription;

    @ManyToOne
    @JsonIgnoreProperties(value = { "session" }, allowSetters = true)
    private Depot depot;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Acceptation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFiliere() {
        return this.filiere;
    }

    public Acceptation filiere(String filiere) {
        this.setFiliere(filiere);
        return this;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public LocalDate getDateAcceptation() {
        return this.dateAcceptation;
    }

    public Acceptation dateAcceptation(LocalDate dateAcceptation) {
        this.setDateAcceptation(dateAcceptation);
        return this;
    }

    public void setDateAcceptation(LocalDate dateAcceptation) {
        this.dateAcceptation = dateAcceptation;
    }

    public Inscription getInscription() {
        return this.inscription;
    }

    public void setInscription(Inscription inscription) {
        this.inscription = inscription;
    }

    public Acceptation inscription(Inscription inscription) {
        this.setInscription(inscription);
        return this;
    }

    public Depot getDepot() {
        return this.depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public Acceptation depot(Depot depot) {
        this.setDepot(depot);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Acceptation user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Acceptation)) {
            return false;
        }
        return id != null && id.equals(((Acceptation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Acceptation{" +
            "id=" + getId() +
            ", filiere='" + getFiliere() + "'" +
            ", dateAcceptation='" + getDateAcceptation() + "'" +
            "}";
    }
}
