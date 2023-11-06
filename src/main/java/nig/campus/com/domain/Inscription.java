package nig.campus.com.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;

/**
 * A Inscription.
 */
@Entity
@Table(name = "inscription")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Inscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_inscription")
    private ZonedDateTime dateInscription;

    @Column(name = "regime")
    private Boolean regime;

    @ManyToOne
    private Session session;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateInscription() {
        return this.dateInscription;
    }

    public Inscription dateInscription(ZonedDateTime dateInscription) {
        this.setDateInscription(dateInscription);
        return this;
    }

    public void setDateInscription(ZonedDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Boolean getRegime() {
        return this.regime;
    }

    public Inscription regime(Boolean regime) {
        this.setRegime(regime);
        return this;
    }

    public void setRegime(Boolean regime) {
        this.regime = regime;
    }

    public Session getSession() {
        return this.session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Inscription session(Session session) {
        this.setSession(session);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inscription)) {
            return false;
        }
        return id != null && id.equals(((Inscription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inscription{" +
            "id=" + getId() +
            ", dateInscription='" + getDateInscription() + "'" +
            ", regime='" + getRegime() + "'" +
            "}";
    }
}
