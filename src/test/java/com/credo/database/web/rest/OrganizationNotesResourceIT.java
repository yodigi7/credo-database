package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.Organization;
import com.credo.database.domain.OrganizationNotes;
import com.credo.database.repository.OrganizationNotesRepository;
import com.credo.database.service.criteria.OrganizationNotesCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrganizationNotesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganizationNotesResourceIT {

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organization-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganizationNotesRepository organizationNotesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationNotesMockMvc;

    private OrganizationNotes organizationNotes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationNotes createEntity(EntityManager em) {
        OrganizationNotes organizationNotes = new OrganizationNotes().notes(DEFAULT_NOTES);
        return organizationNotes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationNotes createUpdatedEntity(EntityManager em) {
        OrganizationNotes organizationNotes = new OrganizationNotes().notes(UPDATED_NOTES);
        return organizationNotes;
    }

    @BeforeEach
    public void initTest() {
        organizationNotes = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganizationNotes() throws Exception {
        int databaseSizeBeforeCreate = organizationNotesRepository.findAll().size();
        // Create the OrganizationNotes
        restOrganizationNotesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationNotes))
            )
            .andExpect(status().isCreated());

        // Validate the OrganizationNotes in the database
        List<OrganizationNotes> organizationNotesList = organizationNotesRepository.findAll();
        assertThat(organizationNotesList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationNotes testOrganizationNotes = organizationNotesList.get(organizationNotesList.size() - 1);
        assertThat(testOrganizationNotes.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createOrganizationNotesWithExistingId() throws Exception {
        // Create the OrganizationNotes with an existing ID
        organizationNotes.setId(1L);

        int databaseSizeBeforeCreate = organizationNotesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationNotesMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationNotes in the database
        List<OrganizationNotes> organizationNotesList = organizationNotesRepository.findAll();
        assertThat(organizationNotesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrganizationNotes() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        // Get all the organizationNotesList
        restOrganizationNotesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationNotes.getId().intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getOrganizationNotes() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        // Get the organizationNotes
        restOrganizationNotesMockMvc
            .perform(get(ENTITY_API_URL_ID, organizationNotes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organizationNotes.getId().intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getOrganizationNotesByIdFiltering() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        Long id = organizationNotes.getId();

        defaultOrganizationNotesShouldBeFound("id.equals=" + id);
        defaultOrganizationNotesShouldNotBeFound("id.notEquals=" + id);

        defaultOrganizationNotesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrganizationNotesShouldNotBeFound("id.greaterThan=" + id);

        defaultOrganizationNotesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrganizationNotesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrganizationNotesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        // Get all the organizationNotesList where notes equals to DEFAULT_NOTES
        defaultOrganizationNotesShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the organizationNotesList where notes equals to UPDATED_NOTES
        defaultOrganizationNotesShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOrganizationNotesByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        // Get all the organizationNotesList where notes not equals to DEFAULT_NOTES
        defaultOrganizationNotesShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the organizationNotesList where notes not equals to UPDATED_NOTES
        defaultOrganizationNotesShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOrganizationNotesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        // Get all the organizationNotesList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultOrganizationNotesShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the organizationNotesList where notes equals to UPDATED_NOTES
        defaultOrganizationNotesShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOrganizationNotesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        // Get all the organizationNotesList where notes is not null
        defaultOrganizationNotesShouldBeFound("notes.specified=true");

        // Get all the organizationNotesList where notes is null
        defaultOrganizationNotesShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationNotesByNotesContainsSomething() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        // Get all the organizationNotesList where notes contains DEFAULT_NOTES
        defaultOrganizationNotesShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the organizationNotesList where notes contains UPDATED_NOTES
        defaultOrganizationNotesShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOrganizationNotesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        // Get all the organizationNotesList where notes does not contain DEFAULT_NOTES
        defaultOrganizationNotesShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the organizationNotesList where notes does not contain UPDATED_NOTES
        defaultOrganizationNotesShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllOrganizationNotesByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);
        Organization organization = OrganizationResourceIT.createEntity(em);
        em.persist(organization);
        em.flush();
        organizationNotes.setOrganization(organization);
        organizationNotesRepository.saveAndFlush(organizationNotes);
        Long organizationId = organization.getId();

        // Get all the organizationNotesList where organization equals to organizationId
        defaultOrganizationNotesShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the organizationNotesList where organization equals to (organizationId + 1)
        defaultOrganizationNotesShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganizationNotesShouldBeFound(String filter) throws Exception {
        restOrganizationNotesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationNotes.getId().intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restOrganizationNotesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganizationNotesShouldNotBeFound(String filter) throws Exception {
        restOrganizationNotesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganizationNotesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrganizationNotes() throws Exception {
        // Get the organizationNotes
        restOrganizationNotesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrganizationNotes() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        int databaseSizeBeforeUpdate = organizationNotesRepository.findAll().size();

        // Update the organizationNotes
        OrganizationNotes updatedOrganizationNotes = organizationNotesRepository.findById(organizationNotes.getId()).get();
        // Disconnect from session so that the updates on updatedOrganizationNotes are not directly saved in db
        em.detach(updatedOrganizationNotes);
        updatedOrganizationNotes.notes(UPDATED_NOTES);

        restOrganizationNotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganizationNotes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrganizationNotes))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationNotes in the database
        List<OrganizationNotes> organizationNotesList = organizationNotesRepository.findAll();
        assertThat(organizationNotesList).hasSize(databaseSizeBeforeUpdate);
        OrganizationNotes testOrganizationNotes = organizationNotesList.get(organizationNotesList.size() - 1);
        assertThat(testOrganizationNotes.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingOrganizationNotes() throws Exception {
        int databaseSizeBeforeUpdate = organizationNotesRepository.findAll().size();
        organizationNotes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationNotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationNotes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationNotes in the database
        List<OrganizationNotes> organizationNotesList = organizationNotesRepository.findAll();
        assertThat(organizationNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganizationNotes() throws Exception {
        int databaseSizeBeforeUpdate = organizationNotesRepository.findAll().size();
        organizationNotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationNotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationNotes in the database
        List<OrganizationNotes> organizationNotesList = organizationNotesRepository.findAll();
        assertThat(organizationNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganizationNotes() throws Exception {
        int databaseSizeBeforeUpdate = organizationNotesRepository.findAll().size();
        organizationNotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationNotesMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationNotes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationNotes in the database
        List<OrganizationNotes> organizationNotesList = organizationNotesRepository.findAll();
        assertThat(organizationNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganizationNotesWithPatch() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        int databaseSizeBeforeUpdate = organizationNotesRepository.findAll().size();

        // Update the organizationNotes using partial update
        OrganizationNotes partialUpdatedOrganizationNotes = new OrganizationNotes();
        partialUpdatedOrganizationNotes.setId(organizationNotes.getId());

        partialUpdatedOrganizationNotes.notes(UPDATED_NOTES);

        restOrganizationNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationNotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationNotes))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationNotes in the database
        List<OrganizationNotes> organizationNotesList = organizationNotesRepository.findAll();
        assertThat(organizationNotesList).hasSize(databaseSizeBeforeUpdate);
        OrganizationNotes testOrganizationNotes = organizationNotesList.get(organizationNotesList.size() - 1);
        assertThat(testOrganizationNotes.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateOrganizationNotesWithPatch() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        int databaseSizeBeforeUpdate = organizationNotesRepository.findAll().size();

        // Update the organizationNotes using partial update
        OrganizationNotes partialUpdatedOrganizationNotes = new OrganizationNotes();
        partialUpdatedOrganizationNotes.setId(organizationNotes.getId());

        partialUpdatedOrganizationNotes.notes(UPDATED_NOTES);

        restOrganizationNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationNotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationNotes))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationNotes in the database
        List<OrganizationNotes> organizationNotesList = organizationNotesRepository.findAll();
        assertThat(organizationNotesList).hasSize(databaseSizeBeforeUpdate);
        OrganizationNotes testOrganizationNotes = organizationNotesList.get(organizationNotesList.size() - 1);
        assertThat(testOrganizationNotes.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingOrganizationNotes() throws Exception {
        int databaseSizeBeforeUpdate = organizationNotesRepository.findAll().size();
        organizationNotes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organizationNotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationNotes in the database
        List<OrganizationNotes> organizationNotesList = organizationNotesRepository.findAll();
        assertThat(organizationNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganizationNotes() throws Exception {
        int databaseSizeBeforeUpdate = organizationNotesRepository.findAll().size();
        organizationNotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationNotes in the database
        List<OrganizationNotes> organizationNotesList = organizationNotesRepository.findAll();
        assertThat(organizationNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganizationNotes() throws Exception {
        int databaseSizeBeforeUpdate = organizationNotesRepository.findAll().size();
        organizationNotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationNotesMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationNotes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationNotes in the database
        List<OrganizationNotes> organizationNotesList = organizationNotesRepository.findAll();
        assertThat(organizationNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganizationNotes() throws Exception {
        // Initialize the database
        organizationNotesRepository.saveAndFlush(organizationNotes);

        int databaseSizeBeforeDelete = organizationNotesRepository.findAll().size();

        // Delete the organizationNotes
        restOrganizationNotesMockMvc
            .perform(delete(ENTITY_API_URL_ID, organizationNotes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganizationNotes> organizationNotesList = organizationNotesRepository.findAll();
        assertThat(organizationNotesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
