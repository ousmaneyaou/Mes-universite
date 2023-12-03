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
import nig.campus.com.domain.Inscription;
import nig.campus.com.repository.InscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link InscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InscriptionResourceIT {

    private static final ZonedDateTime DEFAULT_DATE_INSCRIPTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_INSCRIPTION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_REGIME = false;
    private static final Boolean UPDATED_REGIME = true;

    private static final String DEFAULT_ANNEE_ACADEMIQUE = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE_ACADEMIQUE = "BBBBBBBBBB";

    private static final Integer DEFAULT_MONTANT_INSCRIPTION = 1;
    private static final Integer UPDATED_MONTANT_INSCRIPTION = 2;

    private static final String DEFAULT_NIVEAU = "AAAAAAAAAA";
    private static final String UPDATED_NIVEAU = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/inscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InscriptionRepository inscriptionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInscriptionMockMvc;

    private Inscription inscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createEntity(EntityManager em) {
        Inscription inscription = new Inscription()
            .dateInscription(DEFAULT_DATE_INSCRIPTION)
            .regime(DEFAULT_REGIME)
            .anneeAcademique(DEFAULT_ANNEE_ACADEMIQUE)
            .montantInscription(DEFAULT_MONTANT_INSCRIPTION)
            .niveau(DEFAULT_NIVEAU)
            .observation(DEFAULT_OBSERVATION);
        return inscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inscription createUpdatedEntity(EntityManager em) {
        Inscription inscription = new Inscription()
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .regime(UPDATED_REGIME)
            .anneeAcademique(UPDATED_ANNEE_ACADEMIQUE)
            .montantInscription(UPDATED_MONTANT_INSCRIPTION)
            .niveau(UPDATED_NIVEAU)
            .observation(UPDATED_OBSERVATION);
        return inscription;
    }

    @BeforeEach
    public void initTest() {
        inscription = createEntity(em);
    }

    @Test
    @Transactional
    void createInscription() throws Exception {
        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();
        // Create the Inscription
        restInscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscription)))
            .andExpect(status().isCreated());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getDateInscription()).isEqualTo(DEFAULT_DATE_INSCRIPTION);
        assertThat(testInscription.getRegime()).isEqualTo(DEFAULT_REGIME);
        assertThat(testInscription.getAnneeAcademique()).isEqualTo(DEFAULT_ANNEE_ACADEMIQUE);
        assertThat(testInscription.getMontantInscription()).isEqualTo(DEFAULT_MONTANT_INSCRIPTION);
        assertThat(testInscription.getNiveau()).isEqualTo(DEFAULT_NIVEAU);
        assertThat(testInscription.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void createInscriptionWithExistingId() throws Exception {
        // Create the Inscription with an existing ID
        inscription.setId(1L);

        int databaseSizeBeforeCreate = inscriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscription)))
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInscriptions() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get all the inscriptionList
        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(sameInstant(DEFAULT_DATE_INSCRIPTION))))
            .andExpect(jsonPath("$.[*].regime").value(hasItem(DEFAULT_REGIME.booleanValue())))
            .andExpect(jsonPath("$.[*].anneeAcademique").value(hasItem(DEFAULT_ANNEE_ACADEMIQUE)))
            .andExpect(jsonPath("$.[*].montantInscription").value(hasItem(DEFAULT_MONTANT_INSCRIPTION)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }

    @Test
    @Transactional
    void getInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        // Get the inscription
        restInscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, inscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inscription.getId().intValue()))
            .andExpect(jsonPath("$.dateInscription").value(sameInstant(DEFAULT_DATE_INSCRIPTION)))
            .andExpect(jsonPath("$.regime").value(DEFAULT_REGIME.booleanValue()))
            .andExpect(jsonPath("$.anneeAcademique").value(DEFAULT_ANNEE_ACADEMIQUE))
            .andExpect(jsonPath("$.montantInscription").value(DEFAULT_MONTANT_INSCRIPTION))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }

    @Test
    @Transactional
    void getNonExistingInscription() throws Exception {
        // Get the inscription
        restInscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription
        Inscription updatedInscription = inscriptionRepository.findById(inscription.getId()).get();
        // Disconnect from session so that the updates on updatedInscription are not directly saved in db
        em.detach(updatedInscription);
        updatedInscription
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .regime(UPDATED_REGIME)
            .anneeAcademique(UPDATED_ANNEE_ACADEMIQUE)
            .montantInscription(UPDATED_MONTANT_INSCRIPTION)
            .niveau(UPDATED_NIVEAU)
            .observation(UPDATED_OBSERVATION);

        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInscription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInscription))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getDateInscription()).isEqualTo(UPDATED_DATE_INSCRIPTION);
        assertThat(testInscription.getRegime()).isEqualTo(UPDATED_REGIME);
        assertThat(testInscription.getAnneeAcademique()).isEqualTo(UPDATED_ANNEE_ACADEMIQUE);
        assertThat(testInscription.getMontantInscription()).isEqualTo(UPDATED_MONTANT_INSCRIPTION);
        assertThat(testInscription.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testInscription.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void putNonExistingInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inscription.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inscription)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInscriptionWithPatch() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription using partial update
        Inscription partialUpdatedInscription = new Inscription();
        partialUpdatedInscription.setId(inscription.getId());

        partialUpdatedInscription
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .anneeAcademique(UPDATED_ANNEE_ACADEMIQUE)
            .montantInscription(UPDATED_MONTANT_INSCRIPTION)
            .niveau(UPDATED_NIVEAU);

        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscription))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getDateInscription()).isEqualTo(UPDATED_DATE_INSCRIPTION);
        assertThat(testInscription.getRegime()).isEqualTo(DEFAULT_REGIME);
        assertThat(testInscription.getAnneeAcademique()).isEqualTo(UPDATED_ANNEE_ACADEMIQUE);
        assertThat(testInscription.getMontantInscription()).isEqualTo(UPDATED_MONTANT_INSCRIPTION);
        assertThat(testInscription.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testInscription.getObservation()).isEqualTo(DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void fullUpdateInscriptionWithPatch() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();

        // Update the inscription using partial update
        Inscription partialUpdatedInscription = new Inscription();
        partialUpdatedInscription.setId(inscription.getId());

        partialUpdatedInscription
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .regime(UPDATED_REGIME)
            .anneeAcademique(UPDATED_ANNEE_ACADEMIQUE)
            .montantInscription(UPDATED_MONTANT_INSCRIPTION)
            .niveau(UPDATED_NIVEAU)
            .observation(UPDATED_OBSERVATION);

        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInscription))
            )
            .andExpect(status().isOk());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
        Inscription testInscription = inscriptionList.get(inscriptionList.size() - 1);
        assertThat(testInscription.getDateInscription()).isEqualTo(UPDATED_DATE_INSCRIPTION);
        assertThat(testInscription.getRegime()).isEqualTo(UPDATED_REGIME);
        assertThat(testInscription.getAnneeAcademique()).isEqualTo(UPDATED_ANNEE_ACADEMIQUE);
        assertThat(testInscription.getMontantInscription()).isEqualTo(UPDATED_MONTANT_INSCRIPTION);
        assertThat(testInscription.getNiveau()).isEqualTo(UPDATED_NIVEAU);
        assertThat(testInscription.getObservation()).isEqualTo(UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void patchNonExistingInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inscription))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInscription() throws Exception {
        int databaseSizeBeforeUpdate = inscriptionRepository.findAll().size();
        inscription.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(inscription))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inscription in the database
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInscription() throws Exception {
        // Initialize the database
        inscriptionRepository.saveAndFlush(inscription);

        int databaseSizeBeforeDelete = inscriptionRepository.findAll().size();

        // Delete the inscription
        restInscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, inscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inscription> inscriptionList = inscriptionRepository.findAll();
        assertThat(inscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
