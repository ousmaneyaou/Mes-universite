package nig.campus.com.web.rest;

import static nig.campus.com.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import nig.campus.com.IntegrationTest;
import nig.campus.com.domain.Depot;
import nig.campus.com.domain.enumeration.EnumSexe;
import nig.campus.com.repository.DepotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DepotResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DepotResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_NAISSANCE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_NAISSANCE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LIEU_DE_NAISSANCE = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_DE_NAISSANCE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITE = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final EnumSexe DEFAULT_SEXE = EnumSexe.MASCULIN;
    private static final EnumSexe UPDATED_SEXE = EnumSexe.FEMININ;

    private static final ZonedDateTime DEFAULT_DATE_DEPOT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEPOT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_NUMERO_DE_TABLE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_DE_TABLE = "BBBBBBBBBB";

    private static final String DEFAULT_SERIE = "AAAAAAAAAA";
    private static final String UPDATED_SERIE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_DIPLOME = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DIPLOME = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DIPLOME_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DIPLOME_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_RELEVE_DE_NOTE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_RELEVE_DE_NOTE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_RELEVE_DE_NOTE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_RELEVE_DE_NOTE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ANNEE_OBTENTION = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE_OBTENTION = "BBBBBBBBBB";

    private static final String DEFAULT_LIEU_OBTENTION = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_OBTENTION = "BBBBBBBBBB";

    private static final String DEFAULT_MENTION = "AAAAAAAAAA";
    private static final String UPDATED_MENTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LETTRE_DE_MOTIVATION = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LETTRE_DE_MOTIVATION = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LETTRE_DE_MOTIVATION_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LETTRE_DE_MOTIVATION_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CHOIX_1 = "AAAAAAAAAA";
    private static final String UPDATED_CHOIX_1 = "BBBBBBBBBB";

    private static final String DEFAULT_CHOIX_2 = "AAAAAAAAAA";
    private static final String UPDATED_CHOIX_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CHOIX_3 = "AAAAAAAAAA";
    private static final String UPDATED_CHOIX_3 = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/depots";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepotRepository depotRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepotMockMvc;

    private Depot depot;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Depot createEntity(EntityManager em) {
        Depot depot = new Depot()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .lieuDeNaissance(DEFAULT_LIEU_DE_NAISSANCE)
            .email(DEFAULT_EMAIL)
            .nationalite(DEFAULT_NATIONALITE)
            .telephone(DEFAULT_TELEPHONE)
            .sexe(DEFAULT_SEXE)
            .dateDepot(DEFAULT_DATE_DEPOT)
            .numeroDeTable(DEFAULT_NUMERO_DE_TABLE)
            .serie(DEFAULT_SERIE)
            .diplome(DEFAULT_DIPLOME)
            .diplomeContentType(DEFAULT_DIPLOME_CONTENT_TYPE)
            .releveDeNote(DEFAULT_RELEVE_DE_NOTE)
            .releveDeNoteContentType(DEFAULT_RELEVE_DE_NOTE_CONTENT_TYPE)
            .anneeObtention(DEFAULT_ANNEE_OBTENTION)
            .lieuObtention(DEFAULT_LIEU_OBTENTION)
            .mention(DEFAULT_MENTION)
            .lettreDeMotivation(DEFAULT_LETTRE_DE_MOTIVATION)
            .lettreDeMotivationContentType(DEFAULT_LETTRE_DE_MOTIVATION_CONTENT_TYPE)
            .choix1(DEFAULT_CHOIX_1)
            .choix2(DEFAULT_CHOIX_2)
            .choix3(DEFAULT_CHOIX_3)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        return depot;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Depot createUpdatedEntity(EntityManager em) {
        Depot depot = new Depot()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuDeNaissance(UPDATED_LIEU_DE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .nationalite(UPDATED_NATIONALITE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .numeroDeTable(UPDATED_NUMERO_DE_TABLE)
            .serie(UPDATED_SERIE)
            .diplome(UPDATED_DIPLOME)
            .diplomeContentType(UPDATED_DIPLOME_CONTENT_TYPE)
            .releveDeNote(UPDATED_RELEVE_DE_NOTE)
            .releveDeNoteContentType(UPDATED_RELEVE_DE_NOTE_CONTENT_TYPE)
            .anneeObtention(UPDATED_ANNEE_OBTENTION)
            .lieuObtention(UPDATED_LIEU_OBTENTION)
            .mention(UPDATED_MENTION)
            .lettreDeMotivation(UPDATED_LETTRE_DE_MOTIVATION)
            .lettreDeMotivationContentType(UPDATED_LETTRE_DE_MOTIVATION_CONTENT_TYPE)
            .choix1(UPDATED_CHOIX_1)
            .choix2(UPDATED_CHOIX_2)
            .choix3(UPDATED_CHOIX_3)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        return depot;
    }

    @BeforeEach
    public void initTest() {
        depot = createEntity(em);
    }

    @Test
    @Transactional
    void createDepot() throws Exception {
        int databaseSizeBeforeCreate = depotRepository.findAll().size();
        // Create the Depot
        restDepotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depot)))
            .andExpect(status().isCreated());

        // Validate the Depot in the database
        List<Depot> depotList = depotRepository.findAll();
        assertThat(depotList).hasSize(databaseSizeBeforeCreate + 1);
        Depot testDepot = depotList.get(depotList.size() - 1);
        assertThat(testDepot.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDepot.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testDepot.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testDepot.getLieuDeNaissance()).isEqualTo(DEFAULT_LIEU_DE_NAISSANCE);
        assertThat(testDepot.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDepot.getNationalite()).isEqualTo(DEFAULT_NATIONALITE);
        assertThat(testDepot.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testDepot.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testDepot.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testDepot.getNumeroDeTable()).isEqualTo(DEFAULT_NUMERO_DE_TABLE);
        assertThat(testDepot.getSerie()).isEqualTo(DEFAULT_SERIE);
        assertThat(testDepot.getDiplome()).isEqualTo(DEFAULT_DIPLOME);
        assertThat(testDepot.getDiplomeContentType()).isEqualTo(DEFAULT_DIPLOME_CONTENT_TYPE);
        assertThat(testDepot.getReleveDeNote()).isEqualTo(DEFAULT_RELEVE_DE_NOTE);
        assertThat(testDepot.getReleveDeNoteContentType()).isEqualTo(DEFAULT_RELEVE_DE_NOTE_CONTENT_TYPE);
        assertThat(testDepot.getAnneeObtention()).isEqualTo(DEFAULT_ANNEE_OBTENTION);
        assertThat(testDepot.getLieuObtention()).isEqualTo(DEFAULT_LIEU_OBTENTION);
        assertThat(testDepot.getMention()).isEqualTo(DEFAULT_MENTION);
        assertThat(testDepot.getLettreDeMotivation()).isEqualTo(DEFAULT_LETTRE_DE_MOTIVATION);
        assertThat(testDepot.getLettreDeMotivationContentType()).isEqualTo(DEFAULT_LETTRE_DE_MOTIVATION_CONTENT_TYPE);
        assertThat(testDepot.getChoix1()).isEqualTo(DEFAULT_CHOIX_1);
        assertThat(testDepot.getChoix2()).isEqualTo(DEFAULT_CHOIX_2);
        assertThat(testDepot.getChoix3()).isEqualTo(DEFAULT_CHOIX_3);
        assertThat(testDepot.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testDepot.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createDepotWithExistingId() throws Exception {
        // Create the Depot with an existing ID
        depot.setId(1L);

        int databaseSizeBeforeCreate = depotRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepotMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depot)))
            .andExpect(status().isBadRequest());

        // Validate the Depot in the database
        List<Depot> depotList = depotRepository.findAll();
        assertThat(depotList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepots() throws Exception {
        // Initialize the database
        depotRepository.saveAndFlush(depot);

        // Get all the depotList
        restDepotMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depot.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(sameInstant(DEFAULT_DATE_NAISSANCE))))
            .andExpect(jsonPath("$.[*].lieuDeNaissance").value(hasItem(DEFAULT_LIEU_DE_NAISSANCE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].dateDepot").value(hasItem(sameInstant(DEFAULT_DATE_DEPOT))))
            .andExpect(jsonPath("$.[*].numeroDeTable").value(hasItem(DEFAULT_NUMERO_DE_TABLE)))
            .andExpect(jsonPath("$.[*].serie").value(hasItem(DEFAULT_SERIE)))
            .andExpect(jsonPath("$.[*].diplomeContentType").value(hasItem(DEFAULT_DIPLOME_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].diplome").value(hasItem(Base64Utils.encodeToString(DEFAULT_DIPLOME))))
            .andExpect(jsonPath("$.[*].releveDeNoteContentType").value(hasItem(DEFAULT_RELEVE_DE_NOTE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].releveDeNote").value(hasItem(Base64Utils.encodeToString(DEFAULT_RELEVE_DE_NOTE))))
            .andExpect(jsonPath("$.[*].anneeObtention").value(hasItem(DEFAULT_ANNEE_OBTENTION)))
            .andExpect(jsonPath("$.[*].lieuObtention").value(hasItem(DEFAULT_LIEU_OBTENTION)))
            .andExpect(jsonPath("$.[*].mention").value(hasItem(DEFAULT_MENTION)))
            .andExpect(jsonPath("$.[*].lettreDeMotivationContentType").value(hasItem(DEFAULT_LETTRE_DE_MOTIVATION_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].lettreDeMotivation").value(hasItem(Base64Utils.encodeToString(DEFAULT_LETTRE_DE_MOTIVATION))))
            .andExpect(jsonPath("$.[*].choix1").value(hasItem(DEFAULT_CHOIX_1)))
            .andExpect(jsonPath("$.[*].choix2").value(hasItem(DEFAULT_CHOIX_2)))
            .andExpect(jsonPath("$.[*].choix3").value(hasItem(DEFAULT_CHOIX_3)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))));
    }

    @Test
    @Transactional
    void getDepot() throws Exception {
        // Initialize the database
        depotRepository.saveAndFlush(depot);

        // Get the depot
        restDepotMockMvc
            .perform(get(ENTITY_API_URL_ID, depot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depot.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.dateNaissance").value(sameInstant(DEFAULT_DATE_NAISSANCE)))
            .andExpect(jsonPath("$.lieuDeNaissance").value(DEFAULT_LIEU_DE_NAISSANCE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.nationalite").value(DEFAULT_NATIONALITE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.dateDepot").value(sameInstant(DEFAULT_DATE_DEPOT)))
            .andExpect(jsonPath("$.numeroDeTable").value(DEFAULT_NUMERO_DE_TABLE))
            .andExpect(jsonPath("$.serie").value(DEFAULT_SERIE))
            .andExpect(jsonPath("$.diplomeContentType").value(DEFAULT_DIPLOME_CONTENT_TYPE))
            .andExpect(jsonPath("$.diplome").value(Base64Utils.encodeToString(DEFAULT_DIPLOME)))
            .andExpect(jsonPath("$.releveDeNoteContentType").value(DEFAULT_RELEVE_DE_NOTE_CONTENT_TYPE))
            .andExpect(jsonPath("$.releveDeNote").value(Base64Utils.encodeToString(DEFAULT_RELEVE_DE_NOTE)))
            .andExpect(jsonPath("$.anneeObtention").value(DEFAULT_ANNEE_OBTENTION))
            .andExpect(jsonPath("$.lieuObtention").value(DEFAULT_LIEU_OBTENTION))
            .andExpect(jsonPath("$.mention").value(DEFAULT_MENTION))
            .andExpect(jsonPath("$.lettreDeMotivationContentType").value(DEFAULT_LETTRE_DE_MOTIVATION_CONTENT_TYPE))
            .andExpect(jsonPath("$.lettreDeMotivation").value(Base64Utils.encodeToString(DEFAULT_LETTRE_DE_MOTIVATION)))
            .andExpect(jsonPath("$.choix1").value(DEFAULT_CHOIX_1))
            .andExpect(jsonPath("$.choix2").value(DEFAULT_CHOIX_2))
            .andExpect(jsonPath("$.choix3").value(DEFAULT_CHOIX_3))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)));
    }

    @Test
    @Transactional
    void getNonExistingDepot() throws Exception {
        // Get the depot
        restDepotMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepot() throws Exception {
        // Initialize the database
        depotRepository.saveAndFlush(depot);

        int databaseSizeBeforeUpdate = depotRepository.findAll().size();

        // Update the depot
        Depot updatedDepot = depotRepository.findById(depot.getId()).get();
        // Disconnect from session so that the updates on updatedDepot are not directly saved in db
        em.detach(updatedDepot);
        updatedDepot
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuDeNaissance(UPDATED_LIEU_DE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .nationalite(UPDATED_NATIONALITE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .numeroDeTable(UPDATED_NUMERO_DE_TABLE)
            .serie(UPDATED_SERIE)
            .diplome(UPDATED_DIPLOME)
            .diplomeContentType(UPDATED_DIPLOME_CONTENT_TYPE)
            .releveDeNote(UPDATED_RELEVE_DE_NOTE)
            .releveDeNoteContentType(UPDATED_RELEVE_DE_NOTE_CONTENT_TYPE)
            .anneeObtention(UPDATED_ANNEE_OBTENTION)
            .lieuObtention(UPDATED_LIEU_OBTENTION)
            .mention(UPDATED_MENTION)
            .lettreDeMotivation(UPDATED_LETTRE_DE_MOTIVATION)
            .lettreDeMotivationContentType(UPDATED_LETTRE_DE_MOTIVATION_CONTENT_TYPE)
            .choix1(UPDATED_CHOIX_1)
            .choix2(UPDATED_CHOIX_2)
            .choix3(UPDATED_CHOIX_3)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restDepotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDepot.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDepot))
            )
            .andExpect(status().isOk());

        // Validate the Depot in the database
        List<Depot> depotList = depotRepository.findAll();
        assertThat(depotList).hasSize(databaseSizeBeforeUpdate);
        Depot testDepot = depotList.get(depotList.size() - 1);
        assertThat(testDepot.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDepot.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDepot.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testDepot.getLieuDeNaissance()).isEqualTo(UPDATED_LIEU_DE_NAISSANCE);
        assertThat(testDepot.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDepot.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testDepot.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testDepot.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDepot.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testDepot.getNumeroDeTable()).isEqualTo(UPDATED_NUMERO_DE_TABLE);
        assertThat(testDepot.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testDepot.getDiplome()).isEqualTo(UPDATED_DIPLOME);
        assertThat(testDepot.getDiplomeContentType()).isEqualTo(UPDATED_DIPLOME_CONTENT_TYPE);
        assertThat(testDepot.getReleveDeNote()).isEqualTo(UPDATED_RELEVE_DE_NOTE);
        assertThat(testDepot.getReleveDeNoteContentType()).isEqualTo(UPDATED_RELEVE_DE_NOTE_CONTENT_TYPE);
        assertThat(testDepot.getAnneeObtention()).isEqualTo(UPDATED_ANNEE_OBTENTION);
        assertThat(testDepot.getLieuObtention()).isEqualTo(UPDATED_LIEU_OBTENTION);
        assertThat(testDepot.getMention()).isEqualTo(UPDATED_MENTION);
        assertThat(testDepot.getLettreDeMotivation()).isEqualTo(UPDATED_LETTRE_DE_MOTIVATION);
        assertThat(testDepot.getLettreDeMotivationContentType()).isEqualTo(UPDATED_LETTRE_DE_MOTIVATION_CONTENT_TYPE);
        assertThat(testDepot.getChoix1()).isEqualTo(UPDATED_CHOIX_1);
        assertThat(testDepot.getChoix2()).isEqualTo(UPDATED_CHOIX_2);
        assertThat(testDepot.getChoix3()).isEqualTo(UPDATED_CHOIX_3);
        assertThat(testDepot.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testDepot.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDepot() throws Exception {
        int databaseSizeBeforeUpdate = depotRepository.findAll().size();
        depot.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depot.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depot in the database
        List<Depot> depotList = depotRepository.findAll();
        assertThat(depotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepot() throws Exception {
        int databaseSizeBeforeUpdate = depotRepository.findAll().size();
        depot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepotMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(depot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depot in the database
        List<Depot> depotList = depotRepository.findAll();
        assertThat(depotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepot() throws Exception {
        int databaseSizeBeforeUpdate = depotRepository.findAll().size();
        depot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepotMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(depot)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Depot in the database
        List<Depot> depotList = depotRepository.findAll();
        assertThat(depotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepotWithPatch() throws Exception {
        // Initialize the database
        depotRepository.saveAndFlush(depot);

        int databaseSizeBeforeUpdate = depotRepository.findAll().size();

        // Update the depot using partial update
        Depot partialUpdatedDepot = new Depot();
        partialUpdatedDepot.setId(depot.getId());

        partialUpdatedDepot
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .nationalite(UPDATED_NATIONALITE)
            .sexe(UPDATED_SEXE)
            .anneeObtention(UPDATED_ANNEE_OBTENTION)
            .lieuObtention(UPDATED_LIEU_OBTENTION)
            .mention(UPDATED_MENTION)
            .choix1(UPDATED_CHOIX_1)
            .choix2(UPDATED_CHOIX_2)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restDepotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepot))
            )
            .andExpect(status().isOk());

        // Validate the Depot in the database
        List<Depot> depotList = depotRepository.findAll();
        assertThat(depotList).hasSize(databaseSizeBeforeUpdate);
        Depot testDepot = depotList.get(depotList.size() - 1);
        assertThat(testDepot.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDepot.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDepot.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testDepot.getLieuDeNaissance()).isEqualTo(DEFAULT_LIEU_DE_NAISSANCE);
        assertThat(testDepot.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDepot.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testDepot.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testDepot.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDepot.getDateDepot()).isEqualTo(DEFAULT_DATE_DEPOT);
        assertThat(testDepot.getNumeroDeTable()).isEqualTo(DEFAULT_NUMERO_DE_TABLE);
        assertThat(testDepot.getSerie()).isEqualTo(DEFAULT_SERIE);
        assertThat(testDepot.getDiplome()).isEqualTo(DEFAULT_DIPLOME);
        assertThat(testDepot.getDiplomeContentType()).isEqualTo(DEFAULT_DIPLOME_CONTENT_TYPE);
        assertThat(testDepot.getReleveDeNote()).isEqualTo(DEFAULT_RELEVE_DE_NOTE);
        assertThat(testDepot.getReleveDeNoteContentType()).isEqualTo(DEFAULT_RELEVE_DE_NOTE_CONTENT_TYPE);
        assertThat(testDepot.getAnneeObtention()).isEqualTo(UPDATED_ANNEE_OBTENTION);
        assertThat(testDepot.getLieuObtention()).isEqualTo(UPDATED_LIEU_OBTENTION);
        assertThat(testDepot.getMention()).isEqualTo(UPDATED_MENTION);
        assertThat(testDepot.getLettreDeMotivation()).isEqualTo(DEFAULT_LETTRE_DE_MOTIVATION);
        assertThat(testDepot.getLettreDeMotivationContentType()).isEqualTo(DEFAULT_LETTRE_DE_MOTIVATION_CONTENT_TYPE);
        assertThat(testDepot.getChoix1()).isEqualTo(UPDATED_CHOIX_1);
        assertThat(testDepot.getChoix2()).isEqualTo(UPDATED_CHOIX_2);
        assertThat(testDepot.getChoix3()).isEqualTo(DEFAULT_CHOIX_3);
        assertThat(testDepot.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testDepot.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDepotWithPatch() throws Exception {
        // Initialize the database
        depotRepository.saveAndFlush(depot);

        int databaseSizeBeforeUpdate = depotRepository.findAll().size();

        // Update the depot using partial update
        Depot partialUpdatedDepot = new Depot();
        partialUpdatedDepot.setId(depot.getId());

        partialUpdatedDepot
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuDeNaissance(UPDATED_LIEU_DE_NAISSANCE)
            .email(UPDATED_EMAIL)
            .nationalite(UPDATED_NATIONALITE)
            .telephone(UPDATED_TELEPHONE)
            .sexe(UPDATED_SEXE)
            .dateDepot(UPDATED_DATE_DEPOT)
            .numeroDeTable(UPDATED_NUMERO_DE_TABLE)
            .serie(UPDATED_SERIE)
            .diplome(UPDATED_DIPLOME)
            .diplomeContentType(UPDATED_DIPLOME_CONTENT_TYPE)
            .releveDeNote(UPDATED_RELEVE_DE_NOTE)
            .releveDeNoteContentType(UPDATED_RELEVE_DE_NOTE_CONTENT_TYPE)
            .anneeObtention(UPDATED_ANNEE_OBTENTION)
            .lieuObtention(UPDATED_LIEU_OBTENTION)
            .mention(UPDATED_MENTION)
            .lettreDeMotivation(UPDATED_LETTRE_DE_MOTIVATION)
            .lettreDeMotivationContentType(UPDATED_LETTRE_DE_MOTIVATION_CONTENT_TYPE)
            .choix1(UPDATED_CHOIX_1)
            .choix2(UPDATED_CHOIX_2)
            .choix3(UPDATED_CHOIX_3)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restDepotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepot))
            )
            .andExpect(status().isOk());

        // Validate the Depot in the database
        List<Depot> depotList = depotRepository.findAll();
        assertThat(depotList).hasSize(databaseSizeBeforeUpdate);
        Depot testDepot = depotList.get(depotList.size() - 1);
        assertThat(testDepot.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDepot.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDepot.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testDepot.getLieuDeNaissance()).isEqualTo(UPDATED_LIEU_DE_NAISSANCE);
        assertThat(testDepot.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDepot.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testDepot.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testDepot.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testDepot.getDateDepot()).isEqualTo(UPDATED_DATE_DEPOT);
        assertThat(testDepot.getNumeroDeTable()).isEqualTo(UPDATED_NUMERO_DE_TABLE);
        assertThat(testDepot.getSerie()).isEqualTo(UPDATED_SERIE);
        assertThat(testDepot.getDiplome()).isEqualTo(UPDATED_DIPLOME);
        assertThat(testDepot.getDiplomeContentType()).isEqualTo(UPDATED_DIPLOME_CONTENT_TYPE);
        assertThat(testDepot.getReleveDeNote()).isEqualTo(UPDATED_RELEVE_DE_NOTE);
        assertThat(testDepot.getReleveDeNoteContentType()).isEqualTo(UPDATED_RELEVE_DE_NOTE_CONTENT_TYPE);
        assertThat(testDepot.getAnneeObtention()).isEqualTo(UPDATED_ANNEE_OBTENTION);
        assertThat(testDepot.getLieuObtention()).isEqualTo(UPDATED_LIEU_OBTENTION);
        assertThat(testDepot.getMention()).isEqualTo(UPDATED_MENTION);
        assertThat(testDepot.getLettreDeMotivation()).isEqualTo(UPDATED_LETTRE_DE_MOTIVATION);
        assertThat(testDepot.getLettreDeMotivationContentType()).isEqualTo(UPDATED_LETTRE_DE_MOTIVATION_CONTENT_TYPE);
        assertThat(testDepot.getChoix1()).isEqualTo(UPDATED_CHOIX_1);
        assertThat(testDepot.getChoix2()).isEqualTo(UPDATED_CHOIX_2);
        assertThat(testDepot.getChoix3()).isEqualTo(UPDATED_CHOIX_3);
        assertThat(testDepot.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testDepot.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDepot() throws Exception {
        int databaseSizeBeforeUpdate = depotRepository.findAll().size();
        depot.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depot.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depot in the database
        List<Depot> depotList = depotRepository.findAll();
        assertThat(depotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepot() throws Exception {
        int databaseSizeBeforeUpdate = depotRepository.findAll().size();
        depot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepotMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(depot))
            )
            .andExpect(status().isBadRequest());

        // Validate the Depot in the database
        List<Depot> depotList = depotRepository.findAll();
        assertThat(depotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepot() throws Exception {
        int databaseSizeBeforeUpdate = depotRepository.findAll().size();
        depot.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepotMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(depot)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Depot in the database
        List<Depot> depotList = depotRepository.findAll();
        assertThat(depotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepot() throws Exception {
        // Initialize the database
        depotRepository.saveAndFlush(depot);

        int databaseSizeBeforeDelete = depotRepository.findAll().size();

        // Delete the depot
        restDepotMockMvc
            .perform(delete(ENTITY_API_URL_ID, depot.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Depot> depotList = depotRepository.findAll();
        assertThat(depotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
