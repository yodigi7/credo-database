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
import com.credo.database.domain.*;
import com.credo.database.repository.ParishRepository;
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
 * Integration tests for the {@link ParishResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParishResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parishes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParishRepository parishRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParishMockMvc;

    private Parish parish;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parish createEntity(EntityManager em) {
        Parish parish = new Parish().name(DEFAULT_NAME);
        return parish;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Parish createUpdatedEntity(EntityManager em) {
        Parish parish = new Parish().name(UPDATED_NAME);
        return parish;
    }

    @BeforeEach
    public void initTest() {
        parish = createEntity(em);
    }

    @Test
    @Transactional
    void createParish() throws Exception {
        int databaseSizeBeforeCreate = parishRepository.findAll().size();
        // Create the Parish
        restParishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parish)))
            .andExpect(status().isCreated());

        // Validate the Parish in the database
        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeCreate + 1);
        Parish testParish = parishList.get(parishList.size() - 1);
        assertThat(testParish.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createParishWithExistingId() throws Exception {
        // Create the Parish with an existing ID
        parish.setId(1L);

        int databaseSizeBeforeCreate = parishRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parish)))
            .andExpect(status().isBadRequest());

        // Validate the Parish in the database
        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = parishRepository.findAll().size();
        // set the field null
        parish.setName(null);

        // Create the Parish, which fails.

        restParishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parish)))
            .andExpect(status().isBadRequest());

        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParishes() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        // Get all the parishList
        restParishMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parish.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getParish() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        // Get the parish
        restParishMockMvc
            .perform(get(ENTITY_API_URL_ID, parish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parish.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getParishesByIdFiltering() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        Long id = parish.getId();

        defaultParishShouldBeFound("id.equals=" + id);
        defaultParishShouldNotBeFound("id.notEquals=" + id);

        defaultParishShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParishShouldNotBeFound("id.greaterThan=" + id);

        defaultParishShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParishShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllParishesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        // Get all the parishList where name equals to DEFAULT_NAME
        defaultParishShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the parishList where name equals to UPDATED_NAME
        defaultParishShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllParishesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        // Get all the parishList where name not equals to DEFAULT_NAME
        defaultParishShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the parishList where name not equals to UPDATED_NAME
        defaultParishShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllParishesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        // Get all the parishList where name in DEFAULT_NAME or UPDATED_NAME
        defaultParishShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the parishList where name equals to UPDATED_NAME
        defaultParishShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllParishesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        // Get all the parishList where name is not null
        defaultParishShouldBeFound("name.specified=true");

        // Get all the parishList where name is null
        defaultParishShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllParishesByNameContainsSomething() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        // Get all the parishList where name contains DEFAULT_NAME
        defaultParishShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the parishList where name contains UPDATED_NAME
        defaultParishShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllParishesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        // Get all the parishList where name does not contain DEFAULT_NAME
        defaultParishShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the parishList where name does not contain UPDATED_NAME
        defaultParishShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllParishesByOrganizationsIsEqualToSomething() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);
        Organization organizations = OrganizationResourceIT.createEntity(em);
        em.persist(organizations);
        em.flush();
        parish.addOrganizations(organizations);
        parishRepository.saveAndFlush(parish);
        Long organizationsId = organizations.getId();

        // Get all the parishList where organizations equals to organizationsId
        defaultParishShouldBeFound("organizationsId.equals=" + organizationsId);

        // Get all the parishList where organizations equals to (organizationsId + 1)
        defaultParishShouldNotBeFound("organizationsId.equals=" + (organizationsId + 1));
    }

    @Test
    @Transactional
    void getAllParishesByPhonesIsEqualToSomething() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);
        ParishPhone phones = ParishPhoneResourceIT.createEntity(em);
        em.persist(phones);
        em.flush();
        parish.addPhones(phones);
        parishRepository.saveAndFlush(parish);
        Long phonesId = phones.getId();

        // Get all the parishList where phones equals to phonesId
        defaultParishShouldBeFound("phonesId.equals=" + phonesId);

        // Get all the parishList where phones equals to (phonesId + 1)
        defaultParishShouldNotBeFound("phonesId.equals=" + (phonesId + 1));
    }

    @Test
    @Transactional
    void getAllParishesByPeopleIsEqualToSomething() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);
        Person people = PersonResourceIT.createEntity(em);
        em.persist(people);
        em.flush();
        parish.addPeople(people);
        parishRepository.saveAndFlush(parish);
        Long peopleId = people.getId();

        // Get all the parishList where people equals to peopleId
        defaultParishShouldBeFound("peopleId.equals=" + peopleId);

        // Get all the parishList where people equals to (peopleId + 1)
        defaultParishShouldNotBeFound("peopleId.equals=" + (peopleId + 1));
    }

    @Test
    @Transactional
    void getAllParishesByEmailsIsEqualToSomething() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);
        ParishEmail emails = ParishEmailResourceIT.createEntity(em);
        em.persist(emails);
        em.flush();
        parish.addEmails(emails);
        parishRepository.saveAndFlush(parish);
        Long emailsId = emails.getId();

        // Get all the parishList where emails equals to emailsId
        defaultParishShouldBeFound("emailsId.equals=" + emailsId);

        // Get all the parishList where emails equals to (emailsId + 1)
        defaultParishShouldNotBeFound("emailsId.equals=" + (emailsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParishShouldBeFound(String filter) throws Exception {
        restParishMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parish.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restParishMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParishShouldNotBeFound(String filter) throws Exception {
        restParishMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParishMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParish() throws Exception {
        // Get the parish
        restParishMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParish() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        int databaseSizeBeforeUpdate = parishRepository.findAll().size();

        // Update the parish
        Parish updatedParish = parishRepository.findById(parish.getId()).get();
        // Disconnect from session so that the updates on updatedParish are not directly saved in db
        em.detach(updatedParish);
        updatedParish.name(UPDATED_NAME);

        restParishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParish.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParish))
            )
            .andExpect(status().isOk());

        // Validate the Parish in the database
        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeUpdate);
        Parish testParish = parishList.get(parishList.size() - 1);
        assertThat(testParish.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingParish() throws Exception {
        int databaseSizeBeforeUpdate = parishRepository.findAll().size();
        parish.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parish.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parish in the database
        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParish() throws Exception {
        int databaseSizeBeforeUpdate = parishRepository.findAll().size();
        parish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parish in the database
        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParish() throws Exception {
        int databaseSizeBeforeUpdate = parishRepository.findAll().size();
        parish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParishMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parish)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parish in the database
        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParishWithPatch() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        int databaseSizeBeforeUpdate = parishRepository.findAll().size();

        // Update the parish using partial update
        Parish partialUpdatedParish = new Parish();
        partialUpdatedParish.setId(parish.getId());

        restParishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParish))
            )
            .andExpect(status().isOk());

        // Validate the Parish in the database
        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeUpdate);
        Parish testParish = parishList.get(parishList.size() - 1);
        assertThat(testParish.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateParishWithPatch() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        int databaseSizeBeforeUpdate = parishRepository.findAll().size();

        // Update the parish using partial update
        Parish partialUpdatedParish = new Parish();
        partialUpdatedParish.setId(parish.getId());

        partialUpdatedParish.name(UPDATED_NAME);

        restParishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParish))
            )
            .andExpect(status().isOk());

        // Validate the Parish in the database
        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeUpdate);
        Parish testParish = parishList.get(parishList.size() - 1);
        assertThat(testParish.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingParish() throws Exception {
        int databaseSizeBeforeUpdate = parishRepository.findAll().size();
        parish.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parish in the database
        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParish() throws Exception {
        int databaseSizeBeforeUpdate = parishRepository.findAll().size();
        parish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Parish in the database
        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParish() throws Exception {
        int databaseSizeBeforeUpdate = parishRepository.findAll().size();
        parish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParishMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parish)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Parish in the database
        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParish() throws Exception {
        // Initialize the database
        parishRepository.saveAndFlush(parish);

        int databaseSizeBeforeDelete = parishRepository.findAll().size();

        // Delete the parish
        restParishMockMvc
            .perform(delete(ENTITY_API_URL_ID, parish.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Parish> parishList = parishRepository.findAll();
        assertThat(parishList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
