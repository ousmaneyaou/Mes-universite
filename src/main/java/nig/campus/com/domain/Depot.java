package nig.campus.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import nig.campus.com.domain.enumeration.EnumSexe;

/**
 * A Depot.
 */
@Entity
@Table(name = "depot")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Depot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "date_naissance")
    private ZonedDateTime dateNaissance;

    @Column(name = "lieu_de_naissance")
    private String lieuDeNaissance;

    @Column(name = "email")
    private String email;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "telephone")
    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexe")
    private EnumSexe sexe;

    @Column(name = "date_depot")
    private ZonedDateTime dateDepot;

    @Column(name = "numero_de_table")
    private String numeroDeTable;

    @Column(name = "serie")
    private String serie;

    @Lob
    @Column(name = "diplome")
    private byte[] diplome;

    @Column(name = "diplome_content_type")
    private String diplomeContentType;

    @Lob
    @Column(name = "releve_de_note")
    private byte[] releveDeNote;

    @Column(name = "releve_de_note_content_type")
    private String releveDeNoteContentType;

    @Column(name = "annee_obtention")
    private String anneeObtention;

    @Column(name = "lieu_obtention")
    private String lieuObtention;

    @Column(name = "mention")
    private String mention;

    @Lob
    @Column(name = "lettre_de_motivation")
    private byte[] lettreDeMotivation;

    @Column(name = "lettre_de_motivation_content_type")
    private String lettreDeMotivationContentType;

    @Column(name = "choix_1")
    private String choix1;

    @Column(name = "choix_2")
    private String choix2;

    @Column(name = "choix_3")
    private String choix3;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "utilisateur" }, allowSetters = true)
    private Bachelier bachelier;

    @ManyToOne
    @JsonIgnoreProperties(value = { "niveau", "campagne", "inscriptions" }, allowSetters = true)
    private Dossier dossier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Depot id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Depot nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Depot prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public ZonedDateTime getDateNaissance() {
        return this.dateNaissance;
    }

    public Depot dateNaissance(ZonedDateTime dateNaissance) {
        this.setDateNaissance(dateNaissance);
        return this;
    }

    public void setDateNaissance(ZonedDateTime dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuDeNaissance() {
        return this.lieuDeNaissance;
    }

    public Depot lieuDeNaissance(String lieuDeNaissance) {
        this.setLieuDeNaissance(lieuDeNaissance);
        return this;
    }

    public void setLieuDeNaissance(String lieuDeNaissance) {
        this.lieuDeNaissance = lieuDeNaissance;
    }

    public String getEmail() {
        return this.email;
    }

    public Depot email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationalite() {
        return this.nationalite;
    }

    public Depot nationalite(String nationalite) {
        this.setNationalite(nationalite);
        return this;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Depot telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public EnumSexe getSexe() {
        return this.sexe;
    }

    public Depot sexe(EnumSexe sexe) {
        this.setSexe(sexe);
        return this;
    }

    public void setSexe(EnumSexe sexe) {
        this.sexe = sexe;
    }

    public ZonedDateTime getDateDepot() {
        return this.dateDepot;
    }

    public Depot dateDepot(ZonedDateTime dateDepot) {
        this.setDateDepot(dateDepot);
        return this;
    }

    public void setDateDepot(ZonedDateTime dateDepot) {
        this.dateDepot = dateDepot;
    }

    public String getNumeroDeTable() {
        return this.numeroDeTable;
    }

    public Depot numeroDeTable(String numeroDeTable) {
        this.setNumeroDeTable(numeroDeTable);
        return this;
    }

    public void setNumeroDeTable(String numeroDeTable) {
        this.numeroDeTable = numeroDeTable;
    }

    public String getSerie() {
        return this.serie;
    }

    public Depot serie(String serie) {
        this.setSerie(serie);
        return this;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public byte[] getDiplome() {
        return this.diplome;
    }

    public Depot diplome(byte[] diplome) {
        this.setDiplome(diplome);
        return this;
    }

    public void setDiplome(byte[] diplome) {
        this.diplome = diplome;
    }

    public String getDiplomeContentType() {
        return this.diplomeContentType;
    }

    public Depot diplomeContentType(String diplomeContentType) {
        this.diplomeContentType = diplomeContentType;
        return this;
    }

    public void setDiplomeContentType(String diplomeContentType) {
        this.diplomeContentType = diplomeContentType;
    }

    public byte[] getReleveDeNote() {
        return this.releveDeNote;
    }

    public Depot releveDeNote(byte[] releveDeNote) {
        this.setReleveDeNote(releveDeNote);
        return this;
    }

    public void setReleveDeNote(byte[] releveDeNote) {
        this.releveDeNote = releveDeNote;
    }

    public String getReleveDeNoteContentType() {
        return this.releveDeNoteContentType;
    }

    public Depot releveDeNoteContentType(String releveDeNoteContentType) {
        this.releveDeNoteContentType = releveDeNoteContentType;
        return this;
    }

    public void setReleveDeNoteContentType(String releveDeNoteContentType) {
        this.releveDeNoteContentType = releveDeNoteContentType;
    }

    public String getAnneeObtention() {
        return this.anneeObtention;
    }

    public Depot anneeObtention(String anneeObtention) {
        this.setAnneeObtention(anneeObtention);
        return this;
    }

    public void setAnneeObtention(String anneeObtention) {
        this.anneeObtention = anneeObtention;
    }

    public String getLieuObtention() {
        return this.lieuObtention;
    }

    public Depot lieuObtention(String lieuObtention) {
        this.setLieuObtention(lieuObtention);
        return this;
    }

    public void setLieuObtention(String lieuObtention) {
        this.lieuObtention = lieuObtention;
    }

    public String getMention() {
        return this.mention;
    }

    public Depot mention(String mention) {
        this.setMention(mention);
        return this;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }

    public byte[] getLettreDeMotivation() {
        return this.lettreDeMotivation;
    }

    public Depot lettreDeMotivation(byte[] lettreDeMotivation) {
        this.setLettreDeMotivation(lettreDeMotivation);
        return this;
    }

    public void setLettreDeMotivation(byte[] lettreDeMotivation) {
        this.lettreDeMotivation = lettreDeMotivation;
    }

    public String getLettreDeMotivationContentType() {
        return this.lettreDeMotivationContentType;
    }

    public Depot lettreDeMotivationContentType(String lettreDeMotivationContentType) {
        this.lettreDeMotivationContentType = lettreDeMotivationContentType;
        return this;
    }

    public void setLettreDeMotivationContentType(String lettreDeMotivationContentType) {
        this.lettreDeMotivationContentType = lettreDeMotivationContentType;
    }

    public String getChoix1() {
        return this.choix1;
    }

    public Depot choix1(String choix1) {
        this.setChoix1(choix1);
        return this;
    }

    public void setChoix1(String choix1) {
        this.choix1 = choix1;
    }

    public String getChoix2() {
        return this.choix2;
    }

    public Depot choix2(String choix2) {
        this.setChoix2(choix2);
        return this;
    }

    public void setChoix2(String choix2) {
        this.choix2 = choix2;
    }

    public String getChoix3() {
        return this.choix3;
    }

    public Depot choix3(String choix3) {
        this.setChoix3(choix3);
        return this;
    }

    public void setChoix3(String choix3) {
        this.choix3 = choix3;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Depot photo(byte[] photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Depot photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Bachelier getBachelier() {
        return this.bachelier;
    }

    public void setBachelier(Bachelier bachelier) {
        this.bachelier = bachelier;
    }

    public Depot bachelier(Bachelier bachelier) {
        this.setBachelier(bachelier);
        return this;
    }

    public Dossier getDossier() {
        return this.dossier;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    public Depot dossier(Dossier dossier) {
        this.setDossier(dossier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Depot)) {
            return false;
        }
        return id != null && id.equals(((Depot) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Depot{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", dateNaissance='" + getDateNaissance() + "'" +
            ", lieuDeNaissance='" + getLieuDeNaissance() + "'" +
            ", email='" + getEmail() + "'" +
            ", nationalite='" + getNationalite() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", dateDepot='" + getDateDepot() + "'" +
            ", numeroDeTable='" + getNumeroDeTable() + "'" +
            ", serie='" + getSerie() + "'" +
            ", diplome='" + getDiplome() + "'" +
            ", diplomeContentType='" + getDiplomeContentType() + "'" +
            ", releveDeNote='" + getReleveDeNote() + "'" +
            ", releveDeNoteContentType='" + getReleveDeNoteContentType() + "'" +
            ", anneeObtention='" + getAnneeObtention() + "'" +
            ", lieuObtention='" + getLieuObtention() + "'" +
            ", mention='" + getMention() + "'" +
            ", lettreDeMotivation='" + getLettreDeMotivation() + "'" +
            ", lettreDeMotivationContentType='" + getLettreDeMotivationContentType() + "'" +
            ", choix1='" + getChoix1() + "'" +
            ", choix2='" + getChoix2() + "'" +
            ", choix3='" + getChoix3() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            "}";
    }
}
