package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.Organization;
import com.credo.database.domain.OrganizationPhone;
import com.credo.database.repository.OrganizationPhoneRepository;
import com.credo.database.service.criteria.OrganizationPhoneCriteria;
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
 * Integration tests for the {@link OrganizationPhoneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganizationPhoneResourceIT {

    private static final String DEFAULT_PHONE_NUMBER = "(713) 108-5165";
    private static final String UPDATED_PHONE_NUMBER = "(301) 776-4472";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organization-phones";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganizationPhoneRepository organizationPhoneRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationPhoneMockMvc;

    private OrganizationPhone organizationPhone;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationPhone createEntity(EntityManager em) {
        OrganizationPhone organizationPhone = new OrganizationPhone().phoneNumber(DEFAULT_PHONE_NUMBER).type(DEFAULT_TYPE);
        return organizationPhone;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationPhone createUpdatedEntity(EntityManager em) {
        OrganizationPhone organizationPhone = new OrganizationPhone().phoneNumber(UPDATED_PHONE_NUMBER).type(UPDATED_TYPE);
        return organizationPhone;
    }

    @BeforeEach
    public void initTest() {
        organizationPhone = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganizationPhone() throws Exception {
        int databaseSizeBeforeCreate = organizationPhoneRepository.findAll().size();
        // Create the OrganizationPhone
        restOrganizationPhoneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationPhone))
            )
            .andExpect(status().isCreated());

        // Validate the OrganizationPhone in the database
        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationPhone testOrganizationPhone = organizationPhoneList.get(organizationPhoneList.size() - 1);
        assertThat(testOrganizationPhone.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testOrganizationPhone.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createOrganizationPhoneWithExistingId() throws Exception {
        // Create the OrganizationPhone with an existing ID
        organizationPhone.setId(1L);

        int databaseSizeBeforeCreate = organizationPhoneRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationPhoneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationPhone in the database
        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationPhoneRepository.findAll().size();
        // set the field null
        organizationPhone.setPhoneNumber(null);

        // Create the OrganizationPhone, which fails.

        restOrganizationPhoneMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationPhone))
            )
            .andExpect(status().isBadRequest());

        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganizationPhones() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList
        restOrganizationPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationPhone.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getOrganizationPhone() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get the organizationPhone
        restOrganizationPhoneMockMvc
            .perform(get(ENTITY_API_URL_ID, organizationPhone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organizationPhone.getId().intValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getOrganizationPhonesByIdFiltering() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        Long id = organizationPhone.getId();

        defaultOrganizationPhoneShouldBeFound("id.equals=" + id);
        defaultOrganizationPhoneShouldNotBeFound("id.notEquals=" + id);

        defaultOrganizationPhoneShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrganizationPhoneShouldNotBeFound("id.greaterThan=" + id);

        defaultOrganizationPhoneShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrganizationPhoneShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultOrganizationPhoneShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the organizationPhoneList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultOrganizationPhoneShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByPhoneNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList where phoneNumber not equals to DEFAULT_PHONE_NUMBER
        defaultOrganizationPhoneShouldNotBeFound("phoneNumber.notEquals=" + DEFAULT_PHONE_NUMBER);

        // Get all the organizationPhoneList where phoneNumber not equals to UPDATED_PHONE_NUMBER
        defaultOrganizationPhoneShouldBeFound("phoneNumber.notEquals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultOrganizationPhoneShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the organizationPhoneList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultOrganizationPhoneShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList where phoneNumber is not null
        defaultOrganizationPhoneShouldBeFound("phoneNumber.specified=true");

        // Get all the organizationPhoneList where phoneNumber is null
        defaultOrganizationPhoneShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList where phoneNumber contains DEFAULT_PHONE_NUMBER
        defaultOrganizationPhoneShouldBeFound("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER);

        // Get all the organizationPhoneList where phoneNumber contains UPDATED_PHONE_NUMBER
        defaultOrganizationPhoneShouldNotBeFound("phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList where phoneNumber does not contain DEFAULT_PHONE_NUMBER
        defaultOrganizationPhoneShouldNotBeFound("phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);

        // Get all the organizationPhoneList where phoneNumber does not contain UPDATED_PHONE_NUMBER
        defaultOrganizationPhoneShouldBeFound("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList where type equals to DEFAULT_TYPE
        defaultOrganizationPhoneShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the organizationPhoneList where type equals to UPDATED_TYPE
        defaultOrganizationPhoneShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList where type not equals to DEFAULT_TYPE
        defaultOrganizationPhoneShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the organizationPhoneList where type not equals to UPDATED_TYPE
        defaultOrganizationPhoneShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultOrganizationPhoneShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the organizationPhoneList where type equals to UPDATED_TYPE
        defaultOrganizationPhoneShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList where type is not null
        defaultOrganizationPhoneShouldBeFound("type.specified=true");

        // Get all the organizationPhoneList where type is null
        defaultOrganizationPhoneShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByTypeContainsSomething() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList where type contains DEFAULT_TYPE
        defaultOrganizationPhoneShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the organizationPhoneList where type contains UPDATED_TYPE
        defaultOrganizationPhoneShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        // Get all the organizationPhoneList where type does not contain DEFAULT_TYPE
        defaultOrganizationPhoneShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the organizationPhoneList where type does not contain UPDATED_TYPE
        defaultOrganizationPhoneShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOrganizationPhonesByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);
        Organization organization = OrganizationResourceIT.createEntity(em);
        em.persist(organization);
        em.flush();
        organizationPhone.setOrganization(organization);
        organizationPhoneRepository.saveAndFlush(organizationPhone);
        Long organizationId = organization.getId();

        // Get all the organizationPhoneList where organization equals to organizationId
        defaultOrganizationPhoneShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the organizationPhoneList where organization equals to (organizationId + 1)
        defaultOrganizationPhoneShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganizationPhoneShouldBeFound(String filter) throws Exception {
        restOrganizationPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationPhone.getId().intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restOrganizationPhoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganizationPhoneShouldNotBeFound(String filter) throws Exception {
        restOrganizationPhoneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganizationPhoneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrganizationPhone() throws Exception {
        // Get the organizationPhone
        restOrganizationPhoneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrganizationPhone() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        int databaseSizeBeforeUpdate = organizationPhoneRepository.findAll().size();

        // Update the organizationPhone
        OrganizationPhone updatedOrganizationPhone = organizationPhoneRepository.findById(organizationPhone.getId()).get();
        // Disconnect from session so that the updates on updatedOrganizationPhone are not directly saved in db
        em.detach(updatedOrganizationPhone);
        updatedOrganizationPhone.phoneNumber(UPDATED_PHONE_NUMBER).type(UPDATED_TYPE);

        restOrganizationPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganizationPhone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrganizationPhone))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationPhone in the database
        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeUpdate);
        OrganizationPhone testOrganizationPhone = organizationPhoneList.get(organizationPhoneList.size() - 1);
        assertThat(testOrganizationPhone.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOrganizationPhone.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingOrganizationPhone() throws Exception {
        int databaseSizeBeforeUpdate = organizationPhoneRepository.findAll().size();
        organizationPhone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationPhone.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationPhone in the database
        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganizationPhone() throws Exception {
        int databaseSizeBeforeUpdate = organizationPhoneRepository.findAll().size();
        organizationPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationPhoneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationPhone in the database
        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganizationPhone() throws Exception {
        int databaseSizeBeforeUpdate = organizationPhoneRepository.findAll().size();
        organizationPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationPhoneMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationPhone))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationPhone in the database
        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganizationPhoneWithPatch() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        int databaseSizeBeforeUpdate = organizationPhoneRepository.findAll().size();

        // Update the organizationPhone using partial update
        OrganizationPhone partialUpdatedOrganizationPhone = new OrganizationPhone();
        partialUpdatedOrganizationPhone.setId(organizationPhone.getId());

        partialUpdatedOrganizationPhone.type(UPDATED_TYPE);

        restOrganizationPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationPhone))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationPhone in the database
        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeUpdate);
        OrganizationPhone testOrganizationPhone = organizationPhoneList.get(organizationPhoneList.size() - 1);
        assertThat(testOrganizationPhone.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testOrganizationPhone.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateOrganizationPhoneWithPatch() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        int databaseSizeBeforeUpdate = organizationPhoneRepository.findAll().size();

        // Update the organizationPhone using partial update
        OrganizationPhone partialUpdatedOrganizationPhone = new OrganizationPhone();
        partialUpdatedOrganizationPhone.setId(organizationPhone.getId());

        partialUpdatedOrganizationPhone.phoneNumber(UPDATED_PHONE_NUMBER).type(UPDATED_TYPE);

        restOrganizationPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationPhone))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationPhone in the database
        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeUpdate);
        OrganizationPhone testOrganizationPhone = organizationPhoneList.get(organizationPhoneList.size() - 1);
        assertThat(testOrganizationPhone.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOrganizationPhone.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingOrganizationPhone() throws Exception {
        int databaseSizeBeforeUpdate = organizationPhoneRepository.findAll().size();
        organizationPhone.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organizationPhone.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationPhone in the database
        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganizationPhone() throws Exception {
        int databaseSizeBeforeUpdate = organizationPhoneRepository.findAll().size();
        organizationPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationPhone))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationPhone in the database
        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganizationPhone() throws Exception {
        int databaseSizeBeforeUpdate = organizationPhoneRepository.findAll().size();
        organizationPhone.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationPhoneMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationPhone))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationPhone in the database
        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganizationPhone() throws Exception {
        // Initialize the database
        organizationPhoneRepository.saveAndFlush(organizationPhone);

        int databaseSizeBeforeDelete = organizationPhoneRepository.findAll().size();

        // Delete the organizationPhone
        restOrganizationPhoneMockMvc
            .perform(delete(ENTITY_API_URL_ID, organizationPhone.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganizationPhone> organizationPhoneList = organizationPhoneRepository.findAll();
        assertThat(organizationPhoneList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
