package nig.campus.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import nig.campus.com.domain.enumeration.EnumSexe;

/**
 * A Bachelier.
 */
@Entity
@Table(name = "bachelier")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bachelier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe")
    private EnumSexe sexe;

    @Column(name = "date_naissance")
    private Instant dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "telephone")
    private String telephone;

    @OneToOne
    @JoinColumn(unique = true)
    private User utilisateur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "session" }, allowSetters = true)
    private Depot depot;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bachelier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnumSexe getSexe() {
        return this.sexe;
    }

    public Bachelier sexe(EnumSexe sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(EnumSexe sexe) {
        this.sexe = sexe;
    }

    public Instant getDateNaissance() {
        return this.dateNaissance;
    }

    public Bachelier dateNaissance(Instant dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(Instant dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return this.lieuNaissance;
    }

    public Bachelier lieuNaissance(String lieuNaissance) {
        this.setLieuNaissance(lieuNaissance);
        return this;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getNationalite() {
        return this.nationalite;
    }

    public Bachelier nationalite(String nationalite) {
        this.setNationalite(nationalite);
        return this;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Bachelier telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public User getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(User user) {
        this.utilisateur = user;
    }

    public Bachelier utilisateur(User user) {
        this.setUtilisateur(user);
        return this;
    }

    public Depot getDepot() {
        return this.depot;
    }

    public void setDepot(Depot depot) {
        this.depot = depot;
    }

    public Bachelier depot(Depot depot) {
        this.setDepot(depot);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bachelier)) {
            return false;
        }
        return id != null && id.equals(((Bachelier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bachelier{" +
            "id=" + getId() +
            ", sexe='" + getSexe() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", lieuNaissance='" + getLieuNaissance() + "'" +
            ", nationalite='" + getNationalite() + "'" +
            ", telephone='" + getTelephone() + "'" +
            "}";
    }
}
