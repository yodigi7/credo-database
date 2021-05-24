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
import com.credo.database.domain.Parish;
import com.credo.database.domain.ParishPhone;
import com.credo.database.repository.ParishPhoneRepository;
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
 * Integration tests for the {@link ParishPhoneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParishPhoneResourceIT {

    private static final String DEFAULT_PHONE_NUMBER = "(111) 111-1111";
    private static final String UPDATED_PHONE_NUMBER = "(222) 222-2222";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parish-phones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParishPhoneRepository parishPhoneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParishPhoneMockMvc;

    private ParishPhone parishPhone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParishPhone createEntity(EntityManager em) {
        ParishPhone parishPhone = new ParishPhone().phoneNumber(DEFAULT_PHONE_NUMBER).type(DEFAULT_TYPE);
        return parishPhone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParishPhone createUpdatedEntity(EntityManager em) {
        ParishPhone parishPhone = new ParishPhone().phoneNumber(UPDATED_PHONE_NUMBER).type(UPDATED_TYPE);
        return parishPhone;
    }

    @BeforeEach
    public void initTest() {
        parishPhone = createEntity(em);
    }

    @Test
    @Transactional
    void createParishPhone() throws Exception {
        int databaseSizeBeforeCreate = parishPhoneRepository.findAll().size();
        // Create the ParishPhone
        restParishPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parishPhone)))
            .andExpect(status().isCreated());

        // Validate the ParishPhone in the database
        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeCreate + 1);
        ParishPhone testParishPhone = parishPhoneList.get(parishPhoneList.size() - 1);
        assertThat(testParishPhone.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testParishPhone.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createParishPhoneWithExistingId() throws Exception {
        // Create the ParishPhone with an existing ID
        parishPhone.setId(1L);

        int databaseSizeBeforeCreate = parishPhoneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParishPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parishPhone)))
            .andExpect(status().isBadRequest());

        // Validate the ParishPhone in the database
        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = parishPhoneRepository.findAll().size();
        // set the field null
        parishPhone.setPhoneNumber(null);

        // Create the ParishPhone, which fails.

        restParishPhoneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parishPhone)))
            .andExpect(status().isBadRequest());

        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParishPhones() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList
        restParishPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parishPhone.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getParishPhone() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get the parishPhone
        restParishPhoneMockMvc
            .perform(get(ENTITY_API_URL_ID, parishPhone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parishPhone.getId().intValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getParishPhonesByIdFiltering() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        Long id = parishPhone.getId();

        defaultParishPhoneShouldBeFound("id.equals=" + id);
        defaultParishPhoneShouldNotBeFound("id.notEquals=" + id);

        defaultParishPhoneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParishPhoneShouldNotBeFound("id.greaterThan=" + id);

        defaultParishPhoneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParishPhoneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllParishPhonesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultParishPhoneShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the parishPhoneList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultParishPhoneShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllParishPhonesByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultParishPhoneShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the parishPhoneList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultParishPhoneShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllParishPhonesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultParishPhoneShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the parishPhoneList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultParishPhoneShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllParishPhonesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList where phoneNumber is not null
        defaultParishPhoneShouldBeFound("phoneNumber.specified=true");

        // Get all the parishPhoneList where phoneNumber is null
        defaultParishPhoneShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllParishPhonesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultParishPhoneShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the parishPhoneList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultParishPhoneShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllParishPhonesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultParishPhoneShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the parishPhoneList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultParishPhoneShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllParishPhonesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList where type equals to DEFAULT_TYPE
        defaultParishPhoneShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the parishPhoneList where type equals to UPDATED_TYPE
        defaultParishPhoneShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllParishPhonesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList where type not equals to DEFAULT_TYPE
        defaultParishPhoneShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the parishPhoneList where type not equals to UPDATED_TYPE
        defaultParishPhoneShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllParishPhonesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultParishPhoneShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the parishPhoneList where type equals to UPDATED_TYPE
        defaultParishPhoneShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllParishPhonesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList where type is not null
        defaultParishPhoneShouldBeFound("type.specified=true");

        // Get all the parishPhoneList where type is null
        defaultParishPhoneShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllParishPhonesByTypeContainsSomething() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList where type contains DEFAULT_TYPE
        defaultParishPhoneShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the parishPhoneList where type contains UPDATED_TYPE
        defaultParishPhoneShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllParishPhonesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        // Get all the parishPhoneList where type does not contain DEFAULT_TYPE
        defaultParishPhoneShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the parishPhoneList where type does not contain UPDATED_TYPE
        defaultParishPhoneShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllParishPhonesByParishIsEqualToSomething() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);
        Parish parish = ParishResourceIT.createEntity(em);
        em.persist(parish);
        em.flush();
        parishPhone.setParish(parish);
        parishPhoneRepository.saveAndFlush(parishPhone);
        Long parishId = parish.getId();

        // Get all the parishPhoneList where parish equals to parishId
        defaultParishPhoneShouldBeFound("parishId.equals=" + parishId);

        // Get all the parishPhoneList where parish equals to (parishId + 1)
        defaultParishPhoneShouldNotBeFound("parishId.equals=" + (parishId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParishPhoneShouldBeFound(String filter) throws Exception {
        restParishPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parishPhone.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restParishPhoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParishPhoneShouldNotBeFound(String filter) throws Exception {
        restParishPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParishPhoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParishPhone() throws Exception {
        // Get the parishPhone
        restParishPhoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParishPhone() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        int databaseSizeBeforeUpdate = parishPhoneRepository.findAll().size();

        // Update the parishPhone
        ParishPhone updatedParishPhone = parishPhoneRepository.findById(parishPhone.getId()).get();
        // Disconnect from session so that the updates on updatedParishPhone are not directly saved in db
        em.detach(updatedParishPhone);
        updatedParishPhone.phoneNumber(UPDATED_PHONE_NUMBER).type(UPDATED_TYPE);

        restParishPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParishPhone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParishPhone))
            )
            .andExpect(status().isOk());

        // Validate the ParishPhone in the database
        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeUpdate);
        ParishPhone testParishPhone = parishPhoneList.get(parishPhoneList.size() - 1);
        assertThat(testParishPhone.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testParishPhone.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingParishPhone() throws Exception {
        int databaseSizeBeforeUpdate = parishPhoneRepository.findAll().size();
        parishPhone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParishPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parishPhone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parishPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParishPhone in the database
        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParishPhone() throws Exception {
        int databaseSizeBeforeUpdate = parishPhoneRepository.findAll().size();
        parishPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParishPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parishPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParishPhone in the database
        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParishPhone() throws Exception {
        int databaseSizeBeforeUpdate = parishPhoneRepository.findAll().size();
        parishPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParishPhoneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parishPhone)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParishPhone in the database
        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParishPhoneWithPatch() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        int databaseSizeBeforeUpdate = parishPhoneRepository.findAll().size();

        // Update the parishPhone using partial update
        ParishPhone partialUpdatedParishPhone = new ParishPhone();
        partialUpdatedParishPhone.setId(parishPhone.getId());

        restParishPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParishPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParishPhone))
            )
            .andExpect(status().isOk());

        // Validate the ParishPhone in the database
        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeUpdate);
        ParishPhone testParishPhone = parishPhoneList.get(parishPhoneList.size() - 1);
        assertThat(testParishPhone.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testParishPhone.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateParishPhoneWithPatch() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        int databaseSizeBeforeUpdate = parishPhoneRepository.findAll().size();

        // Update the parishPhone using partial update
        ParishPhone partialUpdatedParishPhone = new ParishPhone();
        partialUpdatedParishPhone.setId(parishPhone.getId());

        partialUpdatedParishPhone.phoneNumber(UPDATED_PHONE_NUMBER).type(UPDATED_TYPE);

        restParishPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParishPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParishPhone))
            )
            .andExpect(status().isOk());

        // Validate the ParishPhone in the database
        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeUpdate);
        ParishPhone testParishPhone = parishPhoneList.get(parishPhoneList.size() - 1);
        assertThat(testParishPhone.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testParishPhone.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingParishPhone() throws Exception {
        int databaseSizeBeforeUpdate = parishPhoneRepository.findAll().size();
        parishPhone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParishPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parishPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parishPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParishPhone in the database
        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParishPhone() throws Exception {
        int databaseSizeBeforeUpdate = parishPhoneRepository.findAll().size();
        parishPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParishPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parishPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParishPhone in the database
        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParishPhone() throws Exception {
        int databaseSizeBeforeUpdate = parishPhoneRepository.findAll().size();
        parishPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParishPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parishPhone))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParishPhone in the database
        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParishPhone() throws Exception {
        // Initialize the database
        parishPhoneRepository.saveAndFlush(parishPhone);

        int databaseSizeBeforeDelete = parishPhoneRepository.findAll().size();

        // Delete the parishPhone
        restParishPhoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, parishPhone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParishPhone> parishPhoneList = parishPhoneRepository.findAll();
        assertThat(parishPhoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
