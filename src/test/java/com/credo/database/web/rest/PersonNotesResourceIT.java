package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.Person;
import com.credo.database.domain.PersonNotes;
import com.credo.database.repository.PersonNotesRepository;
import com.credo.database.repository.PersonRepository;
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
 * Integration tests for the {@link PersonNotesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonNotesResourceIT {

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/person-notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonNotesRepository personNotesRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonNotesMockMvc;

    private PersonNotes personNotes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonNotes createEntity(EntityManager em) {
        PersonNotes personNotes = new PersonNotes().notes(DEFAULT_NOTES);
        return personNotes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonNotes createUpdatedEntity(EntityManager em) {
        PersonNotes personNotes = new PersonNotes().notes(UPDATED_NOTES);
        return personNotes;
    }

    @BeforeEach
    public void initTest() {
        personNotes = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonNotesAdvanced() throws Exception {
        Person person = new Person().isDeceased(false).isHeadOfHouse(false);
        int databaseSizeBeforeCreate = personNotesRepository.findAll().size();
        int databaseSizeBeforeCreatePerson = personRepository.findAll().size();
        restPersonNotesMockMvc
            .perform(post("/api/person").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isCreated());
        // Create the PersonNotes
        restPersonNotesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personNotes)))
            .andExpect(status().isCreated());

        // Validate the PersonNotes in the database
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeCreate + 1);
        PersonNotes testPersonNotes = personNotesList.get(personNotesList.size() - 1);
        assertThat(testPersonNotes.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createPersonNotes() throws Exception {
        int databaseSizeBeforeCreate = personNotesRepository.findAll().size();
        // Create the PersonNotes
        restPersonNotesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personNotes)))
            .andExpect(status().isCreated());

        // Validate the PersonNotes in the database
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeCreate + 1);
        PersonNotes testPersonNotes = personNotesList.get(personNotesList.size() - 1);
        assertThat(testPersonNotes.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createPersonNotesWithExistingId() throws Exception {
        // Create the PersonNotes with an existing ID
        personNotes.setId(1L);

        int databaseSizeBeforeCreate = personNotesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonNotesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personNotes)))
            .andExpect(status().isBadRequest());

        // Validate the PersonNotes in the database
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonNotes() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        // Get all the personNotesList
        restPersonNotesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personNotes.getId().intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getPersonNotes() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        // Get the personNotes
        restPersonNotesMockMvc
            .perform(get(ENTITY_API_URL_ID, personNotes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personNotes.getId().intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getPersonNotesByIdFiltering() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        Long id = personNotes.getId();

        defaultPersonNotesShouldBeFound("id.equals=" + id);
        defaultPersonNotesShouldNotBeFound("id.notEquals=" + id);

        defaultPersonNotesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonNotesShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonNotesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonNotesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPersonNotesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        // Get all the personNotesList where notes equals to DEFAULT_NOTES
        defaultPersonNotesShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the personNotesList where notes equals to UPDATED_NOTES
        defaultPersonNotesShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPersonNotesByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        // Get all the personNotesList where notes not equals to DEFAULT_NOTES
        defaultPersonNotesShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the personNotesList where notes not equals to UPDATED_NOTES
        defaultPersonNotesShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPersonNotesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        // Get all the personNotesList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultPersonNotesShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the personNotesList where notes equals to UPDATED_NOTES
        defaultPersonNotesShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPersonNotesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        // Get all the personNotesList where notes is not null
        defaultPersonNotesShouldBeFound("notes.specified=true");

        // Get all the personNotesList where notes is null
        defaultPersonNotesShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonNotesByNotesContainsSomething() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        // Get all the personNotesList where notes contains DEFAULT_NOTES
        defaultPersonNotesShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the personNotesList where notes contains UPDATED_NOTES
        defaultPersonNotesShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPersonNotesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        // Get all the personNotesList where notes does not contain DEFAULT_NOTES
        defaultPersonNotesShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the personNotesList where notes does not contain UPDATED_NOTES
        defaultPersonNotesShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllPersonNotesByPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);
        Person person = PersonResourceIT.createEntity(em);
        em.persist(person);
        em.flush();
        personNotes.setPerson(person);
        personNotesRepository.saveAndFlush(personNotes);
        Long personId = person.getId();

        // Get all the personNotesList where person equals to personId
        defaultPersonNotesShouldBeFound("personId.equals=" + personId);

        // Get all the personNotesList where person equals to (personId + 1)
        defaultPersonNotesShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonNotesShouldBeFound(String filter) throws Exception {
        restPersonNotesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personNotes.getId().intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restPersonNotesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonNotesShouldNotBeFound(String filter) throws Exception {
        restPersonNotesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonNotesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPersonNotes() throws Exception {
        // Get the personNotes
        restPersonNotesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonNotes() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        int databaseSizeBeforeUpdate = personNotesRepository.findAll().size();

        // Update the personNotes
        PersonNotes updatedPersonNotes = personNotesRepository.findById(personNotes.getId()).get();
        // Disconnect from session so that the updates on updatedPersonNotes are not directly saved in db
        em.detach(updatedPersonNotes);
        updatedPersonNotes.notes(UPDATED_NOTES);

        restPersonNotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonNotes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonNotes))
            )
            .andExpect(status().isOk());

        // Validate the PersonNotes in the database
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeUpdate);
        PersonNotes testPersonNotes = personNotesList.get(personNotesList.size() - 1);
        assertThat(testPersonNotes.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingPersonNotes() throws Exception {
        int databaseSizeBeforeUpdate = personNotesRepository.findAll().size();
        personNotes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonNotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personNotes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonNotes in the database
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonNotes() throws Exception {
        int databaseSizeBeforeUpdate = personNotesRepository.findAll().size();
        personNotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonNotesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonNotes in the database
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonNotes() throws Exception {
        int databaseSizeBeforeUpdate = personNotesRepository.findAll().size();
        personNotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonNotesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personNotes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonNotes in the database
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonNotesWithPatch() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        int databaseSizeBeforeUpdate = personNotesRepository.findAll().size();

        // Update the personNotes using partial update
        PersonNotes partialUpdatedPersonNotes = new PersonNotes();
        partialUpdatedPersonNotes.setId(personNotes.getId());

        restPersonNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonNotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonNotes))
            )
            .andExpect(status().isOk());

        // Validate the PersonNotes in the database
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeUpdate);
        PersonNotes testPersonNotes = personNotesList.get(personNotesList.size() - 1);
        assertThat(testPersonNotes.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void fullUpdatePersonNotesWithPatch() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        int databaseSizeBeforeUpdate = personNotesRepository.findAll().size();

        // Update the personNotes using partial update
        PersonNotes partialUpdatedPersonNotes = new PersonNotes();
        partialUpdatedPersonNotes.setId(personNotes.getId());

        partialUpdatedPersonNotes.notes(UPDATED_NOTES);

        restPersonNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonNotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonNotes))
            )
            .andExpect(status().isOk());

        // Validate the PersonNotes in the database
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeUpdate);
        PersonNotes testPersonNotes = personNotesList.get(personNotesList.size() - 1);
        assertThat(testPersonNotes.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingPersonNotes() throws Exception {
        int databaseSizeBeforeUpdate = personNotesRepository.findAll().size();
        personNotes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personNotes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonNotes in the database
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonNotes() throws Exception {
        int databaseSizeBeforeUpdate = personNotesRepository.findAll().size();
        personNotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonNotesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personNotes))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonNotes in the database
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonNotes() throws Exception {
        int databaseSizeBeforeUpdate = personNotesRepository.findAll().size();
        personNotes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonNotesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personNotes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonNotes in the database
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonNotes() throws Exception {
        // Initialize the database
        personNotesRepository.saveAndFlush(personNotes);

        int databaseSizeBeforeDelete = personNotesRepository.findAll().size();

        // Delete the personNotes
        restPersonNotesMockMvc
            .perform(delete(ENTITY_API_URL_ID, personNotes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonNotes> personNotesList = personNotesRepository.findAll();
        assertThat(personNotesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
