package nig.campus.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import nig.campus.com.IntegrationTest;
import nig.campus.com.domain.Bachelier;
import nig.campus.com.domain.enumeration.EnumSexe;
import nig.campus.com.repository.BachelierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BachelierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BachelierResourceIT {

    private static final EnumSexe DEFAULT_SEXE = EnumSexe.MASCULIN;
    private static final EnumSexe UPDATED_SEXE = EnumSexe.FEMININ;

    private static final Instant DEFAULT_DATE_NAISSANCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_NAISSANCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LIEU_NAISSANCE = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAISSANCE = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITE = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bacheliers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BachelierRepository bachelierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBachelierMockMvc;

    private Bachelier bachelier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bachelier createEntity(EntityManager em) {
        Bachelier bachelier = new Bachelier()
            .sexe(DEFAULT_SEXE)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .lieuNaissance(DEFAULT_LIEU_NAISSANCE)
            .nationalite(DEFAULT_NATIONALITE)
            .telephone(DEFAULT_TELEPHONE);
        return bachelier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bachelier createUpdatedEntity(EntityManager em) {
        Bachelier bachelier = new Bachelier()
            .sexe(UPDATED_SEXE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .nationalite(UPDATED_NATIONALITE)
            .telephone(UPDATED_TELEPHONE);
        return bachelier;
    }

    @BeforeEach
    public void initTest() {
        bachelier = createEntity(em);
    }

    @Test
    @Transactional
    void createBachelier() throws Exception {
        int databaseSizeBeforeCreate = bachelierRepository.findAll().size();
        // Create the Bachelier
        restBachelierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bachelier)))
            .andExpect(status().isCreated());

        // Validate the Bachelier in the database
        List<Bachelier> bachelierList = bachelierRepository.findAll();
        assertThat(bachelierList).hasSize(databaseSizeBeforeCreate + 1);
        Bachelier testBachelier = bachelierList.get(bachelierList.size() - 1);
        assertThat(testBachelier.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testBachelier.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testBachelier.getLieuNaissance()).isEqualTo(DEFAULT_LIEU_NAISSANCE);
        assertThat(testBachelier.getNationalite()).isEqualTo(DEFAULT_NATIONALITE);
        assertThat(testBachelier.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void createBachelierWithExistingId() throws Exception {
        // Create the Bachelier with an existing ID
        bachelier.setId(1L);

        int databaseSizeBeforeCreate = bachelierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBachelierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bachelier)))
            .andExpect(status().isBadRequest());

        // Validate the Bachelier in the database
        List<Bachelier> bachelierList = bachelierRepository.findAll();
        assertThat(bachelierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBacheliers() throws Exception {
        // Initialize the database
        bachelierRepository.saveAndFlush(bachelier);

        // Get all the bachelierList
        restBachelierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bachelier.getId().intValue())))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].lieuNaissance").value(hasItem(DEFAULT_LIEU_NAISSANCE)))
            .andExpect(jsonPath("$.[*].nationalite").value(hasItem(DEFAULT_NATIONALITE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    void getBachelier() throws Exception {
        // Initialize the database
        bachelierRepository.saveAndFlush(bachelier);

        // Get the bachelier
        restBachelierMockMvc
            .perform(get(ENTITY_API_URL_ID, bachelier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bachelier.getId().intValue()))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.lieuNaissance").value(DEFAULT_LIEU_NAISSANCE))
            .andExpect(jsonPath("$.nationalite").value(DEFAULT_NATIONALITE))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE));
    }

    @Test
    @Transactional
    void getNonExistingBachelier() throws Exception {
        // Get the bachelier
        restBachelierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBachelier() throws Exception {
        // Initialize the database
        bachelierRepository.saveAndFlush(bachelier);

        int databaseSizeBeforeUpdate = bachelierRepository.findAll().size();

        // Update the bachelier
        Bachelier updatedBachelier = bachelierRepository.findById(bachelier.getId()).get();
        // Disconnect from session so that the updates on updatedBachelier are not directly saved in db
        em.detach(updatedBachelier);
        updatedBachelier
            .sexe(UPDATED_SEXE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .nationalite(UPDATED_NATIONALITE)
            .telephone(UPDATED_TELEPHONE);

        restBachelierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBachelier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBachelier))
            )
            .andExpect(status().isOk());

        // Validate the Bachelier in the database
        List<Bachelier> bachelierList = bachelierRepository.findAll();
        assertThat(bachelierList).hasSize(databaseSizeBeforeUpdate);
        Bachelier testBachelier = bachelierList.get(bachelierList.size() - 1);
        assertThat(testBachelier.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testBachelier.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testBachelier.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testBachelier.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testBachelier.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void putNonExistingBachelier() throws Exception {
        int databaseSizeBeforeUpdate = bachelierRepository.findAll().size();
        bachelier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBachelierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bachelier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bachelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bachelier in the database
        List<Bachelier> bachelierList = bachelierRepository.findAll();
        assertThat(bachelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBachelier() throws Exception {
        int databaseSizeBeforeUpdate = bachelierRepository.findAll().size();
        bachelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBachelierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bachelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bachelier in the database
        List<Bachelier> bachelierList = bachelierRepository.findAll();
        assertThat(bachelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBachelier() throws Exception {
        int databaseSizeBeforeUpdate = bachelierRepository.findAll().size();
        bachelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBachelierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bachelier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bachelier in the database
        List<Bachelier> bachelierList = bachelierRepository.findAll();
        assertThat(bachelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBachelierWithPatch() throws Exception {
        // Initialize the database
        bachelierRepository.saveAndFlush(bachelier);

        int databaseSizeBeforeUpdate = bachelierRepository.findAll().size();

        // Update the bachelier using partial update
        Bachelier partialUpdatedBachelier = new Bachelier();
        partialUpdatedBachelier.setId(bachelier.getId());

        partialUpdatedBachelier.sexe(UPDATED_SEXE).dateNaissance(UPDATED_DATE_NAISSANCE).lieuNaissance(UPDATED_LIEU_NAISSANCE);

        restBachelierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBachelier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBachelier))
            )
            .andExpect(status().isOk());

        // Validate the Bachelier in the database
        List<Bachelier> bachelierList = bachelierRepository.findAll();
        assertThat(bachelierList).hasSize(databaseSizeBeforeUpdate);
        Bachelier testBachelier = bachelierList.get(bachelierList.size() - 1);
        assertThat(testBachelier.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testBachelier.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testBachelier.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testBachelier.getNationalite()).isEqualTo(DEFAULT_NATIONALITE);
        assertThat(testBachelier.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void fullUpdateBachelierWithPatch() throws Exception {
        // Initialize the database
        bachelierRepository.saveAndFlush(bachelier);

        int databaseSizeBeforeUpdate = bachelierRepository.findAll().size();

        // Update the bachelier using partial update
        Bachelier partialUpdatedBachelier = new Bachelier();
        partialUpdatedBachelier.setId(bachelier.getId());

        partialUpdatedBachelier
            .sexe(UPDATED_SEXE)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .lieuNaissance(UPDATED_LIEU_NAISSANCE)
            .nationalite(UPDATED_NATIONALITE)
            .telephone(UPDATED_TELEPHONE);

        restBachelierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBachelier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBachelier))
            )
            .andExpect(status().isOk());

        // Validate the Bachelier in the database
        List<Bachelier> bachelierList = bachelierRepository.findAll();
        assertThat(bachelierList).hasSize(databaseSizeBeforeUpdate);
        Bachelier testBachelier = bachelierList.get(bachelierList.size() - 1);
        assertThat(testBachelier.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testBachelier.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testBachelier.getLieuNaissance()).isEqualTo(UPDATED_LIEU_NAISSANCE);
        assertThat(testBachelier.getNationalite()).isEqualTo(UPDATED_NATIONALITE);
        assertThat(testBachelier.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void patchNonExistingBachelier() throws Exception {
        int databaseSizeBeforeUpdate = bachelierRepository.findAll().size();
        bachelier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBachelierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bachelier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bachelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bachelier in the database
        List<Bachelier> bachelierList = bachelierRepository.findAll();
        assertThat(bachelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBachelier() throws Exception {
        int databaseSizeBeforeUpdate = bachelierRepository.findAll().size();
        bachelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBachelierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bachelier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bachelier in the database
        List<Bachelier> bachelierList = bachelierRepository.findAll();
        assertThat(bachelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBachelier() throws Exception {
        int databaseSizeBeforeUpdate = bachelierRepository.findAll().size();
        bachelier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBachelierMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bachelier))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bachelier in the database
        List<Bachelier> bachelierList = bachelierRepository.findAll();
        assertThat(bachelierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBachelier() throws Exception {
        // Initialize the database
        bachelierRepository.saveAndFlush(bachelier);

        int databaseSizeBeforeDelete = bachelierRepository.findAll().size();

        // Delete the bachelier
        restBachelierMockMvc
            .perform(delete(ENTITY_API_URL_ID, bachelier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bachelier> bachelierList = bachelierRepository.findAll();
        assertThat(bachelierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
