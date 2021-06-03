package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.Organization;
import com.credo.database.domain.OrganizationAddress;
import com.credo.database.domain.OrganizationEmail;
import com.credo.database.domain.OrganizationNotes;
import com.credo.database.domain.OrganizationPhone;
import com.credo.database.domain.Parish;
import com.credo.database.domain.Person;
import com.credo.database.repository.OrganizationRepository;
import com.credo.database.service.criteria.OrganizationCriteria;
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
 * Integration tests for the {@link OrganizationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganizationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MAILING_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_MAILING_LABEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationMockMvc;

    private Organization organization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createEntity(EntityManager em) {
        Organization organization = new Organization().name(DEFAULT_NAME).mailingLabel(DEFAULT_MAILING_LABEL);
        return organization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createUpdatedEntity(EntityManager em) {
        Organization organization = new Organization().name(UPDATED_NAME).mailingLabel(UPDATED_MAILING_LABEL);
        return organization;
    }

    @BeforeEach
    public void initTest() {
        organization = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganization() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().size();
        // Create the Organization
        restOrganizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isCreated());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate + 1);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOrganization.getMailingLabel()).isEqualTo(DEFAULT_MAILING_LABEL);
    }

    @Test
    @Transactional
    void createOrganizationWithExistingId() throws Exception {
        // Create the Organization with an existing ID
        organization.setId(1L);

        int databaseSizeBeforeCreate = organizationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationRepository.findAll().size();
        // set the field null
        organization.setName(null);

        // Create the Organization, which fails.

        restOrganizationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isBadRequest());

        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganizations() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mailingLabel").value(hasItem(DEFAULT_MAILING_LABEL)));
    }

    @Test
    @Transactional
    void getOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get the organization
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL_ID, organization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organization.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.mailingLabel").value(DEFAULT_MAILING_LABEL));
    }

    @Test
    @Transactional
    void getOrganizationsByIdFiltering() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        Long id = organization.getId();

        defaultOrganizationShouldBeFound("id.equals=" + id);
        defaultOrganizationShouldNotBeFound("id.notEquals=" + id);

        defaultOrganizationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrganizationShouldNotBeFound("id.greaterThan=" + id);

        defaultOrganizationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrganizationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrganizationsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name equals to DEFAULT_NAME
        defaultOrganizationShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the organizationList where name equals to UPDATED_NAME
        defaultOrganizationShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name not equals to DEFAULT_NAME
        defaultOrganizationShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the organizationList where name not equals to UPDATED_NAME
        defaultOrganizationShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOrganizationShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the organizationList where name equals to UPDATED_NAME
        defaultOrganizationShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name is not null
        defaultOrganizationShouldBeFound("name.specified=true");

        // Get all the organizationList where name is null
        defaultOrganizationShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByNameContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name contains DEFAULT_NAME
        defaultOrganizationShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the organizationList where name contains UPDATED_NAME
        defaultOrganizationShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where name does not contain DEFAULT_NAME
        defaultOrganizationShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the organizationList where name does not contain UPDATED_NAME
        defaultOrganizationShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOrganizationsByMailingLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where mailingLabel equals to DEFAULT_MAILING_LABEL
        defaultOrganizationShouldBeFound("mailingLabel.equals=" + DEFAULT_MAILING_LABEL);

        // Get all the organizationList where mailingLabel equals to UPDATED_MAILING_LABEL
        defaultOrganizationShouldNotBeFound("mailingLabel.equals=" + UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByMailingLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where mailingLabel not equals to DEFAULT_MAILING_LABEL
        defaultOrganizationShouldNotBeFound("mailingLabel.notEquals=" + DEFAULT_MAILING_LABEL);

        // Get all the organizationList where mailingLabel not equals to UPDATED_MAILING_LABEL
        defaultOrganizationShouldBeFound("mailingLabel.notEquals=" + UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByMailingLabelIsInShouldWork() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where mailingLabel in DEFAULT_MAILING_LABEL or UPDATED_MAILING_LABEL
        defaultOrganizationShouldBeFound("mailingLabel.in=" + DEFAULT_MAILING_LABEL + "," + UPDATED_MAILING_LABEL);

        // Get all the organizationList where mailingLabel equals to UPDATED_MAILING_LABEL
        defaultOrganizationShouldNotBeFound("mailingLabel.in=" + UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByMailingLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where mailingLabel is not null
        defaultOrganizationShouldBeFound("mailingLabel.specified=true");

        // Get all the organizationList where mailingLabel is null
        defaultOrganizationShouldNotBeFound("mailingLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationsByMailingLabelContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where mailingLabel contains DEFAULT_MAILING_LABEL
        defaultOrganizationShouldBeFound("mailingLabel.contains=" + DEFAULT_MAILING_LABEL);

        // Get all the organizationList where mailingLabel contains UPDATED_MAILING_LABEL
        defaultOrganizationShouldNotBeFound("mailingLabel.contains=" + UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByMailingLabelNotContainsSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        // Get all the organizationList where mailingLabel does not contain DEFAULT_MAILING_LABEL
        defaultOrganizationShouldNotBeFound("mailingLabel.doesNotContain=" + DEFAULT_MAILING_LABEL);

        // Get all the organizationList where mailingLabel does not contain UPDATED_MAILING_LABEL
        defaultOrganizationShouldBeFound("mailingLabel.doesNotContain=" + UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void getAllOrganizationsByParishIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        Parish parish = ParishResourceIT.createEntity(em);
        em.persist(parish);
        em.flush();
        organization.setParish(parish);
        organizationRepository.saveAndFlush(organization);
        Long parishId = parish.getId();

        // Get all the organizationList where parish equals to parishId
        defaultOrganizationShouldBeFound("parishId.equals=" + parishId);

        // Get all the organizationList where parish equals to (parishId + 1)
        defaultOrganizationShouldNotBeFound("parishId.equals=" + (parishId + 1));
    }

    @Test
    @Transactional
    void getAllOrganizationsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        OrganizationNotes notes = OrganizationNotesResourceIT.createEntity(em);
        em.persist(notes);
        em.flush();
        organization.setNotes(notes);
        notes.setOrganization(organization);
        organizationRepository.saveAndFlush(organization);
        Long notesId = notes.getId();

        // Get all the organizationList where notes equals to notesId
        defaultOrganizationShouldBeFound("notesId.equals=" + notesId);

        // Get all the organizationList where notes equals to (notesId + 1)
        defaultOrganizationShouldNotBeFound("notesId.equals=" + (notesId + 1));
    }

    @Test
    @Transactional
    void getAllOrganizationsByAddressesIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        OrganizationAddress addresses = OrganizationAddressResourceIT.createEntity(em);
        em.persist(addresses);
        em.flush();
        organization.addAddresses(addresses);
        organizationRepository.saveAndFlush(organization);
        Long addressesId = addresses.getId();

        // Get all the organizationList where addresses equals to addressesId
        defaultOrganizationShouldBeFound("addressesId.equals=" + addressesId);

        // Get all the organizationList where addresses equals to (addressesId + 1)
        defaultOrganizationShouldNotBeFound("addressesId.equals=" + (addressesId + 1));
    }

    @Test
    @Transactional
    void getAllOrganizationsByPhonesIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        OrganizationPhone phones = OrganizationPhoneResourceIT.createEntity(em);
        em.persist(phones);
        em.flush();
        organization.addPhones(phones);
        organizationRepository.saveAndFlush(organization);
        Long phonesId = phones.getId();

        // Get all the organizationList where phones equals to phonesId
        defaultOrganizationShouldBeFound("phonesId.equals=" + phonesId);

        // Get all the organizationList where phones equals to (phonesId + 1)
        defaultOrganizationShouldNotBeFound("phonesId.equals=" + (phonesId + 1));
    }

    @Test
    @Transactional
    void getAllOrganizationsByEmailsIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        OrganizationEmail emails = OrganizationEmailResourceIT.createEntity(em);
        em.persist(emails);
        em.flush();
        organization.addEmails(emails);
        organizationRepository.saveAndFlush(organization);
        Long emailsId = emails.getId();

        // Get all the organizationList where emails equals to emailsId
        defaultOrganizationShouldBeFound("emailsId.equals=" + emailsId);

        // Get all the organizationList where emails equals to (emailsId + 1)
        defaultOrganizationShouldNotBeFound("emailsId.equals=" + (emailsId + 1));
    }

    @Test
    @Transactional
    void getAllOrganizationsByPersonsIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);
        Person persons = PersonResourceIT.createEntity(em);
        em.persist(persons);
        em.flush();
        organization.addPersons(persons);
        organizationRepository.saveAndFlush(organization);
        Long personsId = persons.getId();

        // Get all the organizationList where persons equals to personsId
        defaultOrganizationShouldBeFound("personsId.equals=" + personsId);

        // Get all the organizationList where persons equals to (personsId + 1)
        defaultOrganizationShouldNotBeFound("personsId.equals=" + (personsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganizationShouldBeFound(String filter) throws Exception {
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organization.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mailingLabel").value(hasItem(DEFAULT_MAILING_LABEL)));

        // Check, that the count call also returns 1
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganizationShouldNotBeFound(String filter) throws Exception {
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrganization() throws Exception {
        // Get the organization
        restOrganizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization
        Organization updatedOrganization = organizationRepository.findById(organization.getId()).get();
        // Disconnect from session so that the updates on updatedOrganization are not directly saved in db
        em.detach(updatedOrganization);
        updatedOrganization.name(UPDATED_NAME).mailingLabel(UPDATED_MAILING_LABEL);

        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganization.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrganization))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganization.getMailingLabel()).isEqualTo(UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void putNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organization.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organization))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organization))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organization)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganizationWithPatch() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization using partial update
        Organization partialUpdatedOrganization = new Organization();
        partialUpdatedOrganization.setId(organization.getId());

        partialUpdatedOrganization.name(UPDATED_NAME);

        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganization))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganization.getMailingLabel()).isEqualTo(DEFAULT_MAILING_LABEL);
    }

    @Test
    @Transactional
    void fullUpdateOrganizationWithPatch() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();

        // Update the organization using partial update
        Organization partialUpdatedOrganization = new Organization();
        partialUpdatedOrganization.setId(organization.getId());

        partialUpdatedOrganization.name(UPDATED_NAME).mailingLabel(UPDATED_MAILING_LABEL);

        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganization))
            )
            .andExpect(status().isOk());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
        assertThat(testOrganization.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOrganization.getMailingLabel()).isEqualTo(UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void patchNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organization))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organization))
            )
            .andExpect(status().isBadRequest());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().size();
        organization.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(organization))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganization() throws Exception {
        // Initialize the database
        organizationRepository.saveAndFlush(organization);

        int databaseSizeBeforeDelete = organizationRepository.findAll().size();

        // Delete the organization
        restOrganizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, organization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Organization> organizationList = organizationRepository.findAll();
        assertThat(organizationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
