package nig.campus.com.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import nig.campus.com.IntegrationTest;
import nig.campus.com.domain.Acceptation;
import nig.campus.com.repository.AcceptationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AcceptationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AcceptationResourceIT {

    private static final String DEFAULT_FILIERE = "AAAAAAAAAA";
    private static final String UPDATED_FILIERE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ACCEPTATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ACCEPTATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/acceptations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcceptationRepository acceptationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcceptationMockMvc;

    private Acceptation acceptation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acceptation createEntity(EntityManager em) {
        Acceptation acceptation = new Acceptation().filiere(DEFAULT_FILIERE).dateAcceptation(DEFAULT_DATE_ACCEPTATION);
        return acceptation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Acceptation createUpdatedEntity(EntityManager em) {
        Acceptation acceptation = new Acceptation().filiere(UPDATED_FILIERE).dateAcceptation(UPDATED_DATE_ACCEPTATION);
        return acceptation;
    }

    @BeforeEach
    public void initTest() {
        acceptation = createEntity(em);
    }

    @Test
    @Transactional
    void createAcceptation() throws Exception {
        int databaseSizeBeforeCreate = acceptationRepository.findAll().size();
        // Create the Acceptation
        restAcceptationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acceptation)))
            .andExpect(status().isCreated());

        // Validate the Acceptation in the database
        List<Acceptation> acceptationList = acceptationRepository.findAll();
        assertThat(acceptationList).hasSize(databaseSizeBeforeCreate + 1);
        Acceptation testAcceptation = acceptationList.get(acceptationList.size() - 1);
        assertThat(testAcceptation.getFiliere()).isEqualTo(DEFAULT_FILIERE);
        assertThat(testAcceptation.getDateAcceptation()).isEqualTo(DEFAULT_DATE_ACCEPTATION);
    }

    @Test
    @Transactional
    void createAcceptationWithExistingId() throws Exception {
        // Create the Acceptation with an existing ID
        acceptation.setId(1L);

        int databaseSizeBeforeCreate = acceptationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcceptationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acceptation)))
            .andExpect(status().isBadRequest());

        // Validate the Acceptation in the database
        List<Acceptation> acceptationList = acceptationRepository.findAll();
        assertThat(acceptationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAcceptations() throws Exception {
        // Initialize the database
        acceptationRepository.saveAndFlush(acceptation);

        // Get all the acceptationList
        restAcceptationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(acceptation.getId().intValue())))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE)))
            .andExpect(jsonPath("$.[*].dateAcceptation").value(hasItem(DEFAULT_DATE_ACCEPTATION.toString())));
    }

    @Test
    @Transactional
    void getAcceptation() throws Exception {
        // Initialize the database
        acceptationRepository.saveAndFlush(acceptation);

        // Get the acceptation
        restAcceptationMockMvc
            .perform(get(ENTITY_API_URL_ID, acceptation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(acceptation.getId().intValue()))
            .andExpect(jsonPath("$.filiere").value(DEFAULT_FILIERE))
            .andExpect(jsonPath("$.dateAcceptation").value(DEFAULT_DATE_ACCEPTATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAcceptation() throws Exception {
        // Get the acceptation
        restAcceptationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAcceptation() throws Exception {
        // Initialize the database
        acceptationRepository.saveAndFlush(acceptation);

        int databaseSizeBeforeUpdate = acceptationRepository.findAll().size();

        // Update the acceptation
        Acceptation updatedAcceptation = acceptationRepository.findById(acceptation.getId()).get();
        // Disconnect from session so that the updates on updatedAcceptation are not directly saved in db
        em.detach(updatedAcceptation);
        updatedAcceptation.filiere(UPDATED_FILIERE).dateAcceptation(UPDATED_DATE_ACCEPTATION);

        restAcceptationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAcceptation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAcceptation))
            )
            .andExpect(status().isOk());

        // Validate the Acceptation in the database
        List<Acceptation> acceptationList = acceptationRepository.findAll();
        assertThat(acceptationList).hasSize(databaseSizeBeforeUpdate);
        Acceptation testAcceptation = acceptationList.get(acceptationList.size() - 1);
        assertThat(testAcceptation.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testAcceptation.getDateAcceptation()).isEqualTo(UPDATED_DATE_ACCEPTATION);
    }

    @Test
    @Transactional
    void putNonExistingAcceptation() throws Exception {
        int databaseSizeBeforeUpdate = acceptationRepository.findAll().size();
        acceptation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcceptationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, acceptation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acceptation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acceptation in the database
        List<Acceptation> acceptationList = acceptationRepository.findAll();
        assertThat(acceptationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcceptation() throws Exception {
        int databaseSizeBeforeUpdate = acceptationRepository.findAll().size();
        acceptation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(acceptation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acceptation in the database
        List<Acceptation> acceptationList = acceptationRepository.findAll();
        assertThat(acceptationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcceptation() throws Exception {
        int databaseSizeBeforeUpdate = acceptationRepository.findAll().size();
        acceptation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(acceptation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Acceptation in the database
        List<Acceptation> acceptationList = acceptationRepository.findAll();
        assertThat(acceptationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcceptationWithPatch() throws Exception {
        // Initialize the database
        acceptationRepository.saveAndFlush(acceptation);

        int databaseSizeBeforeUpdate = acceptationRepository.findAll().size();

        // Update the acceptation using partial update
        Acceptation partialUpdatedAcceptation = new Acceptation();
        partialUpdatedAcceptation.setId(acceptation.getId());

        partialUpdatedAcceptation.filiere(UPDATED_FILIERE);

        restAcceptationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcceptation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcceptation))
            )
            .andExpect(status().isOk());

        // Validate the Acceptation in the database
        List<Acceptation> acceptationList = acceptationRepository.findAll();
        assertThat(acceptationList).hasSize(databaseSizeBeforeUpdate);
        Acceptation testAcceptation = acceptationList.get(acceptationList.size() - 1);
        assertThat(testAcceptation.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testAcceptation.getDateAcceptation()).isEqualTo(DEFAULT_DATE_ACCEPTATION);
    }

    @Test
    @Transactional
    void fullUpdateAcceptationWithPatch() throws Exception {
        // Initialize the database
        acceptationRepository.saveAndFlush(acceptation);

        int databaseSizeBeforeUpdate = acceptationRepository.findAll().size();

        // Update the acceptation using partial update
        Acceptation partialUpdatedAcceptation = new Acceptation();
        partialUpdatedAcceptation.setId(acceptation.getId());

        partialUpdatedAcceptation.filiere(UPDATED_FILIERE).dateAcceptation(UPDATED_DATE_ACCEPTATION);

        restAcceptationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcceptation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcceptation))
            )
            .andExpect(status().isOk());

        // Validate the Acceptation in the database
        List<Acceptation> acceptationList = acceptationRepository.findAll();
        assertThat(acceptationList).hasSize(databaseSizeBeforeUpdate);
        Acceptation testAcceptation = acceptationList.get(acceptationList.size() - 1);
        assertThat(testAcceptation.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testAcceptation.getDateAcceptation()).isEqualTo(UPDATED_DATE_ACCEPTATION);
    }

    @Test
    @Transactional
    void patchNonExistingAcceptation() throws Exception {
        int databaseSizeBeforeUpdate = acceptationRepository.findAll().size();
        acceptation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcceptationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, acceptation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acceptation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acceptation in the database
        List<Acceptation> acceptationList = acceptationRepository.findAll();
        assertThat(acceptationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcceptation() throws Exception {
        int databaseSizeBeforeUpdate = acceptationRepository.findAll().size();
        acceptation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(acceptation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Acceptation in the database
        List<Acceptation> acceptationList = acceptationRepository.findAll();
        assertThat(acceptationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcceptation() throws Exception {
        int databaseSizeBeforeUpdate = acceptationRepository.findAll().size();
        acceptation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcceptationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(acceptation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Acceptation in the database
        List<Acceptation> acceptationList = acceptationRepository.findAll();
        assertThat(acceptationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAcceptation() throws Exception {
        // Initialize the database
        acceptationRepository.saveAndFlush(acceptation);

        int databaseSizeBeforeDelete = acceptationRepository.findAll().size();

        // Delete the acceptation
        restAcceptationMockMvc
            .perform(delete(ENTITY_API_URL_ID, acceptation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Acceptation> acceptationList = acceptationRepository.findAll();
        assertThat(acceptationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
