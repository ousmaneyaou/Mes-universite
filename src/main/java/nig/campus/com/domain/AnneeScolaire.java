package nig.campus.com.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A AnneeScolaire.
 */
@Entity
@Table(name = "annee_scolaire")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnneeScolaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "en_cour")
    private Boolean enCour;

    @ManyToOne
    private Campagne campagne;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AnneeScolaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public AnneeScolaire libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Boolean getEnCour() {
        return this.enCour;
    }

    public AnneeScolaire enCour(Boolean enCour) {
        this.setEnCour(enCour);
        return this;
    }

    public void setEnCour(Boolean enCour) {
        this.enCour = enCour;
    }

    public Campagne getCampagne() {
        return this.campagne;
    }

    public void setCampagne(Campagne campagne) {
        this.campagne = campagne;
    }

    public AnneeScolaire campagne(Campagne campagne) {
        this.setCampagne(campagne);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnneeScolaire)) {
            return false;
        }
        return id != null && id.equals(((AnneeScolaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnneeScolaire{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", enCour='" + getEnCour() + "'" +
            "}";
    }
}
