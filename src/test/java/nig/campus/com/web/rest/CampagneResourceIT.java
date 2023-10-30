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
import nig.campus.com.domain.Campagne;
import nig.campus.com.repository.CampagneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CampagneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CampagneResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/campagnes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CampagneRepository campagneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCampagneMockMvc;

    private Campagne campagne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campagne createEntity(EntityManager em) {
        Campagne campagne = new Campagne().intitule(DEFAULT_INTITULE).dateDebut(DEFAULT_DATE_DEBUT).dateFin(DEFAULT_DATE_FIN);
        return campagne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campagne createUpdatedEntity(EntityManager em) {
        Campagne campagne = new Campagne().intitule(UPDATED_INTITULE).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);
        return campagne;
    }

    @BeforeEach
    public void initTest() {
        campagne = createEntity(em);
    }

    @Test
    @Transactional
    void createCampagne() throws Exception {
        int databaseSizeBeforeCreate = campagneRepository.findAll().size();
        // Create the Campagne
        restCampagneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(campagne)))
            .andExpect(status().isCreated());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeCreate + 1);
        Campagne testCampagne = campagneList.get(campagneList.size() - 1);
        assertThat(testCampagne.getIntitule()).isEqualTo(DEFAULT_INTITULE);
        assertThat(testCampagne.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testCampagne.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void createCampagneWithExistingId() throws Exception {
        // Create the Campagne with an existing ID
        campagne.setId(1L);

        int databaseSizeBeforeCreate = campagneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampagneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(campagne)))
            .andExpect(status().isBadRequest());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCampagnes() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);

        // Get all the campagneList
        restCampagneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campagne.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(sameInstant(DEFAULT_DATE_DEBUT))))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(sameInstant(DEFAULT_DATE_FIN))));
    }

    @Test
    @Transactional
    void getCampagne() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);

        // Get the campagne
        restCampagneMockMvc
            .perform(get(ENTITY_API_URL_ID, campagne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(campagne.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.dateDebut").value(sameInstant(DEFAULT_DATE_DEBUT)))
            .andExpect(jsonPath("$.dateFin").value(sameInstant(DEFAULT_DATE_FIN)));
    }

    @Test
    @Transactional
    void getNonExistingCampagne() throws Exception {
        // Get the campagne
        restCampagneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCampagne() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);

        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();

        // Update the campagne
        Campagne updatedCampagne = campagneRepository.findById(campagne.getId()).get();
        // Disconnect from session so that the updates on updatedCampagne are not directly saved in db
        em.detach(updatedCampagne);
        updatedCampagne.intitule(UPDATED_INTITULE).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restCampagneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCampagne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCampagne))
            )
            .andExpect(status().isOk());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
        Campagne testCampagne = campagneList.get(campagneList.size() - 1);
        assertThat(testCampagne.getIntitule()).isEqualTo(UPDATED_INTITULE);
        assertThat(testCampagne.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testCampagne.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void putNonExistingCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        campagne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampagneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, campagne.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campagne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        campagne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampagneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(campagne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        campagne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampagneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(campagne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCampagneWithPatch() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);

        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();

        // Update the campagne using partial update
        Campagne partialUpdatedCampagne = new Campagne();
        partialUpdatedCampagne.setId(campagne.getId());

        partialUpdatedCampagne.intitule(UPDATED_INTITULE).dateFin(UPDATED_DATE_FIN);

        restCampagneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCampagne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCampagne))
            )
            .andExpect(status().isOk());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
        Campagne testCampagne = campagneList.get(campagneList.size() - 1);
        assertThat(testCampagne.getIntitule()).isEqualTo(UPDATED_INTITULE);
        assertThat(testCampagne.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testCampagne.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void fullUpdateCampagneWithPatch() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);

        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();

        // Update the campagne using partial update
        Campagne partialUpdatedCampagne = new Campagne();
        partialUpdatedCampagne.setId(campagne.getId());

        partialUpdatedCampagne.intitule(UPDATED_INTITULE).dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN);

        restCampagneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCampagne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCampagne))
            )
            .andExpect(status().isOk());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
        Campagne testCampagne = campagneList.get(campagneList.size() - 1);
        assertThat(testCampagne.getIntitule()).isEqualTo(UPDATED_INTITULE);
        assertThat(testCampagne.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testCampagne.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        campagne.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampagneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, campagne.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(campagne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        campagne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampagneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(campagne))
            )
            .andExpect(status().isBadRequest());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCampagne() throws Exception {
        int databaseSizeBeforeUpdate = campagneRepository.findAll().size();
        campagne.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCampagneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(campagne)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Campagne in the database
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCampagne() throws Exception {
        // Initialize the database
        campagneRepository.saveAndFlush(campagne);

        int databaseSizeBeforeDelete = campagneRepository.findAll().size();

        // Delete the campagne
        restCampagneMockMvc
            .perform(delete(ENTITY_API_URL_ID, campagne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Campagne> campagneList = campagneRepository.findAll();
        assertThat(campagneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
