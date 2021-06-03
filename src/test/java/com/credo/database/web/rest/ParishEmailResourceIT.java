package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.Parish;
import com.credo.database.domain.ParishEmail;
import com.credo.database.domain.enumeration.YesNoEmpty;
import com.credo.database.domain.enumeration.YesNoEmpty;
import com.credo.database.repository.ParishEmailRepository;
import com.credo.database.service.criteria.ParishEmailCriteria;
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
 * Integration tests for the {@link ParishEmailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParishEmailResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final YesNoEmpty DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION = YesNoEmpty.YES;
    private static final YesNoEmpty UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION = YesNoEmpty.NO;

    private static final YesNoEmpty DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION = YesNoEmpty.YES;
    private static final YesNoEmpty UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION = YesNoEmpty.NO;

    private static final String ENTITY_API_URL = "/api/parish-emails";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParishEmailRepository parishEmailRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParishEmailMockMvc;

    private ParishEmail parishEmail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParishEmail createEntity(EntityManager em) {
        ParishEmail parishEmail = new ParishEmail()
            .email(DEFAULT_EMAIL)
            .type(DEFAULT_TYPE)
            .emailNewsletterSubscription(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
        return parishEmail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ParishEmail createUpdatedEntity(EntityManager em) {
        ParishEmail parishEmail = new ParishEmail()
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .emailNewsletterSubscription(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
        return parishEmail;
    }

    @BeforeEach
    public void initTest() {
        parishEmail = createEntity(em);
    }

    @Test
    @Transactional
    void createParishEmail() throws Exception {
        int databaseSizeBeforeCreate = parishEmailRepository.findAll().size();
        // Create the ParishEmail
        restParishEmailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parishEmail)))
            .andExpect(status().isCreated());

        // Validate the ParishEmail in the database
        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeCreate + 1);
        ParishEmail testParishEmail = parishEmailList.get(parishEmailList.size() - 1);
        assertThat(testParishEmail.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testParishEmail.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testParishEmail.getEmailNewsletterSubscription()).isEqualTo(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testParishEmail.getEmailEventNotificationSubscription()).isEqualTo(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void createParishEmailWithExistingId() throws Exception {
        // Create the ParishEmail with an existing ID
        parishEmail.setId(1L);

        int databaseSizeBeforeCreate = parishEmailRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParishEmailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parishEmail)))
            .andExpect(status().isBadRequest());

        // Validate the ParishEmail in the database
        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = parishEmailRepository.findAll().size();
        // set the field null
        parishEmail.setEmail(null);

        // Create the ParishEmail, which fails.

        restParishEmailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parishEmail)))
            .andExpect(status().isBadRequest());

        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllParishEmails() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList
        restParishEmailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parishEmail.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].emailNewsletterSubscription").value(hasItem(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION.toString())))
            .andExpect(
                jsonPath("$.[*].emailEventNotificationSubscription")
                    .value(hasItem(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION.toString()))
            );
    }

    @Test
    @Transactional
    void getParishEmail() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get the parishEmail
        restParishEmailMockMvc
            .perform(get(ENTITY_API_URL_ID, parishEmail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(parishEmail.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.emailNewsletterSubscription").value(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION.toString()))
            .andExpect(jsonPath("$.emailEventNotificationSubscription").value(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getParishEmailsByIdFiltering() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        Long id = parishEmail.getId();

        defaultParishEmailShouldBeFound("id.equals=" + id);
        defaultParishEmailShouldNotBeFound("id.notEquals=" + id);

        defaultParishEmailShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParishEmailShouldNotBeFound("id.greaterThan=" + id);

        defaultParishEmailShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParishEmailShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where email equals to DEFAULT_EMAIL
        defaultParishEmailShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the parishEmailList where email equals to UPDATED_EMAIL
        defaultParishEmailShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where email not equals to DEFAULT_EMAIL
        defaultParishEmailShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the parishEmailList where email not equals to UPDATED_EMAIL
        defaultParishEmailShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultParishEmailShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the parishEmailList where email equals to UPDATED_EMAIL
        defaultParishEmailShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where email is not null
        defaultParishEmailShouldBeFound("email.specified=true");

        // Get all the parishEmailList where email is null
        defaultParishEmailShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailContainsSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where email contains DEFAULT_EMAIL
        defaultParishEmailShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the parishEmailList where email contains UPDATED_EMAIL
        defaultParishEmailShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where email does not contain DEFAULT_EMAIL
        defaultParishEmailShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the parishEmailList where email does not contain UPDATED_EMAIL
        defaultParishEmailShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllParishEmailsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where type equals to DEFAULT_TYPE
        defaultParishEmailShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the parishEmailList where type equals to UPDATED_TYPE
        defaultParishEmailShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllParishEmailsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where type not equals to DEFAULT_TYPE
        defaultParishEmailShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the parishEmailList where type not equals to UPDATED_TYPE
        defaultParishEmailShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllParishEmailsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultParishEmailShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the parishEmailList where type equals to UPDATED_TYPE
        defaultParishEmailShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllParishEmailsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where type is not null
        defaultParishEmailShouldBeFound("type.specified=true");

        // Get all the parishEmailList where type is null
        defaultParishEmailShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllParishEmailsByTypeContainsSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where type contains DEFAULT_TYPE
        defaultParishEmailShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the parishEmailList where type contains UPDATED_TYPE
        defaultParishEmailShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllParishEmailsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where type does not contain DEFAULT_TYPE
        defaultParishEmailShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the parishEmailList where type does not contain UPDATED_TYPE
        defaultParishEmailShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailNewsletterSubscriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where emailNewsletterSubscription equals to DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultParishEmailShouldBeFound("emailNewsletterSubscription.equals=" + DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION);

        // Get all the parishEmailList where emailNewsletterSubscription equals to UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultParishEmailShouldNotBeFound("emailNewsletterSubscription.equals=" + UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailNewsletterSubscriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where emailNewsletterSubscription not equals to DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultParishEmailShouldNotBeFound("emailNewsletterSubscription.notEquals=" + DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION);

        // Get all the parishEmailList where emailNewsletterSubscription not equals to UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultParishEmailShouldBeFound("emailNewsletterSubscription.notEquals=" + UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailNewsletterSubscriptionIsInShouldWork() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where emailNewsletterSubscription in DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION or UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultParishEmailShouldBeFound(
            "emailNewsletterSubscription.in=" + DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION + "," + UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        );

        // Get all the parishEmailList where emailNewsletterSubscription equals to UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultParishEmailShouldNotBeFound("emailNewsletterSubscription.in=" + UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailNewsletterSubscriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where emailNewsletterSubscription is not null
        defaultParishEmailShouldBeFound("emailNewsletterSubscription.specified=true");

        // Get all the parishEmailList where emailNewsletterSubscription is null
        defaultParishEmailShouldNotBeFound("emailNewsletterSubscription.specified=false");
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailEventNotificationSubscriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where emailEventNotificationSubscription equals to DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultParishEmailShouldBeFound("emailEventNotificationSubscription.equals=" + DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        // Get all the parishEmailList where emailEventNotificationSubscription equals to UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultParishEmailShouldNotBeFound("emailEventNotificationSubscription.equals=" + UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailEventNotificationSubscriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where emailEventNotificationSubscription not equals to DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultParishEmailShouldNotBeFound("emailEventNotificationSubscription.notEquals=" + DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        // Get all the parishEmailList where emailEventNotificationSubscription not equals to UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultParishEmailShouldBeFound("emailEventNotificationSubscription.notEquals=" + UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailEventNotificationSubscriptionIsInShouldWork() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where emailEventNotificationSubscription in DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION or UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultParishEmailShouldBeFound(
            "emailEventNotificationSubscription.in=" +
            DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION +
            "," +
            UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        );

        // Get all the parishEmailList where emailEventNotificationSubscription equals to UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultParishEmailShouldNotBeFound("emailEventNotificationSubscription.in=" + UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllParishEmailsByEmailEventNotificationSubscriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        // Get all the parishEmailList where emailEventNotificationSubscription is not null
        defaultParishEmailShouldBeFound("emailEventNotificationSubscription.specified=true");

        // Get all the parishEmailList where emailEventNotificationSubscription is null
        defaultParishEmailShouldNotBeFound("emailEventNotificationSubscription.specified=false");
    }

    @Test
    @Transactional
    void getAllParishEmailsByParishIsEqualToSomething() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);
        Parish parish = ParishResourceIT.createEntity(em);
        em.persist(parish);
        em.flush();
        parishEmail.setParish(parish);
        parishEmailRepository.saveAndFlush(parishEmail);
        Long parishId = parish.getId();

        // Get all the parishEmailList where parish equals to parishId
        defaultParishEmailShouldBeFound("parishId.equals=" + parishId);

        // Get all the parishEmailList where parish equals to (parishId + 1)
        defaultParishEmailShouldNotBeFound("parishId.equals=" + (parishId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParishEmailShouldBeFound(String filter) throws Exception {
        restParishEmailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parishEmail.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].emailNewsletterSubscription").value(hasItem(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION.toString())))
            .andExpect(
                jsonPath("$.[*].emailEventNotificationSubscription")
                    .value(hasItem(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restParishEmailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParishEmailShouldNotBeFound(String filter) throws Exception {
        restParishEmailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParishEmailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingParishEmail() throws Exception {
        // Get the parishEmail
        restParishEmailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewParishEmail() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        int databaseSizeBeforeUpdate = parishEmailRepository.findAll().size();

        // Update the parishEmail
        ParishEmail updatedParishEmail = parishEmailRepository.findById(parishEmail.getId()).get();
        // Disconnect from session so that the updates on updatedParishEmail are not directly saved in db
        em.detach(updatedParishEmail);
        updatedParishEmail
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .emailNewsletterSubscription(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        restParishEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedParishEmail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedParishEmail))
            )
            .andExpect(status().isOk());

        // Validate the ParishEmail in the database
        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeUpdate);
        ParishEmail testParishEmail = parishEmailList.get(parishEmailList.size() - 1);
        assertThat(testParishEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testParishEmail.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testParishEmail.getEmailNewsletterSubscription()).isEqualTo(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testParishEmail.getEmailEventNotificationSubscription()).isEqualTo(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingParishEmail() throws Exception {
        int databaseSizeBeforeUpdate = parishEmailRepository.findAll().size();
        parishEmail.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParishEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parishEmail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parishEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParishEmail in the database
        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchParishEmail() throws Exception {
        int databaseSizeBeforeUpdate = parishEmailRepository.findAll().size();
        parishEmail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParishEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parishEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParishEmail in the database
        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamParishEmail() throws Exception {
        int databaseSizeBeforeUpdate = parishEmailRepository.findAll().size();
        parishEmail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParishEmailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parishEmail)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParishEmail in the database
        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParishEmailWithPatch() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        int databaseSizeBeforeUpdate = parishEmailRepository.findAll().size();

        // Update the parishEmail using partial update
        ParishEmail partialUpdatedParishEmail = new ParishEmail();
        partialUpdatedParishEmail.setId(parishEmail.getId());

        partialUpdatedParishEmail
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .emailNewsletterSubscription(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        restParishEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParishEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParishEmail))
            )
            .andExpect(status().isOk());

        // Validate the ParishEmail in the database
        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeUpdate);
        ParishEmail testParishEmail = parishEmailList.get(parishEmailList.size() - 1);
        assertThat(testParishEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testParishEmail.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testParishEmail.getEmailNewsletterSubscription()).isEqualTo(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testParishEmail.getEmailEventNotificationSubscription()).isEqualTo(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateParishEmailWithPatch() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        int databaseSizeBeforeUpdate = parishEmailRepository.findAll().size();

        // Update the parishEmail using partial update
        ParishEmail partialUpdatedParishEmail = new ParishEmail();
        partialUpdatedParishEmail.setId(parishEmail.getId());

        partialUpdatedParishEmail
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .emailNewsletterSubscription(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        restParishEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedParishEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedParishEmail))
            )
            .andExpect(status().isOk());

        // Validate the ParishEmail in the database
        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeUpdate);
        ParishEmail testParishEmail = parishEmailList.get(parishEmailList.size() - 1);
        assertThat(testParishEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testParishEmail.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testParishEmail.getEmailNewsletterSubscription()).isEqualTo(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testParishEmail.getEmailEventNotificationSubscription()).isEqualTo(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingParishEmail() throws Exception {
        int databaseSizeBeforeUpdate = parishEmailRepository.findAll().size();
        parishEmail.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParishEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parishEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parishEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParishEmail in the database
        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchParishEmail() throws Exception {
        int databaseSizeBeforeUpdate = parishEmailRepository.findAll().size();
        parishEmail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParishEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parishEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the ParishEmail in the database
        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamParishEmail() throws Exception {
        int databaseSizeBeforeUpdate = parishEmailRepository.findAll().size();
        parishEmail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParishEmailMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parishEmail))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ParishEmail in the database
        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteParishEmail() throws Exception {
        // Initialize the database
        parishEmailRepository.saveAndFlush(parishEmail);

        int databaseSizeBeforeDelete = parishEmailRepository.findAll().size();

        // Delete the parishEmail
        restParishEmailMockMvc
            .perform(delete(ENTITY_API_URL_ID, parishEmail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ParishEmail> parishEmailList = parishEmailRepository.findAll();
        assertThat(parishEmailList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
