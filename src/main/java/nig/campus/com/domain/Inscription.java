package nig.campus.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
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
    private Paiement paiement;

    @ManyToMany
    @JoinTable(
        name = "rel_inscription__dossiers",
        joinColumns = @JoinColumn(name = "inscription_id"),
        inverseJoinColumns = @JoinColumn(name = "dossiers_id")
    )
    @JsonIgnoreProperties(value = { "niveau", "campagne", "inscriptions" }, allowSetters = true)
    private Set<Dossier> dossiers = new HashSet<>();

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

    public Paiement getPaiement() {
        return this.paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public Inscription paiement(Paiement paiement) {
        this.setPaiement(paiement);
        return this;
    }

    public Set<Dossier> getDossiers() {
        return this.dossiers;
    }

    public void setDossiers(Set<Dossier> dossiers) {
        this.dossiers = dossiers;
    }

    public Inscription dossiers(Set<Dossier> dossiers) {
        this.setDossiers(dossiers);
        return this;
    }

    public Inscription addDossiers(Dossier dossier) {
        this.dossiers.add(dossier);
        dossier.getInscriptions().add(this);
        return this;
    }

    public Inscription removeDossiers(Dossier dossier) {
        this.dossiers.remove(dossier);
        dossier.getInscriptions().remove(this);
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
