package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.Person;
import com.credo.database.domain.PersonPhone;
import com.credo.database.repository.PersonPhoneRepository;
import com.credo.database.service.criteria.PersonPhoneCriteria;
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
 * Integration tests for the {@link PersonPhoneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonPhoneResourceIT {

    private static final String DEFAULT_PHONE_NUMBER = "(201) 156-2924";
    private static final String UPDATED_PHONE_NUMBER = "(371) 587-7015";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/person-phones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonPhoneRepository personPhoneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonPhoneMockMvc;

    private PersonPhone personPhone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonPhone createEntity(EntityManager em) {
        PersonPhone personPhone = new PersonPhone().phoneNumber(DEFAULT_PHONE_NUMBER).type(DEFAULT_TYPE);
        return personPhone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonPhone createUpdatedEntity(EntityManager em) {
        PersonPhone personPhone = new PersonPhone().phoneNumber(UPDATED_PHONE_NUMBER).type(UPDATED_TYPE);
        return personPhone;
    }

    @BeforeEach
    public void initTest() {
        personPhone = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonPhone() throws Exception {
        int databaseSizeBeforeCreate = personPhoneRepository.findAll().size();
        // Create the PersonPhone
        restPersonPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personPhone)))
            .andExpect(status().isCreated());

        // Validate the PersonPhone in the database
        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeCreate + 1);
        PersonPhone testPersonPhone = personPhoneList.get(personPhoneList.size() - 1);
        assertThat(testPersonPhone.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPersonPhone.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createPersonPhoneWithExistingId() throws Exception {
        // Create the PersonPhone with an existing ID
        personPhone.setId(1L);

        int databaseSizeBeforeCreate = personPhoneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personPhone)))
            .andExpect(status().isBadRequest());

        // Validate the PersonPhone in the database
        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = personPhoneRepository.findAll().size();
        // set the field null
        personPhone.setPhoneNumber(null);

        // Create the PersonPhone, which fails.

        restPersonPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personPhone)))
            .andExpect(status().isBadRequest());

        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonPhones() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList
        restPersonPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personPhone.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getPersonPhone() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get the personPhone
        restPersonPhoneMockMvc
            .perform(get(ENTITY_API_URL_ID, personPhone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personPhone.getId().intValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getPersonPhonesByIdFiltering() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        Long id = personPhone.getId();

        defaultPersonPhoneShouldBeFound("id.equals=" + id);
        defaultPersonPhoneShouldNotBeFound("id.notEquals=" + id);

        defaultPersonPhoneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonPhoneShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonPhoneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonPhoneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPersonPhonesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultPersonPhoneShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the personPhoneList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultPersonPhoneShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPersonPhonesByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultPersonPhoneShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the personPhoneList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultPersonPhoneShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPersonPhonesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultPersonPhoneShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the personPhoneList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultPersonPhoneShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPersonPhonesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList where phoneNumber is not null
        defaultPersonPhoneShouldBeFound("phoneNumber.specified=true");

        // Get all the personPhoneList where phoneNumber is null
        defaultPersonPhoneShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonPhonesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultPersonPhoneShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the personPhoneList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultPersonPhoneShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPersonPhonesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultPersonPhoneShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the personPhoneList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultPersonPhoneShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllPersonPhonesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList where type equals to DEFAULT_TYPE
        defaultPersonPhoneShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the personPhoneList where type equals to UPDATED_TYPE
        defaultPersonPhoneShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonPhonesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList where type not equals to DEFAULT_TYPE
        defaultPersonPhoneShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the personPhoneList where type not equals to UPDATED_TYPE
        defaultPersonPhoneShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonPhonesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultPersonPhoneShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the personPhoneList where type equals to UPDATED_TYPE
        defaultPersonPhoneShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonPhonesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList where type is not null
        defaultPersonPhoneShouldBeFound("type.specified=true");

        // Get all the personPhoneList where type is null
        defaultPersonPhoneShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonPhonesByTypeContainsSomething() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList where type contains DEFAULT_TYPE
        defaultPersonPhoneShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the personPhoneList where type contains UPDATED_TYPE
        defaultPersonPhoneShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonPhonesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        // Get all the personPhoneList where type does not contain DEFAULT_TYPE
        defaultPersonPhoneShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the personPhoneList where type does not contain UPDATED_TYPE
        defaultPersonPhoneShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonPhonesByPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);
        Person person = PersonResourceIT.createEntity(em);
        em.persist(person);
        em.flush();
        personPhone.setPerson(person);
        personPhoneRepository.saveAndFlush(personPhone);
        Long personId = person.getId();

        // Get all the personPhoneList where person equals to personId
        defaultPersonPhoneShouldBeFound("personId.equals=" + personId);

        // Get all the personPhoneList where person equals to (personId + 1)
        defaultPersonPhoneShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonPhoneShouldBeFound(String filter) throws Exception {
        restPersonPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personPhone.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restPersonPhoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonPhoneShouldNotBeFound(String filter) throws Exception {
        restPersonPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonPhoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPersonPhone() throws Exception {
        // Get the personPhone
        restPersonPhoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonPhone() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        int databaseSizeBeforeUpdate = personPhoneRepository.findAll().size();

        // Update the personPhone
        PersonPhone updatedPersonPhone = personPhoneRepository.findById(personPhone.getId()).get();
        // Disconnect from session so that the updates on updatedPersonPhone are not directly saved in db
        em.detach(updatedPersonPhone);
        updatedPersonPhone.phoneNumber(UPDATED_PHONE_NUMBER).type(UPDATED_TYPE);

        restPersonPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonPhone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonPhone))
            )
            .andExpect(status().isOk());

        // Validate the PersonPhone in the database
        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeUpdate);
        PersonPhone testPersonPhone = personPhoneList.get(personPhoneList.size() - 1);
        assertThat(testPersonPhone.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPersonPhone.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingPersonPhone() throws Exception {
        int databaseSizeBeforeUpdate = personPhoneRepository.findAll().size();
        personPhone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personPhone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonPhone in the database
        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonPhone() throws Exception {
        int databaseSizeBeforeUpdate = personPhoneRepository.findAll().size();
        personPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonPhone in the database
        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonPhone() throws Exception {
        int databaseSizeBeforeUpdate = personPhoneRepository.findAll().size();
        personPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonPhoneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personPhone)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonPhone in the database
        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonPhoneWithPatch() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        int databaseSizeBeforeUpdate = personPhoneRepository.findAll().size();

        // Update the personPhone using partial update
        PersonPhone partialUpdatedPersonPhone = new PersonPhone();
        partialUpdatedPersonPhone.setId(personPhone.getId());

        restPersonPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonPhone))
            )
            .andExpect(status().isOk());

        // Validate the PersonPhone in the database
        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeUpdate);
        PersonPhone testPersonPhone = personPhoneList.get(personPhoneList.size() - 1);
        assertThat(testPersonPhone.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPersonPhone.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdatePersonPhoneWithPatch() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        int databaseSizeBeforeUpdate = personPhoneRepository.findAll().size();

        // Update the personPhone using partial update
        PersonPhone partialUpdatedPersonPhone = new PersonPhone();
        partialUpdatedPersonPhone.setId(personPhone.getId());

        partialUpdatedPersonPhone.phoneNumber(UPDATED_PHONE_NUMBER).type(UPDATED_TYPE);

        restPersonPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonPhone))
            )
            .andExpect(status().isOk());

        // Validate the PersonPhone in the database
        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeUpdate);
        PersonPhone testPersonPhone = personPhoneList.get(personPhoneList.size() - 1);
        assertThat(testPersonPhone.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPersonPhone.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingPersonPhone() throws Exception {
        int databaseSizeBeforeUpdate = personPhoneRepository.findAll().size();
        personPhone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonPhone in the database
        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonPhone() throws Exception {
        int databaseSizeBeforeUpdate = personPhoneRepository.findAll().size();
        personPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonPhone in the database
        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonPhone() throws Exception {
        int databaseSizeBeforeUpdate = personPhoneRepository.findAll().size();
        personPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personPhone))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonPhone in the database
        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonPhone() throws Exception {
        // Initialize the database
        personPhoneRepository.saveAndFlush(personPhone);

        int databaseSizeBeforeDelete = personPhoneRepository.findAll().size();

        // Delete the personPhone
        restPersonPhoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, personPhone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonPhone> personPhoneList = personPhoneRepository.findAll();
        assertThat(personPhoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
