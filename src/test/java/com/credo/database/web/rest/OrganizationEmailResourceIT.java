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
import com.credo.database.domain.Organization;
import com.credo.database.domain.OrganizationEmail;
import com.credo.database.domain.enumeration.YesNoEmpty;
import com.credo.database.repository.OrganizationEmailRepository;
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
 * Integration tests for the {@link OrganizationEmailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganizationEmailResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final YesNoEmpty DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION = YesNoEmpty.YES;
    private static final YesNoEmpty UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION = YesNoEmpty.NO;

    private static final YesNoEmpty DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION = YesNoEmpty.YES;
    private static final YesNoEmpty UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION = YesNoEmpty.NO;

    private static final String ENTITY_API_URL = "/api/organization-emails";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganizationEmailRepository organizationEmailRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationEmailMockMvc;

    private OrganizationEmail organizationEmail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationEmail createEntity(EntityManager em) {
        OrganizationEmail organizationEmail = new OrganizationEmail()
            .email(DEFAULT_EMAIL)
            .type(DEFAULT_TYPE)
            .emailNewsletterSubscription(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
        return organizationEmail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationEmail createUpdatedEntity(EntityManager em) {
        OrganizationEmail organizationEmail = new OrganizationEmail()
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .emailNewsletterSubscription(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
        return organizationEmail;
    }

    @BeforeEach
    public void initTest() {
        organizationEmail = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganizationEmail() throws Exception {
        int databaseSizeBeforeCreate = organizationEmailRepository.findAll().size();
        // Create the OrganizationEmail
        restOrganizationEmailMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationEmail))
            )
            .andExpect(status().isCreated());

        // Validate the OrganizationEmail in the database
        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationEmail testOrganizationEmail = organizationEmailList.get(organizationEmailList.size() - 1);
        assertThat(testOrganizationEmail.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOrganizationEmail.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOrganizationEmail.getEmailNewsletterSubscription()).isEqualTo(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testOrganizationEmail.getEmailEventNotificationSubscription()).isEqualTo(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void createOrganizationEmailWithExistingId() throws Exception {
        // Create the OrganizationEmail with an existing ID
        organizationEmail.setId(1L);

        int databaseSizeBeforeCreate = organizationEmailRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationEmailMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationEmail in the database
        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = organizationEmailRepository.findAll().size();
        // set the field null
        organizationEmail.setEmail(null);

        // Create the OrganizationEmail, which fails.

        restOrganizationEmailMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationEmail))
            )
            .andExpect(status().isBadRequest());

        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrganizationEmails() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList
        restOrganizationEmailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationEmail.getId().intValue())))
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
    void getOrganizationEmail() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get the organizationEmail
        restOrganizationEmailMockMvc
            .perform(get(ENTITY_API_URL_ID, organizationEmail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organizationEmail.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.emailNewsletterSubscription").value(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION.toString()))
            .andExpect(jsonPath("$.emailEventNotificationSubscription").value(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getOrganizationEmailsByIdFiltering() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        Long id = organizationEmail.getId();

        defaultOrganizationEmailShouldBeFound("id.equals=" + id);
        defaultOrganizationEmailShouldNotBeFound("id.notEquals=" + id);

        defaultOrganizationEmailShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrganizationEmailShouldNotBeFound("id.greaterThan=" + id);

        defaultOrganizationEmailShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrganizationEmailShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where email equals to DEFAULT_EMAIL
        defaultOrganizationEmailShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the organizationEmailList where email equals to UPDATED_EMAIL
        defaultOrganizationEmailShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where email not equals to DEFAULT_EMAIL
        defaultOrganizationEmailShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the organizationEmailList where email not equals to UPDATED_EMAIL
        defaultOrganizationEmailShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultOrganizationEmailShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the organizationEmailList where email equals to UPDATED_EMAIL
        defaultOrganizationEmailShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where email is not null
        defaultOrganizationEmailShouldBeFound("email.specified=true");

        // Get all the organizationEmailList where email is null
        defaultOrganizationEmailShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailContainsSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where email contains DEFAULT_EMAIL
        defaultOrganizationEmailShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the organizationEmailList where email contains UPDATED_EMAIL
        defaultOrganizationEmailShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where email does not contain DEFAULT_EMAIL
        defaultOrganizationEmailShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the organizationEmailList where email does not contain UPDATED_EMAIL
        defaultOrganizationEmailShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where type equals to DEFAULT_TYPE
        defaultOrganizationEmailShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the organizationEmailList where type equals to UPDATED_TYPE
        defaultOrganizationEmailShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where type not equals to DEFAULT_TYPE
        defaultOrganizationEmailShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the organizationEmailList where type not equals to UPDATED_TYPE
        defaultOrganizationEmailShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultOrganizationEmailShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the organizationEmailList where type equals to UPDATED_TYPE
        defaultOrganizationEmailShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where type is not null
        defaultOrganizationEmailShouldBeFound("type.specified=true");

        // Get all the organizationEmailList where type is null
        defaultOrganizationEmailShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByTypeContainsSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where type contains DEFAULT_TYPE
        defaultOrganizationEmailShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the organizationEmailList where type contains UPDATED_TYPE
        defaultOrganizationEmailShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where type does not contain DEFAULT_TYPE
        defaultOrganizationEmailShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the organizationEmailList where type does not contain UPDATED_TYPE
        defaultOrganizationEmailShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailNewsletterSubscriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where emailNewsletterSubscription equals to DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultOrganizationEmailShouldBeFound("emailNewsletterSubscription.equals=" + DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION);

        // Get all the organizationEmailList where emailNewsletterSubscription equals to UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultOrganizationEmailShouldNotBeFound("emailNewsletterSubscription.equals=" + UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailNewsletterSubscriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where emailNewsletterSubscription not equals to DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultOrganizationEmailShouldNotBeFound("emailNewsletterSubscription.notEquals=" + DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION);

        // Get all the organizationEmailList where emailNewsletterSubscription not equals to UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultOrganizationEmailShouldBeFound("emailNewsletterSubscription.notEquals=" + UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailNewsletterSubscriptionIsInShouldWork() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where emailNewsletterSubscription in DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION or UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultOrganizationEmailShouldBeFound(
            "emailNewsletterSubscription.in=" + DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION + "," + UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        );

        // Get all the organizationEmailList where emailNewsletterSubscription equals to UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultOrganizationEmailShouldNotBeFound("emailNewsletterSubscription.in=" + UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailNewsletterSubscriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where emailNewsletterSubscription is not null
        defaultOrganizationEmailShouldBeFound("emailNewsletterSubscription.specified=true");

        // Get all the organizationEmailList where emailNewsletterSubscription is null
        defaultOrganizationEmailShouldNotBeFound("emailNewsletterSubscription.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailEventNotificationSubscriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where emailEventNotificationSubscription equals to DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultOrganizationEmailShouldBeFound("emailEventNotificationSubscription.equals=" + DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        // Get all the organizationEmailList where emailEventNotificationSubscription equals to UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultOrganizationEmailShouldNotBeFound(
            "emailEventNotificationSubscription.equals=" + UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailEventNotificationSubscriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where emailEventNotificationSubscription not equals to DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultOrganizationEmailShouldNotBeFound(
            "emailEventNotificationSubscription.notEquals=" + DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        );

        // Get all the organizationEmailList where emailEventNotificationSubscription not equals to UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultOrganizationEmailShouldBeFound(
            "emailEventNotificationSubscription.notEquals=" + UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailEventNotificationSubscriptionIsInShouldWork() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where emailEventNotificationSubscription in DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION or UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultOrganizationEmailShouldBeFound(
            "emailEventNotificationSubscription.in=" +
            DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION +
            "," +
            UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        );

        // Get all the organizationEmailList where emailEventNotificationSubscription equals to UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultOrganizationEmailShouldNotBeFound("emailEventNotificationSubscription.in=" + UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByEmailEventNotificationSubscriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        // Get all the organizationEmailList where emailEventNotificationSubscription is not null
        defaultOrganizationEmailShouldBeFound("emailEventNotificationSubscription.specified=true");

        // Get all the organizationEmailList where emailEventNotificationSubscription is null
        defaultOrganizationEmailShouldNotBeFound("emailEventNotificationSubscription.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationEmailsByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);
        Organization organization = OrganizationResourceIT.createEntity(em);
        em.persist(organization);
        em.flush();
        organizationEmail.setOrganization(organization);
        organizationEmailRepository.saveAndFlush(organizationEmail);
        Long organizationId = organization.getId();

        // Get all the organizationEmailList where organization equals to organizationId
        defaultOrganizationEmailShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the organizationEmailList where organization equals to (organizationId + 1)
        defaultOrganizationEmailShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganizationEmailShouldBeFound(String filter) throws Exception {
        restOrganizationEmailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationEmail.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].emailNewsletterSubscription").value(hasItem(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION.toString())))
            .andExpect(
                jsonPath("$.[*].emailEventNotificationSubscription")
                    .value(hasItem(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restOrganizationEmailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganizationEmailShouldNotBeFound(String filter) throws Exception {
        restOrganizationEmailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganizationEmailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrganizationEmail() throws Exception {
        // Get the organizationEmail
        restOrganizationEmailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrganizationEmail() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        int databaseSizeBeforeUpdate = organizationEmailRepository.findAll().size();

        // Update the organizationEmail
        OrganizationEmail updatedOrganizationEmail = organizationEmailRepository.findById(organizationEmail.getId()).get();
        // Disconnect from session so that the updates on updatedOrganizationEmail are not directly saved in db
        em.detach(updatedOrganizationEmail);
        updatedOrganizationEmail
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .emailNewsletterSubscription(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        restOrganizationEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganizationEmail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrganizationEmail))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationEmail in the database
        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeUpdate);
        OrganizationEmail testOrganizationEmail = organizationEmailList.get(organizationEmailList.size() - 1);
        assertThat(testOrganizationEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrganizationEmail.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOrganizationEmail.getEmailNewsletterSubscription()).isEqualTo(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testOrganizationEmail.getEmailEventNotificationSubscription()).isEqualTo(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingOrganizationEmail() throws Exception {
        int databaseSizeBeforeUpdate = organizationEmailRepository.findAll().size();
        organizationEmail.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationEmail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationEmail in the database
        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganizationEmail() throws Exception {
        int databaseSizeBeforeUpdate = organizationEmailRepository.findAll().size();
        organizationEmail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationEmail in the database
        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganizationEmail() throws Exception {
        int databaseSizeBeforeUpdate = organizationEmailRepository.findAll().size();
        organizationEmail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationEmailMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationEmail))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationEmail in the database
        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganizationEmailWithPatch() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        int databaseSizeBeforeUpdate = organizationEmailRepository.findAll().size();

        // Update the organizationEmail using partial update
        OrganizationEmail partialUpdatedOrganizationEmail = new OrganizationEmail();
        partialUpdatedOrganizationEmail.setId(organizationEmail.getId());

        partialUpdatedOrganizationEmail.email(UPDATED_EMAIL);

        restOrganizationEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationEmail))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationEmail in the database
        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeUpdate);
        OrganizationEmail testOrganizationEmail = organizationEmailList.get(organizationEmailList.size() - 1);
        assertThat(testOrganizationEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrganizationEmail.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOrganizationEmail.getEmailNewsletterSubscription()).isEqualTo(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testOrganizationEmail.getEmailEventNotificationSubscription()).isEqualTo(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateOrganizationEmailWithPatch() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        int databaseSizeBeforeUpdate = organizationEmailRepository.findAll().size();

        // Update the organizationEmail using partial update
        OrganizationEmail partialUpdatedOrganizationEmail = new OrganizationEmail();
        partialUpdatedOrganizationEmail.setId(organizationEmail.getId());

        partialUpdatedOrganizationEmail
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .emailNewsletterSubscription(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        restOrganizationEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationEmail))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationEmail in the database
        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeUpdate);
        OrganizationEmail testOrganizationEmail = organizationEmailList.get(organizationEmailList.size() - 1);
        assertThat(testOrganizationEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOrganizationEmail.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOrganizationEmail.getEmailNewsletterSubscription()).isEqualTo(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testOrganizationEmail.getEmailEventNotificationSubscription()).isEqualTo(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingOrganizationEmail() throws Exception {
        int databaseSizeBeforeUpdate = organizationEmailRepository.findAll().size();
        organizationEmail.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organizationEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationEmail in the database
        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganizationEmail() throws Exception {
        int databaseSizeBeforeUpdate = organizationEmailRepository.findAll().size();
        organizationEmail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationEmail in the database
        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganizationEmail() throws Exception {
        int databaseSizeBeforeUpdate = organizationEmailRepository.findAll().size();
        organizationEmail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationEmailMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationEmail))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationEmail in the database
        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganizationEmail() throws Exception {
        // Initialize the database
        organizationEmailRepository.saveAndFlush(organizationEmail);

        int databaseSizeBeforeDelete = organizationEmailRepository.findAll().size();

        // Delete the organizationEmail
        restOrganizationEmailMockMvc
            .perform(delete(ENTITY_API_URL_ID, organizationEmail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganizationEmail> organizationEmailList = organizationEmailRepository.findAll();
        assertThat(organizationEmailList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
