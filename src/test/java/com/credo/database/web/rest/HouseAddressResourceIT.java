package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.HouseAddress;
import com.credo.database.domain.HouseDetails;
import com.credo.database.domain.enumeration.YesNoEmpty;
import com.credo.database.domain.enumeration.YesNoEmpty;
import com.credo.database.repository.HouseAddressRepository;
import com.credo.database.service.criteria.HouseAddressCriteria;
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
 * Integration tests for the {@link HouseAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HouseAddressResourceIT {

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIPCODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIPCODE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final YesNoEmpty DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION = YesNoEmpty.YES;
    private static final YesNoEmpty UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION = YesNoEmpty.NO;

    private static final YesNoEmpty DEFAULT_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION = YesNoEmpty.YES;
    private static final YesNoEmpty UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION = YesNoEmpty.NO;

    private static final String ENTITY_API_URL = "/api/house-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HouseAddressRepository houseAddressRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHouseAddressMockMvc;

    private HouseAddress houseAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HouseAddress createEntity(EntityManager em) {
        HouseAddress houseAddress = new HouseAddress()
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipcode(DEFAULT_ZIPCODE)
            .type(DEFAULT_TYPE)
            .mailNewsletterSubscription(DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION)
            .mailEventNotificationSubscription(DEFAULT_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
        return houseAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HouseAddress createUpdatedEntity(EntityManager em) {
        HouseAddress houseAddress = new HouseAddress()
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipcode(UPDATED_ZIPCODE)
            .type(UPDATED_TYPE)
            .mailNewsletterSubscription(UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION)
            .mailEventNotificationSubscription(UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
        return houseAddress;
    }

    @BeforeEach
    public void initTest() {
        houseAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createHouseAddress() throws Exception {
        int databaseSizeBeforeCreate = houseAddressRepository.findAll().size();
        // Create the HouseAddress
        restHouseAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(houseAddress)))
            .andExpect(status().isCreated());

        // Validate the HouseAddress in the database
        List<HouseAddress> houseAddressList = houseAddressRepository.findAll();
        assertThat(houseAddressList).hasSize(databaseSizeBeforeCreate + 1);
        HouseAddress testHouseAddress = houseAddressList.get(houseAddressList.size() - 1);
        assertThat(testHouseAddress.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testHouseAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testHouseAddress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testHouseAddress.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testHouseAddress.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testHouseAddress.getMailNewsletterSubscription()).isEqualTo(DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testHouseAddress.getMailEventNotificationSubscription()).isEqualTo(DEFAULT_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void createHouseAddressWithExistingId() throws Exception {
        // Create the HouseAddress with an existing ID
        houseAddress.setId(1L);

        int databaseSizeBeforeCreate = houseAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHouseAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(houseAddress)))
            .andExpect(status().isBadRequest());

        // Validate the HouseAddress in the database
        List<HouseAddress> houseAddressList = houseAddressRepository.findAll();
        assertThat(houseAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHouseAddresses() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList
        restHouseAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(houseAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].mailNewsletterSubscription").value(hasItem(DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION.toString())))
            .andExpect(
                jsonPath("$.[*].mailEventNotificationSubscription").value(hasItem(DEFAULT_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION.toString()))
            );
    }

    @Test
    @Transactional
    void getHouseAddress() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get the houseAddress
        restHouseAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, houseAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(houseAddress.getId().intValue()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.mailNewsletterSubscription").value(DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION.toString()))
            .andExpect(jsonPath("$.mailEventNotificationSubscription").value(DEFAULT_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getHouseAddressesByIdFiltering() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        Long id = houseAddress.getId();

        defaultHouseAddressShouldBeFound("id.equals=" + id);
        defaultHouseAddressShouldNotBeFound("id.notEquals=" + id);

        defaultHouseAddressShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHouseAddressShouldNotBeFound("id.greaterThan=" + id);

        defaultHouseAddressShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHouseAddressShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultHouseAddressShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the houseAddressList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultHouseAddressShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByStreetAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where streetAddress not equals to DEFAULT_STREET_ADDRESS
        defaultHouseAddressShouldNotBeFound("streetAddress.notEquals=" + DEFAULT_STREET_ADDRESS);

        // Get all the houseAddressList where streetAddress not equals to UPDATED_STREET_ADDRESS
        defaultHouseAddressShouldBeFound("streetAddress.notEquals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultHouseAddressShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the houseAddressList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultHouseAddressShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where streetAddress is not null
        defaultHouseAddressShouldBeFound("streetAddress.specified=true");

        // Get all the houseAddressList where streetAddress is null
        defaultHouseAddressShouldNotBeFound("streetAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllHouseAddressesByStreetAddressContainsSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where streetAddress contains DEFAULT_STREET_ADDRESS
        defaultHouseAddressShouldBeFound("streetAddress.contains=" + DEFAULT_STREET_ADDRESS);

        // Get all the houseAddressList where streetAddress contains UPDATED_STREET_ADDRESS
        defaultHouseAddressShouldNotBeFound("streetAddress.contains=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByStreetAddressNotContainsSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where streetAddress does not contain DEFAULT_STREET_ADDRESS
        defaultHouseAddressShouldNotBeFound("streetAddress.doesNotContain=" + DEFAULT_STREET_ADDRESS);

        // Get all the houseAddressList where streetAddress does not contain UPDATED_STREET_ADDRESS
        defaultHouseAddressShouldBeFound("streetAddress.doesNotContain=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where city equals to DEFAULT_CITY
        defaultHouseAddressShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the houseAddressList where city equals to UPDATED_CITY
        defaultHouseAddressShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where city not equals to DEFAULT_CITY
        defaultHouseAddressShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the houseAddressList where city not equals to UPDATED_CITY
        defaultHouseAddressShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where city in DEFAULT_CITY or UPDATED_CITY
        defaultHouseAddressShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the houseAddressList where city equals to UPDATED_CITY
        defaultHouseAddressShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where city is not null
        defaultHouseAddressShouldBeFound("city.specified=true");

        // Get all the houseAddressList where city is null
        defaultHouseAddressShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllHouseAddressesByCityContainsSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where city contains DEFAULT_CITY
        defaultHouseAddressShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the houseAddressList where city contains UPDATED_CITY
        defaultHouseAddressShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where city does not contain DEFAULT_CITY
        defaultHouseAddressShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the houseAddressList where city does not contain UPDATED_CITY
        defaultHouseAddressShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where state equals to DEFAULT_STATE
        defaultHouseAddressShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the houseAddressList where state equals to UPDATED_STATE
        defaultHouseAddressShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where state not equals to DEFAULT_STATE
        defaultHouseAddressShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the houseAddressList where state not equals to UPDATED_STATE
        defaultHouseAddressShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where state in DEFAULT_STATE or UPDATED_STATE
        defaultHouseAddressShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the houseAddressList where state equals to UPDATED_STATE
        defaultHouseAddressShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where state is not null
        defaultHouseAddressShouldBeFound("state.specified=true");

        // Get all the houseAddressList where state is null
        defaultHouseAddressShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllHouseAddressesByStateContainsSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where state contains DEFAULT_STATE
        defaultHouseAddressShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the houseAddressList where state contains UPDATED_STATE
        defaultHouseAddressShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByStateNotContainsSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where state does not contain DEFAULT_STATE
        defaultHouseAddressShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the houseAddressList where state does not contain UPDATED_STATE
        defaultHouseAddressShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByZipcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where zipcode equals to DEFAULT_ZIPCODE
        defaultHouseAddressShouldBeFound("zipcode.equals=" + DEFAULT_ZIPCODE);

        // Get all the houseAddressList where zipcode equals to UPDATED_ZIPCODE
        defaultHouseAddressShouldNotBeFound("zipcode.equals=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByZipcodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where zipcode not equals to DEFAULT_ZIPCODE
        defaultHouseAddressShouldNotBeFound("zipcode.notEquals=" + DEFAULT_ZIPCODE);

        // Get all the houseAddressList where zipcode not equals to UPDATED_ZIPCODE
        defaultHouseAddressShouldBeFound("zipcode.notEquals=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByZipcodeIsInShouldWork() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where zipcode in DEFAULT_ZIPCODE or UPDATED_ZIPCODE
        defaultHouseAddressShouldBeFound("zipcode.in=" + DEFAULT_ZIPCODE + "," + UPDATED_ZIPCODE);

        // Get all the houseAddressList where zipcode equals to UPDATED_ZIPCODE
        defaultHouseAddressShouldNotBeFound("zipcode.in=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByZipcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where zipcode is not null
        defaultHouseAddressShouldBeFound("zipcode.specified=true");

        // Get all the houseAddressList where zipcode is null
        defaultHouseAddressShouldNotBeFound("zipcode.specified=false");
    }

    @Test
    @Transactional
    void getAllHouseAddressesByZipcodeContainsSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where zipcode contains DEFAULT_ZIPCODE
        defaultHouseAddressShouldBeFound("zipcode.contains=" + DEFAULT_ZIPCODE);

        // Get all the houseAddressList where zipcode contains UPDATED_ZIPCODE
        defaultHouseAddressShouldNotBeFound("zipcode.contains=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByZipcodeNotContainsSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where zipcode does not contain DEFAULT_ZIPCODE
        defaultHouseAddressShouldNotBeFound("zipcode.doesNotContain=" + DEFAULT_ZIPCODE);

        // Get all the houseAddressList where zipcode does not contain UPDATED_ZIPCODE
        defaultHouseAddressShouldBeFound("zipcode.doesNotContain=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where type equals to DEFAULT_TYPE
        defaultHouseAddressShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the houseAddressList where type equals to UPDATED_TYPE
        defaultHouseAddressShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where type not equals to DEFAULT_TYPE
        defaultHouseAddressShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the houseAddressList where type not equals to UPDATED_TYPE
        defaultHouseAddressShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultHouseAddressShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the houseAddressList where type equals to UPDATED_TYPE
        defaultHouseAddressShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where type is not null
        defaultHouseAddressShouldBeFound("type.specified=true");

        // Get all the houseAddressList where type is null
        defaultHouseAddressShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllHouseAddressesByTypeContainsSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where type contains DEFAULT_TYPE
        defaultHouseAddressShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the houseAddressList where type contains UPDATED_TYPE
        defaultHouseAddressShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where type does not contain DEFAULT_TYPE
        defaultHouseAddressShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the houseAddressList where type does not contain UPDATED_TYPE
        defaultHouseAddressShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByMailNewsletterSubscriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where mailNewsletterSubscription equals to DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION
        defaultHouseAddressShouldBeFound("mailNewsletterSubscription.equals=" + DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION);

        // Get all the houseAddressList where mailNewsletterSubscription equals to UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION
        defaultHouseAddressShouldNotBeFound("mailNewsletterSubscription.equals=" + UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByMailNewsletterSubscriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where mailNewsletterSubscription not equals to DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION
        defaultHouseAddressShouldNotBeFound("mailNewsletterSubscription.notEquals=" + DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION);

        // Get all the houseAddressList where mailNewsletterSubscription not equals to UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION
        defaultHouseAddressShouldBeFound("mailNewsletterSubscription.notEquals=" + UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByMailNewsletterSubscriptionIsInShouldWork() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where mailNewsletterSubscription in DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION or UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION
        defaultHouseAddressShouldBeFound(
            "mailNewsletterSubscription.in=" + DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION + "," + UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION
        );

        // Get all the houseAddressList where mailNewsletterSubscription equals to UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION
        defaultHouseAddressShouldNotBeFound("mailNewsletterSubscription.in=" + UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByMailNewsletterSubscriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where mailNewsletterSubscription is not null
        defaultHouseAddressShouldBeFound("mailNewsletterSubscription.specified=true");

        // Get all the houseAddressList where mailNewsletterSubscription is null
        defaultHouseAddressShouldNotBeFound("mailNewsletterSubscription.specified=false");
    }

    @Test
    @Transactional
    void getAllHouseAddressesByMailEventNotificationSubscriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where mailEventNotificationSubscription equals to DEFAULT_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultHouseAddressShouldBeFound("mailEventNotificationSubscription.equals=" + DEFAULT_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        // Get all the houseAddressList where mailEventNotificationSubscription equals to UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultHouseAddressShouldNotBeFound("mailEventNotificationSubscription.equals=" + UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByMailEventNotificationSubscriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where mailEventNotificationSubscription not equals to DEFAULT_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultHouseAddressShouldNotBeFound("mailEventNotificationSubscription.notEquals=" + DEFAULT_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        // Get all the houseAddressList where mailEventNotificationSubscription not equals to UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultHouseAddressShouldBeFound("mailEventNotificationSubscription.notEquals=" + UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByMailEventNotificationSubscriptionIsInShouldWork() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where mailEventNotificationSubscription in DEFAULT_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION or UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultHouseAddressShouldBeFound(
            "mailEventNotificationSubscription.in=" +
            DEFAULT_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION +
            "," +
            UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        );

        // Get all the houseAddressList where mailEventNotificationSubscription equals to UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION
        defaultHouseAddressShouldNotBeFound("mailEventNotificationSubscription.in=" + UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void getAllHouseAddressesByMailEventNotificationSubscriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        // Get all the houseAddressList where mailEventNotificationSubscription is not null
        defaultHouseAddressShouldBeFound("mailEventNotificationSubscription.specified=true");

        // Get all the houseAddressList where mailEventNotificationSubscription is null
        defaultHouseAddressShouldNotBeFound("mailEventNotificationSubscription.specified=false");
    }

    @Test
    @Transactional
    void getAllHouseAddressesByHouseDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);
        HouseDetails houseDetails = HouseDetailsResourceIT.createEntity(em);
        em.persist(houseDetails);
        em.flush();
        houseAddress.setHouseDetails(houseDetails);
        houseAddressRepository.saveAndFlush(houseAddress);
        Long houseDetailsId = houseDetails.getId();

        // Get all the houseAddressList where houseDetails equals to houseDetailsId
        defaultHouseAddressShouldBeFound("houseDetailsId.equals=" + houseDetailsId);

        // Get all the houseAddressList where houseDetails equals to (houseDetailsId + 1)
        defaultHouseAddressShouldNotBeFound("houseDetailsId.equals=" + (houseDetailsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHouseAddressShouldBeFound(String filter) throws Exception {
        restHouseAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(houseAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].mailNewsletterSubscription").value(hasItem(DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION.toString())))
            .andExpect(
                jsonPath("$.[*].mailEventNotificationSubscription").value(hasItem(DEFAULT_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION.toString()))
            );

        // Check, that the count call also returns 1
        restHouseAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHouseAddressShouldNotBeFound(String filter) throws Exception {
        restHouseAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHouseAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHouseAddress() throws Exception {
        // Get the houseAddress
        restHouseAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHouseAddress() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        int databaseSizeBeforeUpdate = houseAddressRepository.findAll().size();

        // Update the houseAddress
        HouseAddress updatedHouseAddress = houseAddressRepository.findById(houseAddress.getId()).get();
        // Disconnect from session so that the updates on updatedHouseAddress are not directly saved in db
        em.detach(updatedHouseAddress);
        updatedHouseAddress
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipcode(UPDATED_ZIPCODE)
            .type(UPDATED_TYPE)
            .mailNewsletterSubscription(UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION)
            .mailEventNotificationSubscription(UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        restHouseAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHouseAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHouseAddress))
            )
            .andExpect(status().isOk());

        // Validate the HouseAddress in the database
        List<HouseAddress> houseAddressList = houseAddressRepository.findAll();
        assertThat(houseAddressList).hasSize(databaseSizeBeforeUpdate);
        HouseAddress testHouseAddress = houseAddressList.get(houseAddressList.size() - 1);
        assertThat(testHouseAddress.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testHouseAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testHouseAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testHouseAddress.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testHouseAddress.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHouseAddress.getMailNewsletterSubscription()).isEqualTo(UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testHouseAddress.getMailEventNotificationSubscription()).isEqualTo(UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingHouseAddress() throws Exception {
        int databaseSizeBeforeUpdate = houseAddressRepository.findAll().size();
        houseAddress.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHouseAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, houseAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(houseAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseAddress in the database
        List<HouseAddress> houseAddressList = houseAddressRepository.findAll();
        assertThat(houseAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHouseAddress() throws Exception {
        int databaseSizeBeforeUpdate = houseAddressRepository.findAll().size();
        houseAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(houseAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseAddress in the database
        List<HouseAddress> houseAddressList = houseAddressRepository.findAll();
        assertThat(houseAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHouseAddress() throws Exception {
        int databaseSizeBeforeUpdate = houseAddressRepository.findAll().size();
        houseAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(houseAddress)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HouseAddress in the database
        List<HouseAddress> houseAddressList = houseAddressRepository.findAll();
        assertThat(houseAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHouseAddressWithPatch() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        int databaseSizeBeforeUpdate = houseAddressRepository.findAll().size();

        // Update the houseAddress using partial update
        HouseAddress partialUpdatedHouseAddress = new HouseAddress();
        partialUpdatedHouseAddress.setId(houseAddress.getId());

        partialUpdatedHouseAddress
            .streetAddress(UPDATED_STREET_ADDRESS)
            .state(UPDATED_STATE)
            .mailEventNotificationSubscription(UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        restHouseAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHouseAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHouseAddress))
            )
            .andExpect(status().isOk());

        // Validate the HouseAddress in the database
        List<HouseAddress> houseAddressList = houseAddressRepository.findAll();
        assertThat(houseAddressList).hasSize(databaseSizeBeforeUpdate);
        HouseAddress testHouseAddress = houseAddressList.get(houseAddressList.size() - 1);
        assertThat(testHouseAddress.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testHouseAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testHouseAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testHouseAddress.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
        assertThat(testHouseAddress.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testHouseAddress.getMailNewsletterSubscription()).isEqualTo(DEFAULT_MAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testHouseAddress.getMailEventNotificationSubscription()).isEqualTo(UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateHouseAddressWithPatch() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        int databaseSizeBeforeUpdate = houseAddressRepository.findAll().size();

        // Update the houseAddress using partial update
        HouseAddress partialUpdatedHouseAddress = new HouseAddress();
        partialUpdatedHouseAddress.setId(houseAddress.getId());

        partialUpdatedHouseAddress
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipcode(UPDATED_ZIPCODE)
            .type(UPDATED_TYPE)
            .mailNewsletterSubscription(UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION)
            .mailEventNotificationSubscription(UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);

        restHouseAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHouseAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHouseAddress))
            )
            .andExpect(status().isOk());

        // Validate the HouseAddress in the database
        List<HouseAddress> houseAddressList = houseAddressRepository.findAll();
        assertThat(houseAddressList).hasSize(databaseSizeBeforeUpdate);
        HouseAddress testHouseAddress = houseAddressList.get(houseAddressList.size() - 1);
        assertThat(testHouseAddress.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testHouseAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testHouseAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testHouseAddress.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
        assertThat(testHouseAddress.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHouseAddress.getMailNewsletterSubscription()).isEqualTo(UPDATED_MAIL_NEWSLETTER_SUBSCRIPTION);
        assertThat(testHouseAddress.getMailEventNotificationSubscription()).isEqualTo(UPDATED_MAIL_EVENT_NOTIFICATION_SUBSCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingHouseAddress() throws Exception {
        int databaseSizeBeforeUpdate = houseAddressRepository.findAll().size();
        houseAddress.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHouseAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, houseAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(houseAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseAddress in the database
        List<HouseAddress> houseAddressList = houseAddressRepository.findAll();
        assertThat(houseAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHouseAddress() throws Exception {
        int databaseSizeBeforeUpdate = houseAddressRepository.findAll().size();
        houseAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(houseAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseAddress in the database
        List<HouseAddress> houseAddressList = houseAddressRepository.findAll();
        assertThat(houseAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHouseAddress() throws Exception {
        int databaseSizeBeforeUpdate = houseAddressRepository.findAll().size();
        houseAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseAddressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(houseAddress))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HouseAddress in the database
        List<HouseAddress> houseAddressList = houseAddressRepository.findAll();
        assertThat(houseAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHouseAddress() throws Exception {
        // Initialize the database
        houseAddressRepository.saveAndFlush(houseAddress);

        int databaseSizeBeforeDelete = houseAddressRepository.findAll().size();

        // Delete the houseAddress
        restHouseAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, houseAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HouseAddress> houseAddressList = houseAddressRepository.findAll();
        assertThat(houseAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
