package nig.campus.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Dossier.
 */
@Entity
@Table(name = "dossier")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dossier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "valider")
    private Boolean valider;

    @ManyToOne
    @JsonIgnoreProperties(value = { "departement", "session" }, allowSetters = true)
    private Niveau niveau;

    @ManyToOne
    private Campagne campagne;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dossier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getValider() {
        return this.valider;
    }

    public Dossier valider(Boolean valider) {
        this.setValider(valider);
        return this;
    }

    public void setValider(Boolean valider) {
        this.valider = valider;
    }

    public Niveau getNiveau() {
        return this.niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Dossier niveau(Niveau niveau) {
        this.setNiveau(niveau);
        return this;
    }

    public Campagne getCampagne() {
        return this.campagne;
    }

    public void setCampagne(Campagne campagne) {
        this.campagne = campagne;
    }

    public Dossier campagne(Campagne campagne) {
        this.setCampagne(campagne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dossier)) {
            return false;
        }
        return id != null && id.equals(((Dossier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dossier{" +
            "id=" + getId() +
            ", valider='" + getValider() + "'" +
            "}";
    }
}
