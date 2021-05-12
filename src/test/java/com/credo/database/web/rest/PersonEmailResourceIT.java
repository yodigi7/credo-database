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
import com.credo.database.domain.PersonEmail;
import com.credo.database.domain.enumeration.YesNoEmpty;
import com.credo.database.repository.PersonEmailRepository;
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
 * Integration tests for the {@link PersonEmailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonEmailResourceIT {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final YesNoEmpty DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION = YesNoEmpty.YES;
    private static final YesNoEmpty UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION = YesNoEmpty.NO;

    private static final YesNoEmpty DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION = YesNoEmpty.YES;
    private static final YesNoEmpty UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION = YesNoEmpty.NO;

    private static final String ENTITY_API_URL = "/api/person-emails";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonEmailRepository personEmailRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonEmailMockMvc;

    private PersonEmail personEmail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonEmail createEntity(EntityManager em) {
        PersonEmail personEmail = new PersonEmail()
            .email(DEFAULT_EMAIL)
            .type(DEFAULT_TYPE)
            .emailNewsletterSubscription(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
        return personEmail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonEmail createUpdatedEntity(EntityManager em) {
        PersonEmail personEmail = new PersonEmail()
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .emailNewsletterSubscription(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
        return personEmail;
    }

    @BeforeEach
    public void initTest() {
        personEmail = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonEmail() throws Exception {
        int databaseSizeBeforeCreate = personEmailRepository.findAll().size();
        // Create the PersonEmail
        restPersonEmailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personEmail)))
            .andExpect(status().isCreated());

        // Validate the PersonEmail in the database
        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeCreate + 1);
        PersonEmail testPersonEmail = personEmailList.get(personEmailList.size() - 1);
        assertThat(testPersonEmail.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPersonEmail.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testPersonEmail.getEmailNewsletterSubscription()).isEqualTo(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testPersonEmail.getEmailEventNotificationSubscription()).isEqualTo(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void createPersonEmailWithExistingId() throws Exception {
        // Create the PersonEmail with an existing ID
        personEmail.setId(1L);

        int databaseSizeBeforeCreate = personEmailRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonEmailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personEmail)))
            .andExpect(status().isBadRequest());

        // Validate the PersonEmail in the database
        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = personEmailRepository.findAll().size();
        // set the field null
        personEmail.setEmail(null);

        // Create the PersonEmail, which fails.

        restPersonEmailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personEmail)))
            .andExpect(status().isBadRequest());

        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonEmails() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList
        restPersonEmailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personEmail.getId().intValue())))
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
    void getPersonEmail() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get the personEmail
        restPersonEmailMockMvc
            .perform(get(ENTITY_API_URL_ID, personEmail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personEmail.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.emailNewsletterSubscription").value(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION.toString()))
            .andExpect(jsonPath("$.emailEventNotificationSubscription").value(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getPersonEmailsByIdFiltering() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        Long id = personEmail.getId();

        defaultPersonEmailShouldBeFound("id.equals=" + id);
        defaultPersonEmailShouldNotBeFound("id.notEquals=" + id);

        defaultPersonEmailShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonEmailShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonEmailShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonEmailShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where email equals to DEFAULT_EMAIL
        defaultPersonEmailShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the personEmailList where email equals to UPDATED_EMAIL
        defaultPersonEmailShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where email not equals to DEFAULT_EMAIL
        defaultPersonEmailShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the personEmailList where email not equals to UPDATED_EMAIL
        defaultPersonEmailShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultPersonEmailShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the personEmailList where email equals to UPDATED_EMAIL
        defaultPersonEmailShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where email is not null
        defaultPersonEmailShouldBeFound("email.specified=true");

        // Get all the personEmailList where email is null
        defaultPersonEmailShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailContainsSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where email contains DEFAULT_EMAIL
        defaultPersonEmailShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the personEmailList where email contains UPDATED_EMAIL
        defaultPersonEmailShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where email does not contain DEFAULT_EMAIL
        defaultPersonEmailShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the personEmailList where email does not contain UPDATED_EMAIL
        defaultPersonEmailShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where type equals to DEFAULT_TYPE
        defaultPersonEmailShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the personEmailList where type equals to UPDATED_TYPE
        defaultPersonEmailShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where type not equals to DEFAULT_TYPE
        defaultPersonEmailShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the personEmailList where type not equals to UPDATED_TYPE
        defaultPersonEmailShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultPersonEmailShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the personEmailList where type equals to UPDATED_TYPE
        defaultPersonEmailShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where type is not null
        defaultPersonEmailShouldBeFound("type.specified=true");

        // Get all the personEmailList where type is null
        defaultPersonEmailShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonEmailsByTypeContainsSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where type contains DEFAULT_TYPE
        defaultPersonEmailShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the personEmailList where type contains UPDATED_TYPE
        defaultPersonEmailShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where type does not contain DEFAULT_TYPE
        defaultPersonEmailShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the personEmailList where type does not contain UPDATED_TYPE
        defaultPersonEmailShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailNewsletterSubscriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where emailNewsletterSubscription equals to DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultPersonEmailShouldBeFound("emailNewsletterSubscription.equals=" + DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION);

        // Get all the personEmailList where emailNewsletterSubscription equals to UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultPersonEmailShouldNotBeFound("emailNewsletterSubscription.equals=" + UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailNewsletterSubscriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where emailNewsletterSubscription not equals to DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultPersonEmailShouldNotBeFound("emailNewsletterSubscription.notEquals=" + DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION);

        // Get all the personEmailList where emailNewsletterSubscription not equals to UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultPersonEmailShouldBeFound("emailNewsletterSubscription.notEquals=" + UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailNewsletterSubscriptionIsInShouldWork() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where emailNewsletterSubscription in DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION or UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultPersonEmailShouldBeFound(
            "emailNewsletterSubscription.in=" + DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION + "," + UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        );

        // Get all the personEmailList where emailNewsletterSubscription equals to UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION
        defaultPersonEmailShouldNotBeFound("emailNewsletterSubscription.in=" + UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailNewsletterSubscriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where emailNewsletterSubscription is not null
        defaultPersonEmailShouldBeFound("emailNewsletterSubscription.specified=true");

        // Get all the personEmailList where emailNewsletterSubscription is null
        defaultPersonEmailShouldNotBeFound("emailNewsletterSubscription.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailEventNotificationSubscriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where emailEventNotificationSubscription equals to DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultPersonEmailShouldBeFound("emailEventNotificationSubscription.equals=" + DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        // Get all the personEmailList where emailEventNotificationSubscription equals to UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultPersonEmailShouldNotBeFound("emailEventNotificationSubscription.equals=" + UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailEventNotificationSubscriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where emailEventNotificationSubscription not equals to DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultPersonEmailShouldNotBeFound("emailEventNotificationSubscription.notEquals=" + DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        // Get all the personEmailList where emailEventNotificationSubscription not equals to UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultPersonEmailShouldBeFound("emailEventNotificationSubscription.notEquals=" + UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailEventNotificationSubscriptionIsInShouldWork() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where emailEventNotificationSubscription in DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION or UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultPersonEmailShouldBeFound(
            "emailEventNotificationSubscription.in=" +
            DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION +
            "," +
            UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        );

        // Get all the personEmailList where emailEventNotificationSubscription equals to UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultPersonEmailShouldNotBeFound("emailEventNotificationSubscription.in=" + UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllPersonEmailsByEmailEventNotificationSubscriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        // Get all the personEmailList where emailEventNotificationSubscription is not null
        defaultPersonEmailShouldBeFound("emailEventNotificationSubscription.specified=true");

        // Get all the personEmailList where emailEventNotificationSubscription is null
        defaultPersonEmailShouldNotBeFound("emailEventNotificationSubscription.specified=false");
    }

    @Test
    @Transactional
    void getAllPersonEmailsByPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);
        Person person = PersonResourceIT.createEntity(em);
        em.persist(person);
        em.flush();
        personEmail.setPerson(person);
        personEmailRepository.saveAndFlush(personEmail);
        Long personId = person.getId();

        // Get all the personEmailList where person equals to personId
        defaultPersonEmailShouldBeFound("personId.equals=" + personId);

        // Get all the personEmailList where person equals to (personId + 1)
        defaultPersonEmailShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonEmailShouldBeFound(String filter) throws Exception {
        restPersonEmailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personEmail.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].emailNewsletterSubscription").value(hasItem(DEFAULT_EMAIL_NEWSLETTER_SUBSCRIPTION.toString())))
            .andExpect(
                jsonPath("$.[*].emailEventNotificationSubscription")
                    .value(hasItem(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restPersonEmailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonEmailShouldNotBeFound(String filter) throws Exception {
        restPersonEmailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonEmailMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPersonEmail() throws Exception {
        // Get the personEmail
        restPersonEmailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonEmail() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        int databaseSizeBeforeUpdate = personEmailRepository.findAll().size();

        // Update the personEmail
        PersonEmail updatedPersonEmail = personEmailRepository.findById(personEmail.getId()).get();
        // Disconnect from session so that the updates on updatedPersonEmail are not directly saved in db
        em.detach(updatedPersonEmail);
        updatedPersonEmail
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .emailNewsletterSubscription(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        restPersonEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonEmail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonEmail))
            )
            .andExpect(status().isOk());

        // Validate the PersonEmail in the database
        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeUpdate);
        PersonEmail testPersonEmail = personEmailList.get(personEmailList.size() - 1);
        assertThat(testPersonEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersonEmail.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPersonEmail.getEmailNewsletterSubscription()).isEqualTo(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testPersonEmail.getEmailEventNotificationSubscription()).isEqualTo(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingPersonEmail() throws Exception {
        int databaseSizeBeforeUpdate = personEmailRepository.findAll().size();
        personEmail.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personEmail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonEmail in the database
        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonEmail() throws Exception {
        int databaseSizeBeforeUpdate = personEmailRepository.findAll().size();
        personEmail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonEmailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonEmail in the database
        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonEmail() throws Exception {
        int databaseSizeBeforeUpdate = personEmailRepository.findAll().size();
        personEmail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonEmailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personEmail)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonEmail in the database
        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonEmailWithPatch() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        int databaseSizeBeforeUpdate = personEmailRepository.findAll().size();

        // Update the personEmail using partial update
        PersonEmail partialUpdatedPersonEmail = new PersonEmail();
        partialUpdatedPersonEmail.setId(personEmail.getId());

        partialUpdatedPersonEmail.type(UPDATED_TYPE).emailNewsletterSubscription(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);

        restPersonEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonEmail))
            )
            .andExpect(status().isOk());

        // Validate the PersonEmail in the database
        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeUpdate);
        PersonEmail testPersonEmail = personEmailList.get(personEmailList.size() - 1);
        assertThat(testPersonEmail.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPersonEmail.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPersonEmail.getEmailNewsletterSubscription()).isEqualTo(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testPersonEmail.getEmailEventNotificationSubscription()).isEqualTo(DEFAULT_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePersonEmailWithPatch() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        int databaseSizeBeforeUpdate = personEmailRepository.findAll().size();

        // Update the personEmail using partial update
        PersonEmail partialUpdatedPersonEmail = new PersonEmail();
        partialUpdatedPersonEmail.setId(personEmail.getId());

        partialUpdatedPersonEmail
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .emailNewsletterSubscription(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION)
            .emailEventNotificationSubscription(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        restPersonEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonEmail))
            )
            .andExpect(status().isOk());

        // Validate the PersonEmail in the database
        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeUpdate);
        PersonEmail testPersonEmail = personEmailList.get(personEmailList.size() - 1);
        assertThat(testPersonEmail.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPersonEmail.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testPersonEmail.getEmailNewsletterSubscription()).isEqualTo(UPDATED_EMAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testPersonEmail.getEmailEventNotificationSubscription()).isEqualTo(UPDATED_EMAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPersonEmail() throws Exception {
        int databaseSizeBeforeUpdate = personEmailRepository.findAll().size();
        personEmail.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personEmail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonEmail in the database
        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonEmail() throws Exception {
        int databaseSizeBeforeUpdate = personEmailRepository.findAll().size();
        personEmail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonEmailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personEmail))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonEmail in the database
        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonEmail() throws Exception {
        int databaseSizeBeforeUpdate = personEmailRepository.findAll().size();
        personEmail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonEmailMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personEmail))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonEmail in the database
        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonEmail() throws Exception {
        // Initialize the database
        personEmailRepository.saveAndFlush(personEmail);

        int databaseSizeBeforeDelete = personEmailRepository.findAll().size();

        // Delete the personEmail
        restPersonEmailMockMvc
            .perform(delete(ENTITY_API_URL_ID, personEmail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonEmail> personEmailList = personEmailRepository.findAll();
        assertThat(personEmailList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
