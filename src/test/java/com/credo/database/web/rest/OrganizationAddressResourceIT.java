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
import com.credo.database.domain.OrganizationAddress;
import com.credo.database.repository.OrganizationAddressRepository;
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
 * Integration tests for the {@link OrganizationAddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganizationAddressResourceIT {

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIPCODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIPCODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organization-addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrganizationAddressRepository organizationAddressRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrganizationAddressMockMvc;

    private OrganizationAddress organizationAddress;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationAddress createEntity(EntityManager em) {
        OrganizationAddress organizationAddress = new OrganizationAddress()
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zipcode(DEFAULT_ZIPCODE);
        return organizationAddress;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationAddress createUpdatedEntity(EntityManager em) {
        OrganizationAddress organizationAddress = new OrganizationAddress()
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipcode(UPDATED_ZIPCODE);
        return organizationAddress;
    }

    @BeforeEach
    public void initTest() {
        organizationAddress = createEntity(em);
    }

    @Test
    @Transactional
    void createOrganizationAddress() throws Exception {
        int databaseSizeBeforeCreate = organizationAddressRepository.findAll().size();
        // Create the OrganizationAddress
        restOrganizationAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationAddress))
            )
            .andExpect(status().isCreated());

        // Validate the OrganizationAddress in the database
        List<OrganizationAddress> organizationAddressList = organizationAddressRepository.findAll();
        assertThat(organizationAddressList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationAddress testOrganizationAddress = organizationAddressList.get(organizationAddressList.size() - 1);
        assertThat(testOrganizationAddress.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testOrganizationAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testOrganizationAddress.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testOrganizationAddress.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
    }

    @Test
    @Transactional
    void createOrganizationAddressWithExistingId() throws Exception {
        // Create the OrganizationAddress with an existing ID
        organizationAddress.setId(1L);

        int databaseSizeBeforeCreate = organizationAddressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationAddressMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationAddress in the database
        List<OrganizationAddress> organizationAddressList = organizationAddressRepository.findAll();
        assertThat(organizationAddressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrganizationAddresses() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList
        restOrganizationAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE)));
    }

    @Test
    @Transactional
    void getOrganizationAddress() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get the organizationAddress
        restOrganizationAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, organizationAddress.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organizationAddress.getId().intValue()))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.zipcode").value(DEFAULT_ZIPCODE));
    }

    @Test
    @Transactional
    void getOrganizationAddressesByIdFiltering() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        Long id = organizationAddress.getId();

        defaultOrganizationAddressShouldBeFound("id.equals=" + id);
        defaultOrganizationAddressShouldNotBeFound("id.notEquals=" + id);

        defaultOrganizationAddressShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrganizationAddressShouldNotBeFound("id.greaterThan=" + id);

        defaultOrganizationAddressShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrganizationAddressShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByStreetAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where streetAddress equals to DEFAULT_STREET_ADDRESS
        defaultOrganizationAddressShouldBeFound("streetAddress.equals=" + DEFAULT_STREET_ADDRESS);

        // Get all the organizationAddressList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultOrganizationAddressShouldNotBeFound("streetAddress.equals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByStreetAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where streetAddress not equals to DEFAULT_STREET_ADDRESS
        defaultOrganizationAddressShouldNotBeFound("streetAddress.notEquals=" + DEFAULT_STREET_ADDRESS);

        // Get all the organizationAddressList where streetAddress not equals to UPDATED_STREET_ADDRESS
        defaultOrganizationAddressShouldBeFound("streetAddress.notEquals=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByStreetAddressIsInShouldWork() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where streetAddress in DEFAULT_STREET_ADDRESS or UPDATED_STREET_ADDRESS
        defaultOrganizationAddressShouldBeFound("streetAddress.in=" + DEFAULT_STREET_ADDRESS + "," + UPDATED_STREET_ADDRESS);

        // Get all the organizationAddressList where streetAddress equals to UPDATED_STREET_ADDRESS
        defaultOrganizationAddressShouldNotBeFound("streetAddress.in=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByStreetAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where streetAddress is not null
        defaultOrganizationAddressShouldBeFound("streetAddress.specified=true");

        // Get all the organizationAddressList where streetAddress is null
        defaultOrganizationAddressShouldNotBeFound("streetAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByStreetAddressContainsSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where streetAddress contains DEFAULT_STREET_ADDRESS
        defaultOrganizationAddressShouldBeFound("streetAddress.contains=" + DEFAULT_STREET_ADDRESS);

        // Get all the organizationAddressList where streetAddress contains UPDATED_STREET_ADDRESS
        defaultOrganizationAddressShouldNotBeFound("streetAddress.contains=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByStreetAddressNotContainsSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where streetAddress does not contain DEFAULT_STREET_ADDRESS
        defaultOrganizationAddressShouldNotBeFound("streetAddress.doesNotContain=" + DEFAULT_STREET_ADDRESS);

        // Get all the organizationAddressList where streetAddress does not contain UPDATED_STREET_ADDRESS
        defaultOrganizationAddressShouldBeFound("streetAddress.doesNotContain=" + UPDATED_STREET_ADDRESS);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where city equals to DEFAULT_CITY
        defaultOrganizationAddressShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the organizationAddressList where city equals to UPDATED_CITY
        defaultOrganizationAddressShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where city not equals to DEFAULT_CITY
        defaultOrganizationAddressShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the organizationAddressList where city not equals to UPDATED_CITY
        defaultOrganizationAddressShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where city in DEFAULT_CITY or UPDATED_CITY
        defaultOrganizationAddressShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the organizationAddressList where city equals to UPDATED_CITY
        defaultOrganizationAddressShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where city is not null
        defaultOrganizationAddressShouldBeFound("city.specified=true");

        // Get all the organizationAddressList where city is null
        defaultOrganizationAddressShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByCityContainsSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where city contains DEFAULT_CITY
        defaultOrganizationAddressShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the organizationAddressList where city contains UPDATED_CITY
        defaultOrganizationAddressShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where city does not contain DEFAULT_CITY
        defaultOrganizationAddressShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the organizationAddressList where city does not contain UPDATED_CITY
        defaultOrganizationAddressShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where state equals to DEFAULT_STATE
        defaultOrganizationAddressShouldBeFound("state.equals=" + DEFAULT_STATE);

        // Get all the organizationAddressList where state equals to UPDATED_STATE
        defaultOrganizationAddressShouldNotBeFound("state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where state not equals to DEFAULT_STATE
        defaultOrganizationAddressShouldNotBeFound("state.notEquals=" + DEFAULT_STATE);

        // Get all the organizationAddressList where state not equals to UPDATED_STATE
        defaultOrganizationAddressShouldBeFound("state.notEquals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where state in DEFAULT_STATE or UPDATED_STATE
        defaultOrganizationAddressShouldBeFound("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE);

        // Get all the organizationAddressList where state equals to UPDATED_STATE
        defaultOrganizationAddressShouldNotBeFound("state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where state is not null
        defaultOrganizationAddressShouldBeFound("state.specified=true");

        // Get all the organizationAddressList where state is null
        defaultOrganizationAddressShouldNotBeFound("state.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByStateContainsSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where state contains DEFAULT_STATE
        defaultOrganizationAddressShouldBeFound("state.contains=" + DEFAULT_STATE);

        // Get all the organizationAddressList where state contains UPDATED_STATE
        defaultOrganizationAddressShouldNotBeFound("state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByStateNotContainsSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where state does not contain DEFAULT_STATE
        defaultOrganizationAddressShouldNotBeFound("state.doesNotContain=" + DEFAULT_STATE);

        // Get all the organizationAddressList where state does not contain UPDATED_STATE
        defaultOrganizationAddressShouldBeFound("state.doesNotContain=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByZipcodeIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where zipcode equals to DEFAULT_ZIPCODE
        defaultOrganizationAddressShouldBeFound("zipcode.equals=" + DEFAULT_ZIPCODE);

        // Get all the organizationAddressList where zipcode equals to UPDATED_ZIPCODE
        defaultOrganizationAddressShouldNotBeFound("zipcode.equals=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByZipcodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where zipcode not equals to DEFAULT_ZIPCODE
        defaultOrganizationAddressShouldNotBeFound("zipcode.notEquals=" + DEFAULT_ZIPCODE);

        // Get all the organizationAddressList where zipcode not equals to UPDATED_ZIPCODE
        defaultOrganizationAddressShouldBeFound("zipcode.notEquals=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByZipcodeIsInShouldWork() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where zipcode in DEFAULT_ZIPCODE or UPDATED_ZIPCODE
        defaultOrganizationAddressShouldBeFound("zipcode.in=" + DEFAULT_ZIPCODE + "," + UPDATED_ZIPCODE);

        // Get all the organizationAddressList where zipcode equals to UPDATED_ZIPCODE
        defaultOrganizationAddressShouldNotBeFound("zipcode.in=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByZipcodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where zipcode is not null
        defaultOrganizationAddressShouldBeFound("zipcode.specified=true");

        // Get all the organizationAddressList where zipcode is null
        defaultOrganizationAddressShouldNotBeFound("zipcode.specified=false");
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByZipcodeContainsSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where zipcode contains DEFAULT_ZIPCODE
        defaultOrganizationAddressShouldBeFound("zipcode.contains=" + DEFAULT_ZIPCODE);

        // Get all the organizationAddressList where zipcode contains UPDATED_ZIPCODE
        defaultOrganizationAddressShouldNotBeFound("zipcode.contains=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByZipcodeNotContainsSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        // Get all the organizationAddressList where zipcode does not contain DEFAULT_ZIPCODE
        defaultOrganizationAddressShouldNotBeFound("zipcode.doesNotContain=" + DEFAULT_ZIPCODE);

        // Get all the organizationAddressList where zipcode does not contain UPDATED_ZIPCODE
        defaultOrganizationAddressShouldBeFound("zipcode.doesNotContain=" + UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void getAllOrganizationAddressesByOrganizationIsEqualToSomething() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);
        Organization organization = OrganizationResourceIT.createEntity(em);
        em.persist(organization);
        em.flush();
        organizationAddress.setOrganization(organization);
        organizationAddressRepository.saveAndFlush(organizationAddress);
        Long organizationId = organization.getId();

        // Get all the organizationAddressList where organization equals to organizationId
        defaultOrganizationAddressShouldBeFound("organizationId.equals=" + organizationId);

        // Get all the organizationAddressList where organization equals to (organizationId + 1)
        defaultOrganizationAddressShouldNotBeFound("organizationId.equals=" + (organizationId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrganizationAddressShouldBeFound(String filter) throws Exception {
        restOrganizationAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationAddress.getId().intValue())))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zipcode").value(hasItem(DEFAULT_ZIPCODE)));

        // Check, that the count call also returns 1
        restOrganizationAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrganizationAddressShouldNotBeFound(String filter) throws Exception {
        restOrganizationAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrganizationAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrganizationAddress() throws Exception {
        // Get the organizationAddress
        restOrganizationAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrganizationAddress() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        int databaseSizeBeforeUpdate = organizationAddressRepository.findAll().size();

        // Update the organizationAddress
        OrganizationAddress updatedOrganizationAddress = organizationAddressRepository.findById(organizationAddress.getId()).get();
        // Disconnect from session so that the updates on updatedOrganizationAddress are not directly saved in db
        em.detach(updatedOrganizationAddress);
        updatedOrganizationAddress.streetAddress(UPDATED_STREET_ADDRESS).city(UPDATED_CITY).state(UPDATED_STATE).zipcode(UPDATED_ZIPCODE);

        restOrganizationAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganizationAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrganizationAddress))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationAddress in the database
        List<OrganizationAddress> organizationAddressList = organizationAddressRepository.findAll();
        assertThat(organizationAddressList).hasSize(databaseSizeBeforeUpdate);
        OrganizationAddress testOrganizationAddress = organizationAddressList.get(organizationAddressList.size() - 1);
        assertThat(testOrganizationAddress.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testOrganizationAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testOrganizationAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testOrganizationAddress.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void putNonExistingOrganizationAddress() throws Exception {
        int databaseSizeBeforeUpdate = organizationAddressRepository.findAll().size();
        organizationAddress.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationAddress.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationAddress in the database
        List<OrganizationAddress> organizationAddressList = organizationAddressRepository.findAll();
        assertThat(organizationAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrganizationAddress() throws Exception {
        int databaseSizeBeforeUpdate = organizationAddressRepository.findAll().size();
        organizationAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationAddress in the database
        List<OrganizationAddress> organizationAddressList = organizationAddressRepository.findAll();
        assertThat(organizationAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrganizationAddress() throws Exception {
        int databaseSizeBeforeUpdate = organizationAddressRepository.findAll().size();
        organizationAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationAddressMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(organizationAddress))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationAddress in the database
        List<OrganizationAddress> organizationAddressList = organizationAddressRepository.findAll();
        assertThat(organizationAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrganizationAddressWithPatch() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        int databaseSizeBeforeUpdate = organizationAddressRepository.findAll().size();

        // Update the organizationAddress using partial update
        OrganizationAddress partialUpdatedOrganizationAddress = new OrganizationAddress();
        partialUpdatedOrganizationAddress.setId(organizationAddress.getId());

        partialUpdatedOrganizationAddress.state(UPDATED_STATE);

        restOrganizationAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationAddress))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationAddress in the database
        List<OrganizationAddress> organizationAddressList = organizationAddressRepository.findAll();
        assertThat(organizationAddressList).hasSize(databaseSizeBeforeUpdate);
        OrganizationAddress testOrganizationAddress = organizationAddressList.get(organizationAddressList.size() - 1);
        assertThat(testOrganizationAddress.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testOrganizationAddress.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testOrganizationAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testOrganizationAddress.getZipcode()).isEqualTo(DEFAULT_ZIPCODE);
    }

    @Test
    @Transactional
    void fullUpdateOrganizationAddressWithPatch() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        int databaseSizeBeforeUpdate = organizationAddressRepository.findAll().size();

        // Update the organizationAddress using partial update
        OrganizationAddress partialUpdatedOrganizationAddress = new OrganizationAddress();
        partialUpdatedOrganizationAddress.setId(organizationAddress.getId());

        partialUpdatedOrganizationAddress
            .streetAddress(UPDATED_STREET_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zipcode(UPDATED_ZIPCODE);

        restOrganizationAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationAddress))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationAddress in the database
        List<OrganizationAddress> organizationAddressList = organizationAddressRepository.findAll();
        assertThat(organizationAddressList).hasSize(databaseSizeBeforeUpdate);
        OrganizationAddress testOrganizationAddress = organizationAddressList.get(organizationAddressList.size() - 1);
        assertThat(testOrganizationAddress.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testOrganizationAddress.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testOrganizationAddress.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testOrganizationAddress.getZipcode()).isEqualTo(UPDATED_ZIPCODE);
    }

    @Test
    @Transactional
    void patchNonExistingOrganizationAddress() throws Exception {
        int databaseSizeBeforeUpdate = organizationAddressRepository.findAll().size();
        organizationAddress.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organizationAddress.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationAddress in the database
        List<OrganizationAddress> organizationAddressList = organizationAddressRepository.findAll();
        assertThat(organizationAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrganizationAddress() throws Exception {
        int databaseSizeBeforeUpdate = organizationAddressRepository.findAll().size();
        organizationAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationAddress))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationAddress in the database
        List<OrganizationAddress> organizationAddressList = organizationAddressRepository.findAll();
        assertThat(organizationAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrganizationAddress() throws Exception {
        int databaseSizeBeforeUpdate = organizationAddressRepository.findAll().size();
        organizationAddress.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationAddressMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationAddress))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationAddress in the database
        List<OrganizationAddress> organizationAddressList = organizationAddressRepository.findAll();
        assertThat(organizationAddressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrganizationAddress() throws Exception {
        // Initialize the database
        organizationAddressRepository.saveAndFlush(organizationAddress);

        int databaseSizeBeforeDelete = organizationAddressRepository.findAll().size();

        // Delete the organizationAddress
        restOrganizationAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, organizationAddress.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganizationAddress> organizationAddressList = organizationAddressRepository.findAll();
        assertThat(organizationAddressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
