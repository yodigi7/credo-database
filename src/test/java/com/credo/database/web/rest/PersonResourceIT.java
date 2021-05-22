package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.HouseAddress;
import com.credo.database.domain.HouseDetails;
import com.credo.database.domain.MembershipLevel;
import com.credo.database.domain.Organization;
import com.credo.database.domain.Parish;
import com.credo.database.domain.Payment;
import com.credo.database.domain.Person;
import com.credo.database.domain.PersonEmail;
import com.credo.database.domain.PersonNotes;
import com.credo.database.domain.PersonPhone;
import com.credo.database.domain.Relationship;
import com.credo.database.domain.Ticket;
import com.credo.database.repository.PersonRepository;
import com.credo.database.service.PersonService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PersonResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PersonResourceIT {

    private static final String DEFAULT_PREFIX = "AAAAAAAAAA";
    private static final String UPDATED_PREFIX = "BBBBBBBBBB";

    private static final String DEFAULT_PREFERRED_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PREFERRED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MIDDLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MIDDLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUFFIX = "AAAAAAAAAA";
    private static final String UPDATED_SUFFIX = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_TAG = "AAAAAAAAAA";
    private static final String UPDATED_NAME_TAG = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CURRENT_MEMBER = false;
    private static final Boolean UPDATED_CURRENT_MEMBER = true;

    private static final LocalDate DEFAULT_MEMBERSHIP_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MEMBERSHIP_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MEMBERSHIP_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_MEMBERSHIP_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MEMBERSHIP_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MEMBERSHIP_EXPIRATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_IS_HEAD_OF_HOUSE = false;
    private static final Boolean UPDATED_IS_HEAD_OF_HOUSE = true;

    private static final Boolean DEFAULT_IS_DECEASED = false;
    private static final Boolean UPDATED_IS_DECEASED = true;

    private static final String ENTITY_API_URL = "/api/people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonRepository personRepository;

    @Mock
    private PersonRepository personRepositoryMock;

    @Mock
    private PersonService personServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonMockMvc;

    private Person person;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createEntity(EntityManager em) {
        Person person = new Person()
            .prefix(DEFAULT_PREFIX)
            .preferredName(DEFAULT_PREFERRED_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .middleName(DEFAULT_MIDDLE_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .suffix(DEFAULT_SUFFIX)
            .nameTag(DEFAULT_NAME_TAG)
            .currentMember(DEFAULT_CURRENT_MEMBER)
            .membershipStartDate(DEFAULT_MEMBERSHIP_START_DATE)
            .membershipExpirationDate(DEFAULT_MEMBERSHIP_EXPIRATION_DATE)
            .isHeadOfHouse(DEFAULT_IS_HEAD_OF_HOUSE)
            .isDeceased(DEFAULT_IS_DECEASED);
        return person;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createUpdatedEntity(EntityManager em) {
        Person person = new Person()
            .prefix(UPDATED_PREFIX)
            .preferredName(UPDATED_PREFERRED_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .suffix(UPDATED_SUFFIX)
            .nameTag(UPDATED_NAME_TAG)
            .currentMember(UPDATED_CURRENT_MEMBER)
            .membershipStartDate(UPDATED_MEMBERSHIP_START_DATE)
            .membershipExpirationDate(UPDATED_MEMBERSHIP_EXPIRATION_DATE)
            .isHeadOfHouse(UPDATED_IS_HEAD_OF_HOUSE)
            .isDeceased(UPDATED_IS_DECEASED);
        return person;
    }

    @BeforeEach
    public void initTest() {
        person = createEntity(em);
    }

    @Test
    @Transactional
    void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();
        // Create the Person
        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate + 1);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testPerson.getPreferredName()).isEqualTo(DEFAULT_PREFERRED_NAME);
        assertThat(testPerson.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPerson.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPerson.getSuffix()).isEqualTo(DEFAULT_SUFFIX);
        assertThat(testPerson.getNameTag()).isEqualTo(DEFAULT_NAME_TAG);
        assertThat(testPerson.getCurrentMember()).isEqualTo(DEFAULT_CURRENT_MEMBER);
        assertThat(testPerson.getMembershipStartDate()).isEqualTo(DEFAULT_MEMBERSHIP_START_DATE);
        assertThat(testPerson.getMembershipExpirationDate()).isEqualTo(DEFAULT_MEMBERSHIP_EXPIRATION_DATE);
        assertThat(testPerson.getIsHeadOfHouse()).isEqualTo(DEFAULT_IS_HEAD_OF_HOUSE);
        assertThat(testPerson.getIsDeceased()).isEqualTo(DEFAULT_IS_DECEASED);
    }

    @Test
    @Transactional
    void createPersonCascadesAllRelationships() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();
        // Create the Person
        person.setNotes(new PersonNotes().notes("notes"));
        person.setSpouse(new Person().isDeceased(false).isHeadOfHouse(false));
        person.setHouseDetails(new HouseDetails().addresses(new HashSet<>(Arrays.asList(new HouseAddress().city("city")))));
        person.setPhones(new HashSet<>(Arrays.asList(new PersonPhone().phoneNumber("(111) 111-1111"))));
        person.setEmails(new HashSet<>(Arrays.asList(new PersonEmail().email("email"))));
        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate + 2);
        Person testPerson = personList.get(personList.size() - 2);
        assertThat(testPerson.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testPerson.getPreferredName()).isEqualTo(DEFAULT_PREFERRED_NAME);
        assertThat(testPerson.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPerson.getMiddleName()).isEqualTo(DEFAULT_MIDDLE_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPerson.getSuffix()).isEqualTo(DEFAULT_SUFFIX);
        assertThat(testPerson.getNameTag()).isEqualTo(DEFAULT_NAME_TAG);
        assertThat(testPerson.getCurrentMember()).isEqualTo(DEFAULT_CURRENT_MEMBER);
        assertThat(testPerson.getMembershipStartDate()).isEqualTo(DEFAULT_MEMBERSHIP_START_DATE);
        assertThat(testPerson.getMembershipExpirationDate()).isEqualTo(DEFAULT_MEMBERSHIP_EXPIRATION_DATE);
        assertThat(testPerson.getIsHeadOfHouse()).isEqualTo(DEFAULT_IS_HEAD_OF_HOUSE);
        assertThat(testPerson.getIsDeceased()).isEqualTo(DEFAULT_IS_DECEASED);
        assertThat(testPerson.getNotes()).isNotNull();
        assertThat(testPerson.getSpouse()).isNotNull();
        assertThat(testPerson.getHouseDetails()).isNotNull();
        assertThat(testPerson.getHouseDetails().getAddresses()).isNotEmpty();
        assertThat(testPerson.getPhones()).isNotEmpty();
        assertThat(testPerson.getEmails()).isNotEmpty();
    }

    @Test
    @Transactional
    void createPersonWithExistingId() throws Exception {
        // Create the Person with an existing ID
        person.setId(1L);

        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsHeadOfHouseIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        person.setIsHeadOfHouse(null);

        // Create the Person, which fails.

        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDeceasedIsRequired() throws Exception {
        int databaseSizeBeforeTest = personRepository.findAll().size();
        // set the field null
        person.setIsDeceased(null);

        // Create the Person, which fails.

        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isBadRequest());

        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPeople() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX)))
            .andExpect(jsonPath("$.[*].preferredName").value(hasItem(DEFAULT_PREFERRED_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].suffix").value(hasItem(DEFAULT_SUFFIX)))
            .andExpect(jsonPath("$.[*].nameTag").value(hasItem(DEFAULT_NAME_TAG)))
            .andExpect(jsonPath("$.[*].currentMember").value(hasItem(DEFAULT_CURRENT_MEMBER.booleanValue())))
            .andExpect(jsonPath("$.[*].membershipStartDate").value(hasItem(DEFAULT_MEMBERSHIP_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].membershipExpirationDate").value(hasItem(DEFAULT_MEMBERSHIP_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isHeadOfHouse").value(hasItem(DEFAULT_IS_HEAD_OF_HOUSE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeceased").value(hasItem(DEFAULT_IS_DECEASED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeopleWithEagerRelationshipsIsEnabled() throws Exception {
        when(personServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(personServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeopleWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(personServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(personServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get the person
        restPersonMockMvc
            .perform(get(ENTITY_API_URL_ID, person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(person.getId().intValue()))
            .andExpect(jsonPath("$.prefix").value(DEFAULT_PREFIX))
            .andExpect(jsonPath("$.preferredName").value(DEFAULT_PREFERRED_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.middleName").value(DEFAULT_MIDDLE_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.suffix").value(DEFAULT_SUFFIX))
            .andExpect(jsonPath("$.nameTag").value(DEFAULT_NAME_TAG))
            .andExpect(jsonPath("$.currentMember").value(DEFAULT_CURRENT_MEMBER.booleanValue()))
            .andExpect(jsonPath("$.membershipStartDate").value(DEFAULT_MEMBERSHIP_START_DATE.toString()))
            .andExpect(jsonPath("$.membershipExpirationDate").value(DEFAULT_MEMBERSHIP_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.isHeadOfHouse").value(DEFAULT_IS_HEAD_OF_HOUSE.booleanValue()))
            .andExpect(jsonPath("$.isDeceased").value(DEFAULT_IS_DECEASED.booleanValue()));
    }

    @Test
    @Transactional
    void getPeopleByIdFiltering() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        Long id = person.getId();

        defaultPersonShouldBeFound("id.equals=" + id);
        defaultPersonShouldNotBeFound("id.notEquals=" + id);

        defaultPersonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPeopleByPrefixIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where prefix equals to DEFAULT_PREFIX
        defaultPersonShouldBeFound("prefix.equals=" + DEFAULT_PREFIX);

        // Get all the personList where prefix equals to UPDATED_PREFIX
        defaultPersonShouldNotBeFound("prefix.equals=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllPeopleByPrefixIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where prefix not equals to DEFAULT_PREFIX
        defaultPersonShouldNotBeFound("prefix.notEquals=" + DEFAULT_PREFIX);

        // Get all the personList where prefix not equals to UPDATED_PREFIX
        defaultPersonShouldBeFound("prefix.notEquals=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllPeopleByPrefixIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where prefix in DEFAULT_PREFIX or UPDATED_PREFIX
        defaultPersonShouldBeFound("prefix.in=" + DEFAULT_PREFIX + "," + UPDATED_PREFIX);

        // Get all the personList where prefix equals to UPDATED_PREFIX
        defaultPersonShouldNotBeFound("prefix.in=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllPeopleByPrefixIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where prefix is not null
        defaultPersonShouldBeFound("prefix.specified=true");

        // Get all the personList where prefix is null
        defaultPersonShouldNotBeFound("prefix.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByPrefixContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where prefix contains DEFAULT_PREFIX
        defaultPersonShouldBeFound("prefix.contains=" + DEFAULT_PREFIX);

        // Get all the personList where prefix contains UPDATED_PREFIX
        defaultPersonShouldNotBeFound("prefix.contains=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllPeopleByPrefixNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where prefix does not contain DEFAULT_PREFIX
        defaultPersonShouldNotBeFound("prefix.doesNotContain=" + DEFAULT_PREFIX);

        // Get all the personList where prefix does not contain UPDATED_PREFIX
        defaultPersonShouldBeFound("prefix.doesNotContain=" + UPDATED_PREFIX);
    }

    @Test
    @Transactional
    void getAllPeopleByPreferredNameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where preferredName equals to DEFAULT_PREFERRED_NAME
        defaultPersonShouldBeFound("preferredName.equals=" + DEFAULT_PREFERRED_NAME);

        // Get all the personList where preferredName equals to UPDATED_PREFERRED_NAME
        defaultPersonShouldNotBeFound("preferredName.equals=" + UPDATED_PREFERRED_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByPreferredNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where preferredName not equals to DEFAULT_PREFERRED_NAME
        defaultPersonShouldNotBeFound("preferredName.notEquals=" + DEFAULT_PREFERRED_NAME);

        // Get all the personList where preferredName not equals to UPDATED_PREFERRED_NAME
        defaultPersonShouldBeFound("preferredName.notEquals=" + UPDATED_PREFERRED_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByPreferredNameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where preferredName in DEFAULT_PREFERRED_NAME or UPDATED_PREFERRED_NAME
        defaultPersonShouldBeFound("preferredName.in=" + DEFAULT_PREFERRED_NAME + "," + UPDATED_PREFERRED_NAME);

        // Get all the personList where preferredName equals to UPDATED_PREFERRED_NAME
        defaultPersonShouldNotBeFound("preferredName.in=" + UPDATED_PREFERRED_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByPreferredNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where preferredName is not null
        defaultPersonShouldBeFound("preferredName.specified=true");

        // Get all the personList where preferredName is null
        defaultPersonShouldNotBeFound("preferredName.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByPreferredNameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where preferredName contains DEFAULT_PREFERRED_NAME
        defaultPersonShouldBeFound("preferredName.contains=" + DEFAULT_PREFERRED_NAME);

        // Get all the personList where preferredName contains UPDATED_PREFERRED_NAME
        defaultPersonShouldNotBeFound("preferredName.contains=" + UPDATED_PREFERRED_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByPreferredNameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where preferredName does not contain DEFAULT_PREFERRED_NAME
        defaultPersonShouldNotBeFound("preferredName.doesNotContain=" + DEFAULT_PREFERRED_NAME);

        // Get all the personList where preferredName does not contain UPDATED_PREFERRED_NAME
        defaultPersonShouldBeFound("preferredName.doesNotContain=" + UPDATED_PREFERRED_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstName equals to DEFAULT_FIRST_NAME
        defaultPersonShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the personList where firstName equals to UPDATED_FIRST_NAME
        defaultPersonShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstName not equals to DEFAULT_FIRST_NAME
        defaultPersonShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the personList where firstName not equals to UPDATED_FIRST_NAME
        defaultPersonShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultPersonShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the personList where firstName equals to UPDATED_FIRST_NAME
        defaultPersonShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstName is not null
        defaultPersonShouldBeFound("firstName.specified=true");

        // Get all the personList where firstName is null
        defaultPersonShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstName contains DEFAULT_FIRST_NAME
        defaultPersonShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the personList where firstName contains UPDATED_FIRST_NAME
        defaultPersonShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where firstName does not contain DEFAULT_FIRST_NAME
        defaultPersonShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the personList where firstName does not contain UPDATED_FIRST_NAME
        defaultPersonShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByMiddleNameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where middleName equals to DEFAULT_MIDDLE_NAME
        defaultPersonShouldBeFound("middleName.equals=" + DEFAULT_MIDDLE_NAME);

        // Get all the personList where middleName equals to UPDATED_MIDDLE_NAME
        defaultPersonShouldNotBeFound("middleName.equals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByMiddleNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where middleName not equals to DEFAULT_MIDDLE_NAME
        defaultPersonShouldNotBeFound("middleName.notEquals=" + DEFAULT_MIDDLE_NAME);

        // Get all the personList where middleName not equals to UPDATED_MIDDLE_NAME
        defaultPersonShouldBeFound("middleName.notEquals=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByMiddleNameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where middleName in DEFAULT_MIDDLE_NAME or UPDATED_MIDDLE_NAME
        defaultPersonShouldBeFound("middleName.in=" + DEFAULT_MIDDLE_NAME + "," + UPDATED_MIDDLE_NAME);

        // Get all the personList where middleName equals to UPDATED_MIDDLE_NAME
        defaultPersonShouldNotBeFound("middleName.in=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByMiddleNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where middleName is not null
        defaultPersonShouldBeFound("middleName.specified=true");

        // Get all the personList where middleName is null
        defaultPersonShouldNotBeFound("middleName.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByMiddleNameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where middleName contains DEFAULT_MIDDLE_NAME
        defaultPersonShouldBeFound("middleName.contains=" + DEFAULT_MIDDLE_NAME);

        // Get all the personList where middleName contains UPDATED_MIDDLE_NAME
        defaultPersonShouldNotBeFound("middleName.contains=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByMiddleNameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where middleName does not contain DEFAULT_MIDDLE_NAME
        defaultPersonShouldNotBeFound("middleName.doesNotContain=" + DEFAULT_MIDDLE_NAME);

        // Get all the personList where middleName does not contain UPDATED_MIDDLE_NAME
        defaultPersonShouldBeFound("middleName.doesNotContain=" + UPDATED_MIDDLE_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastName equals to DEFAULT_LAST_NAME
        defaultPersonShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the personList where lastName equals to UPDATED_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastName not equals to DEFAULT_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the personList where lastName not equals to UPDATED_LAST_NAME
        defaultPersonShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultPersonShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the personList where lastName equals to UPDATED_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastName is not null
        defaultPersonShouldBeFound("lastName.specified=true");

        // Get all the personList where lastName is null
        defaultPersonShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByLastNameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastName contains DEFAULT_LAST_NAME
        defaultPersonShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the personList where lastName contains UPDATED_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where lastName does not contain DEFAULT_LAST_NAME
        defaultPersonShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the personList where lastName does not contain UPDATED_LAST_NAME
        defaultPersonShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleBySuffixIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where suffix equals to DEFAULT_SUFFIX
        defaultPersonShouldBeFound("suffix.equals=" + DEFAULT_SUFFIX);

        // Get all the personList where suffix equals to UPDATED_SUFFIX
        defaultPersonShouldNotBeFound("suffix.equals=" + UPDATED_SUFFIX);
    }

    @Test
    @Transactional
    void getAllPeopleBySuffixIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where suffix not equals to DEFAULT_SUFFIX
        defaultPersonShouldNotBeFound("suffix.notEquals=" + DEFAULT_SUFFIX);

        // Get all the personList where suffix not equals to UPDATED_SUFFIX
        defaultPersonShouldBeFound("suffix.notEquals=" + UPDATED_SUFFIX);
    }

    @Test
    @Transactional
    void getAllPeopleBySuffixIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where suffix in DEFAULT_SUFFIX or UPDATED_SUFFIX
        defaultPersonShouldBeFound("suffix.in=" + DEFAULT_SUFFIX + "," + UPDATED_SUFFIX);

        // Get all the personList where suffix equals to UPDATED_SUFFIX
        defaultPersonShouldNotBeFound("suffix.in=" + UPDATED_SUFFIX);
    }

    @Test
    @Transactional
    void getAllPeopleBySuffixIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where suffix is not null
        defaultPersonShouldBeFound("suffix.specified=true");

        // Get all the personList where suffix is null
        defaultPersonShouldNotBeFound("suffix.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleBySuffixContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where suffix contains DEFAULT_SUFFIX
        defaultPersonShouldBeFound("suffix.contains=" + DEFAULT_SUFFIX);

        // Get all the personList where suffix contains UPDATED_SUFFIX
        defaultPersonShouldNotBeFound("suffix.contains=" + UPDATED_SUFFIX);
    }

    @Test
    @Transactional
    void getAllPeopleBySuffixNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where suffix does not contain DEFAULT_SUFFIX
        defaultPersonShouldNotBeFound("suffix.doesNotContain=" + DEFAULT_SUFFIX);

        // Get all the personList where suffix does not contain UPDATED_SUFFIX
        defaultPersonShouldBeFound("suffix.doesNotContain=" + UPDATED_SUFFIX);
    }

    @Test
    @Transactional
    void getAllPeopleByNameTagIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nameTag equals to DEFAULT_NAME_TAG
        defaultPersonShouldBeFound("nameTag.equals=" + DEFAULT_NAME_TAG);

        // Get all the personList where nameTag equals to UPDATED_NAME_TAG
        defaultPersonShouldNotBeFound("nameTag.equals=" + UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void getAllPeopleByNameTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nameTag not equals to DEFAULT_NAME_TAG
        defaultPersonShouldNotBeFound("nameTag.notEquals=" + DEFAULT_NAME_TAG);

        // Get all the personList where nameTag not equals to UPDATED_NAME_TAG
        defaultPersonShouldBeFound("nameTag.notEquals=" + UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void getAllPeopleByNameTagIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nameTag in DEFAULT_NAME_TAG or UPDATED_NAME_TAG
        defaultPersonShouldBeFound("nameTag.in=" + DEFAULT_NAME_TAG + "," + UPDATED_NAME_TAG);

        // Get all the personList where nameTag equals to UPDATED_NAME_TAG
        defaultPersonShouldNotBeFound("nameTag.in=" + UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void getAllPeopleByNameTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nameTag is not null
        defaultPersonShouldBeFound("nameTag.specified=true");

        // Get all the personList where nameTag is null
        defaultPersonShouldNotBeFound("nameTag.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByNameTagContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nameTag contains DEFAULT_NAME_TAG
        defaultPersonShouldBeFound("nameTag.contains=" + DEFAULT_NAME_TAG);

        // Get all the personList where nameTag contains UPDATED_NAME_TAG
        defaultPersonShouldNotBeFound("nameTag.contains=" + UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void getAllPeopleByNameTagNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nameTag does not contain DEFAULT_NAME_TAG
        defaultPersonShouldNotBeFound("nameTag.doesNotContain=" + DEFAULT_NAME_TAG);

        // Get all the personList where nameTag does not contain UPDATED_NAME_TAG
        defaultPersonShouldBeFound("nameTag.doesNotContain=" + UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void getAllPeopleByCurrentMemberIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where currentMember equals to DEFAULT_CURRENT_MEMBER
        defaultPersonShouldBeFound("currentMember.equals=" + DEFAULT_CURRENT_MEMBER);

        // Get all the personList where currentMember equals to UPDATED_CURRENT_MEMBER
        defaultPersonShouldNotBeFound("currentMember.equals=" + UPDATED_CURRENT_MEMBER);
    }

    @Test
    @Transactional
    void getAllPeopleByCurrentMemberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where currentMember not equals to DEFAULT_CURRENT_MEMBER
        defaultPersonShouldNotBeFound("currentMember.notEquals=" + DEFAULT_CURRENT_MEMBER);

        // Get all the personList where currentMember not equals to UPDATED_CURRENT_MEMBER
        defaultPersonShouldBeFound("currentMember.notEquals=" + UPDATED_CURRENT_MEMBER);
    }

    @Test
    @Transactional
    void getAllPeopleByCurrentMemberIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where currentMember in DEFAULT_CURRENT_MEMBER or UPDATED_CURRENT_MEMBER
        defaultPersonShouldBeFound("currentMember.in=" + DEFAULT_CURRENT_MEMBER + "," + UPDATED_CURRENT_MEMBER);

        // Get all the personList where currentMember equals to UPDATED_CURRENT_MEMBER
        defaultPersonShouldNotBeFound("currentMember.in=" + UPDATED_CURRENT_MEMBER);
    }

    @Test
    @Transactional
    void getAllPeopleByCurrentMemberIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where currentMember is not null
        defaultPersonShouldBeFound("currentMember.specified=true");

        // Get all the personList where currentMember is null
        defaultPersonShouldNotBeFound("currentMember.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipStartDate equals to DEFAULT_MEMBERSHIP_START_DATE
        defaultPersonShouldBeFound("membershipStartDate.equals=" + DEFAULT_MEMBERSHIP_START_DATE);

        // Get all the personList where membershipStartDate equals to UPDATED_MEMBERSHIP_START_DATE
        defaultPersonShouldNotBeFound("membershipStartDate.equals=" + UPDATED_MEMBERSHIP_START_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipStartDate not equals to DEFAULT_MEMBERSHIP_START_DATE
        defaultPersonShouldNotBeFound("membershipStartDate.notEquals=" + DEFAULT_MEMBERSHIP_START_DATE);

        // Get all the personList where membershipStartDate not equals to UPDATED_MEMBERSHIP_START_DATE
        defaultPersonShouldBeFound("membershipStartDate.notEquals=" + UPDATED_MEMBERSHIP_START_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipStartDate in DEFAULT_MEMBERSHIP_START_DATE or UPDATED_MEMBERSHIP_START_DATE
        defaultPersonShouldBeFound("membershipStartDate.in=" + DEFAULT_MEMBERSHIP_START_DATE + "," + UPDATED_MEMBERSHIP_START_DATE);

        // Get all the personList where membershipStartDate equals to UPDATED_MEMBERSHIP_START_DATE
        defaultPersonShouldNotBeFound("membershipStartDate.in=" + UPDATED_MEMBERSHIP_START_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipStartDate is not null
        defaultPersonShouldBeFound("membershipStartDate.specified=true");

        // Get all the personList where membershipStartDate is null
        defaultPersonShouldNotBeFound("membershipStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipStartDate is greater than or equal to DEFAULT_MEMBERSHIP_START_DATE
        defaultPersonShouldBeFound("membershipStartDate.greaterThanOrEqual=" + DEFAULT_MEMBERSHIP_START_DATE);

        // Get all the personList where membershipStartDate is greater than or equal to UPDATED_MEMBERSHIP_START_DATE
        defaultPersonShouldNotBeFound("membershipStartDate.greaterThanOrEqual=" + UPDATED_MEMBERSHIP_START_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipStartDate is less than or equal to DEFAULT_MEMBERSHIP_START_DATE
        defaultPersonShouldBeFound("membershipStartDate.lessThanOrEqual=" + DEFAULT_MEMBERSHIP_START_DATE);

        // Get all the personList where membershipStartDate is less than or equal to SMALLER_MEMBERSHIP_START_DATE
        defaultPersonShouldNotBeFound("membershipStartDate.lessThanOrEqual=" + SMALLER_MEMBERSHIP_START_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipStartDate is less than DEFAULT_MEMBERSHIP_START_DATE
        defaultPersonShouldNotBeFound("membershipStartDate.lessThan=" + DEFAULT_MEMBERSHIP_START_DATE);

        // Get all the personList where membershipStartDate is less than UPDATED_MEMBERSHIP_START_DATE
        defaultPersonShouldBeFound("membershipStartDate.lessThan=" + UPDATED_MEMBERSHIP_START_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipStartDate is greater than DEFAULT_MEMBERSHIP_START_DATE
        defaultPersonShouldNotBeFound("membershipStartDate.greaterThan=" + DEFAULT_MEMBERSHIP_START_DATE);

        // Get all the personList where membershipStartDate is greater than SMALLER_MEMBERSHIP_START_DATE
        defaultPersonShouldBeFound("membershipStartDate.greaterThan=" + SMALLER_MEMBERSHIP_START_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipExpirationDate equals to DEFAULT_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldBeFound("membershipExpirationDate.equals=" + DEFAULT_MEMBERSHIP_EXPIRATION_DATE);

        // Get all the personList where membershipExpirationDate equals to UPDATED_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldNotBeFound("membershipExpirationDate.equals=" + UPDATED_MEMBERSHIP_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipExpirationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipExpirationDate not equals to DEFAULT_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldNotBeFound("membershipExpirationDate.notEquals=" + DEFAULT_MEMBERSHIP_EXPIRATION_DATE);

        // Get all the personList where membershipExpirationDate not equals to UPDATED_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldBeFound("membershipExpirationDate.notEquals=" + UPDATED_MEMBERSHIP_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipExpirationDate in DEFAULT_MEMBERSHIP_EXPIRATION_DATE or UPDATED_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldBeFound(
            "membershipExpirationDate.in=" + DEFAULT_MEMBERSHIP_EXPIRATION_DATE + "," + UPDATED_MEMBERSHIP_EXPIRATION_DATE
        );

        // Get all the personList where membershipExpirationDate equals to UPDATED_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldNotBeFound("membershipExpirationDate.in=" + UPDATED_MEMBERSHIP_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipExpirationDate is not null
        defaultPersonShouldBeFound("membershipExpirationDate.specified=true");

        // Get all the personList where membershipExpirationDate is null
        defaultPersonShouldNotBeFound("membershipExpirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipExpirationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipExpirationDate is greater than or equal to DEFAULT_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldBeFound("membershipExpirationDate.greaterThanOrEqual=" + DEFAULT_MEMBERSHIP_EXPIRATION_DATE);

        // Get all the personList where membershipExpirationDate is greater than or equal to UPDATED_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldNotBeFound("membershipExpirationDate.greaterThanOrEqual=" + UPDATED_MEMBERSHIP_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipExpirationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipExpirationDate is less than or equal to DEFAULT_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldBeFound("membershipExpirationDate.lessThanOrEqual=" + DEFAULT_MEMBERSHIP_EXPIRATION_DATE);

        // Get all the personList where membershipExpirationDate is less than or equal to SMALLER_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldNotBeFound("membershipExpirationDate.lessThanOrEqual=" + SMALLER_MEMBERSHIP_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipExpirationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipExpirationDate is less than DEFAULT_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldNotBeFound("membershipExpirationDate.lessThan=" + DEFAULT_MEMBERSHIP_EXPIRATION_DATE);

        // Get all the personList where membershipExpirationDate is less than UPDATED_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldBeFound("membershipExpirationDate.lessThan=" + UPDATED_MEMBERSHIP_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipExpirationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where membershipExpirationDate is greater than DEFAULT_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldNotBeFound("membershipExpirationDate.greaterThan=" + DEFAULT_MEMBERSHIP_EXPIRATION_DATE);

        // Get all the personList where membershipExpirationDate is greater than SMALLER_MEMBERSHIP_EXPIRATION_DATE
        defaultPersonShouldBeFound("membershipExpirationDate.greaterThan=" + SMALLER_MEMBERSHIP_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllPeopleByIsHeadOfHouseIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where isHeadOfHouse equals to DEFAULT_IS_HEAD_OF_HOUSE
        defaultPersonShouldBeFound("isHeadOfHouse.equals=" + DEFAULT_IS_HEAD_OF_HOUSE);

        // Get all the personList where isHeadOfHouse equals to UPDATED_IS_HEAD_OF_HOUSE
        defaultPersonShouldNotBeFound("isHeadOfHouse.equals=" + UPDATED_IS_HEAD_OF_HOUSE);
    }

    @Test
    @Transactional
    void getAllPeopleByIsHeadOfHouseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where isHeadOfHouse not equals to DEFAULT_IS_HEAD_OF_HOUSE
        defaultPersonShouldNotBeFound("isHeadOfHouse.notEquals=" + DEFAULT_IS_HEAD_OF_HOUSE);

        // Get all the personList where isHeadOfHouse not equals to UPDATED_IS_HEAD_OF_HOUSE
        defaultPersonShouldBeFound("isHeadOfHouse.notEquals=" + UPDATED_IS_HEAD_OF_HOUSE);
    }

    @Test
    @Transactional
    void getAllPeopleByIsHeadOfHouseIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where isHeadOfHouse in DEFAULT_IS_HEAD_OF_HOUSE or UPDATED_IS_HEAD_OF_HOUSE
        defaultPersonShouldBeFound("isHeadOfHouse.in=" + DEFAULT_IS_HEAD_OF_HOUSE + "," + UPDATED_IS_HEAD_OF_HOUSE);

        // Get all the personList where isHeadOfHouse equals to UPDATED_IS_HEAD_OF_HOUSE
        defaultPersonShouldNotBeFound("isHeadOfHouse.in=" + UPDATED_IS_HEAD_OF_HOUSE);
    }

    @Test
    @Transactional
    void getAllPeopleByIsHeadOfHouseIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where isHeadOfHouse is not null
        defaultPersonShouldBeFound("isHeadOfHouse.specified=true");

        // Get all the personList where isHeadOfHouse is null
        defaultPersonShouldNotBeFound("isHeadOfHouse.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByIsDeceasedIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where isDeceased equals to DEFAULT_IS_DECEASED
        defaultPersonShouldBeFound("isDeceased.equals=" + DEFAULT_IS_DECEASED);

        // Get all the personList where isDeceased equals to UPDATED_IS_DECEASED
        defaultPersonShouldNotBeFound("isDeceased.equals=" + UPDATED_IS_DECEASED);
    }

    @Test
    @Transactional
    void getAllPeopleByIsDeceasedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where isDeceased not equals to DEFAULT_IS_DECEASED
        defaultPersonShouldNotBeFound("isDeceased.notEquals=" + DEFAULT_IS_DECEASED);

        // Get all the personList where isDeceased not equals to UPDATED_IS_DECEASED
        defaultPersonShouldBeFound("isDeceased.notEquals=" + UPDATED_IS_DECEASED);
    }

    @Test
    @Transactional
    void getAllPeopleByIsDeceasedIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where isDeceased in DEFAULT_IS_DECEASED or UPDATED_IS_DECEASED
        defaultPersonShouldBeFound("isDeceased.in=" + DEFAULT_IS_DECEASED + "," + UPDATED_IS_DECEASED);

        // Get all the personList where isDeceased equals to UPDATED_IS_DECEASED
        defaultPersonShouldNotBeFound("isDeceased.in=" + UPDATED_IS_DECEASED);
    }

    @Test
    @Transactional
    void getAllPeopleByIsDeceasedIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where isDeceased is not null
        defaultPersonShouldBeFound("isDeceased.specified=true");

        // Get all the personList where isDeceased is null
        defaultPersonShouldNotBeFound("isDeceased.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleBySpouseIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Person spouse = PersonResourceIT.createEntity(em);
        em.persist(spouse);
        em.flush();
        person.setSpouse(spouse);
        personRepository.saveAndFlush(person);
        Long spouseId = spouse.getId();

        // Get all the personList where spouse equals to spouseId
        defaultPersonShouldBeFound("spouseId.equals=" + spouseId);

        // Get all the personList where spouse equals to (spouseId + 1)
        defaultPersonShouldNotBeFound("spouseId.equals=" + (spouseId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByMembershipLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        MembershipLevel membershipLevel = MembershipLevelResourceIT.createEntity(em);
        em.persist(membershipLevel);
        em.flush();
        person.setMembershipLevel(membershipLevel);
        personRepository.saveAndFlush(person);
        Long membershipLevelId = membershipLevel.getId();

        // Get all the personList where membershipLevel equals to membershipLevelId
        defaultPersonShouldBeFound("membershipLevelId.equals=" + membershipLevelId);

        // Get all the personList where membershipLevel equals to (membershipLevelId + 1)
        defaultPersonShouldNotBeFound("membershipLevelId.equals=" + (membershipLevelId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByHeadOfHouseIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Person headOfHouse = PersonResourceIT.createEntity(em);
        em.persist(headOfHouse);
        em.flush();
        person.setHeadOfHouse(headOfHouse);
        personRepository.saveAndFlush(person);
        Long headOfHouseId = headOfHouse.getId();

        // Get all the personList where headOfHouse equals to headOfHouseId
        defaultPersonShouldBeFound("headOfHouseId.equals=" + headOfHouseId);

        // Get all the personList where headOfHouse equals to (headOfHouseId + 1)
        defaultPersonShouldNotBeFound("headOfHouseId.equals=" + (headOfHouseId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByParishIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Parish parish = ParishResourceIT.createEntity(em);
        em.persist(parish);
        em.flush();
        person.setParish(parish);
        personRepository.saveAndFlush(person);
        Long parishId = parish.getId();

        // Get all the personList where parish equals to parishId
        defaultPersonShouldBeFound("parishId.equals=" + parishId);

        // Get all the personList where parish equals to (parishId + 1)
        defaultPersonShouldNotBeFound("parishId.equals=" + (parishId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByRelationshipIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Relationship relationship = RelationshipResourceIT.createEntity(em);
        em.persist(relationship);
        em.flush();
        person.setRelationship(relationship);
        personRepository.saveAndFlush(person);
        Long relationshipId = relationship.getId();

        // Get all the personList where relationship equals to relationshipId
        defaultPersonShouldBeFound("relationshipId.equals=" + relationshipId);

        // Get all the personList where relationship equals to (relationshipId + 1)
        defaultPersonShouldNotBeFound("relationshipId.equals=" + (relationshipId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByOrganizationsIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Organization organizations = OrganizationResourceIT.createEntity(em);
        em.persist(organizations);
        em.flush();
        person.addOrganizations(organizations);
        personRepository.saveAndFlush(person);
        Long organizationsId = organizations.getId();

        // Get all the personList where organizations equals to organizationsId
        defaultPersonShouldBeFound("organizationsId.equals=" + organizationsId);

        // Get all the personList where organizations equals to (organizationsId + 1)
        defaultPersonShouldNotBeFound("organizationsId.equals=" + (organizationsId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByHouseDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        HouseDetails houseDetails = HouseDetailsResourceIT.createEntity(em);
        em.persist(houseDetails);
        em.flush();
        person.setHouseDetails(houseDetails);
        houseDetails.setHeadOfHouse(person);
        personRepository.saveAndFlush(person);
        Long houseDetailsId = houseDetails.getId();

        // Get all the personList where houseDetails equals to houseDetailsId
        defaultPersonShouldBeFound("houseDetailsId.equals=" + houseDetailsId);

        // Get all the personList where houseDetails equals to (houseDetailsId + 1)
        defaultPersonShouldNotBeFound("houseDetailsId.equals=" + (houseDetailsId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        PersonNotes notes = PersonNotesResourceIT.createEntity(em);
        em.persist(notes);
        em.flush();
        person.setNotes(notes);
        notes.setPerson(person);
        personRepository.saveAndFlush(person);
        Long notesId = notes.getId();

        // Get all the personList where notes equals to notesId
        defaultPersonShouldBeFound("notesId.equals=" + notesId);

        // Get all the personList where notes equals to (notesId + 1)
        defaultPersonShouldNotBeFound("notesId.equals=" + (notesId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByPhonesIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        PersonPhone phones = PersonPhoneResourceIT.createEntity(em);
        em.persist(phones);
        em.flush();
        person.addPhones(phones);
        personRepository.saveAndFlush(person);
        Long phonesId = phones.getId();

        // Get all the personList where phones equals to phonesId
        defaultPersonShouldBeFound("phonesId.equals=" + phonesId);

        // Get all the personList where phones equals to (phonesId + 1)
        defaultPersonShouldNotBeFound("phonesId.equals=" + (phonesId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByPaymentsIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Payment payments = PaymentResourceIT.createEntity(em);
        em.persist(payments);
        em.flush();
        person.addPayments(payments);
        personRepository.saveAndFlush(person);
        Long paymentsId = payments.getId();

        // Get all the personList where payments equals to paymentsId
        defaultPersonShouldBeFound("paymentsId.equals=" + paymentsId);

        // Get all the personList where payments equals to (paymentsId + 1)
        defaultPersonShouldNotBeFound("paymentsId.equals=" + (paymentsId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByEmailsIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        PersonEmail emails = PersonEmailResourceIT.createEntity(em);
        em.persist(emails);
        em.flush();
        person.addEmails(emails);
        personRepository.saveAndFlush(person);
        Long emailsId = emails.getId();

        // Get all the personList where emails equals to emailsId
        defaultPersonShouldBeFound("emailsId.equals=" + emailsId);

        // Get all the personList where emails equals to (emailsId + 1)
        defaultPersonShouldNotBeFound("emailsId.equals=" + (emailsId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByPersonsInHouseIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Person personsInHouse = PersonResourceIT.createEntity(em);
        em.persist(personsInHouse);
        em.flush();
        person.addPersonsInHouse(personsInHouse);
        personRepository.saveAndFlush(person);
        Long personsInHouseId = personsInHouse.getId();

        // Get all the personList where personsInHouse equals to personsInHouseId
        defaultPersonShouldBeFound("personsInHouseId.equals=" + personsInHouseId);

        // Get all the personList where personsInHouse equals to (personsInHouseId + 1)
        defaultPersonShouldNotBeFound("personsInHouseId.equals=" + (personsInHouseId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByTicketsIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);
        Ticket tickets = TicketResourceIT.createEntity(em);
        em.persist(tickets);
        em.flush();
        person.addTickets(tickets);
        personRepository.saveAndFlush(person);
        Long ticketsId = tickets.getId();

        // Get all the personList where tickets equals to ticketsId
        defaultPersonShouldBeFound("ticketsId.equals=" + ticketsId);

        // Get all the personList where tickets equals to (ticketsId + 1)
        defaultPersonShouldNotBeFound("ticketsId.equals=" + (ticketsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonShouldBeFound(String filter) throws Exception {
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].prefix").value(hasItem(DEFAULT_PREFIX)))
            .andExpect(jsonPath("$.[*].preferredName").value(hasItem(DEFAULT_PREFERRED_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].middleName").value(hasItem(DEFAULT_MIDDLE_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].suffix").value(hasItem(DEFAULT_SUFFIX)))
            .andExpect(jsonPath("$.[*].nameTag").value(hasItem(DEFAULT_NAME_TAG)))
            .andExpect(jsonPath("$.[*].currentMember").value(hasItem(DEFAULT_CURRENT_MEMBER.booleanValue())))
            .andExpect(jsonPath("$.[*].membershipStartDate").value(hasItem(DEFAULT_MEMBERSHIP_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].membershipExpirationDate").value(hasItem(DEFAULT_MEMBERSHIP_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].isHeadOfHouse").value(hasItem(DEFAULT_IS_HEAD_OF_HOUSE.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeceased").value(hasItem(DEFAULT_IS_DECEASED.booleanValue())));

        // Check, that the count call also returns 1
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonShouldNotBeFound(String filter) throws Exception {
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person
        Person updatedPerson = personRepository.findById(person.getId()).get();
        // Disconnect from session so that the updates on updatedPerson are not directly saved in db
        em.detach(updatedPerson);
        updatedPerson
            .prefix(UPDATED_PREFIX)
            .preferredName(UPDATED_PREFERRED_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .suffix(UPDATED_SUFFIX)
            .nameTag(UPDATED_NAME_TAG)
            .currentMember(UPDATED_CURRENT_MEMBER)
            .membershipStartDate(UPDATED_MEMBERSHIP_START_DATE)
            .membershipExpirationDate(UPDATED_MEMBERSHIP_EXPIRATION_DATE)
            .isHeadOfHouse(UPDATED_IS_HEAD_OF_HOUSE)
            .isDeceased(UPDATED_IS_DECEASED);

        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPerson.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPerson))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testPerson.getPreferredName()).isEqualTo(UPDATED_PREFERRED_NAME);
        assertThat(testPerson.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPerson.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPerson.getSuffix()).isEqualTo(UPDATED_SUFFIX);
        assertThat(testPerson.getNameTag()).isEqualTo(UPDATED_NAME_TAG);
        assertThat(testPerson.getCurrentMember()).isEqualTo(UPDATED_CURRENT_MEMBER);
        assertThat(testPerson.getMembershipStartDate()).isEqualTo(UPDATED_MEMBERSHIP_START_DATE);
        assertThat(testPerson.getMembershipExpirationDate()).isEqualTo(UPDATED_MEMBERSHIP_EXPIRATION_DATE);
        assertThat(testPerson.getIsHeadOfHouse()).isEqualTo(UPDATED_IS_HEAD_OF_HOUSE);
        assertThat(testPerson.getIsDeceased()).isEqualTo(UPDATED_IS_DECEASED);
    }

    @Test
    @Transactional
    void putNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, person.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(person))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(person))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonWithPatch() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person using partial update
        Person partialUpdatedPerson = new Person();
        partialUpdatedPerson.setId(person.getId());

        partialUpdatedPerson
            .preferredName(UPDATED_PREFERRED_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .membershipStartDate(UPDATED_MEMBERSHIP_START_DATE)
            .membershipExpirationDate(UPDATED_MEMBERSHIP_EXPIRATION_DATE)
            .isHeadOfHouse(UPDATED_IS_HEAD_OF_HOUSE);

        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerson))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getPrefix()).isEqualTo(DEFAULT_PREFIX);
        assertThat(testPerson.getPreferredName()).isEqualTo(UPDATED_PREFERRED_NAME);
        assertThat(testPerson.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPerson.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPerson.getSuffix()).isEqualTo(DEFAULT_SUFFIX);
        assertThat(testPerson.getNameTag()).isEqualTo(DEFAULT_NAME_TAG);
        assertThat(testPerson.getCurrentMember()).isEqualTo(DEFAULT_CURRENT_MEMBER);
        assertThat(testPerson.getMembershipStartDate()).isEqualTo(UPDATED_MEMBERSHIP_START_DATE);
        assertThat(testPerson.getMembershipExpirationDate()).isEqualTo(UPDATED_MEMBERSHIP_EXPIRATION_DATE);
        assertThat(testPerson.getIsHeadOfHouse()).isEqualTo(UPDATED_IS_HEAD_OF_HOUSE);
        assertThat(testPerson.getIsDeceased()).isEqualTo(DEFAULT_IS_DECEASED);
    }

    @Test
    @Transactional
    void fullUpdatePersonWithPatch() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person using partial update
        Person partialUpdatedPerson = new Person();
        partialUpdatedPerson.setId(person.getId());

        partialUpdatedPerson
            .prefix(UPDATED_PREFIX)
            .preferredName(UPDATED_PREFERRED_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .middleName(UPDATED_MIDDLE_NAME)
            .lastName(UPDATED_LAST_NAME)
            .suffix(UPDATED_SUFFIX)
            .nameTag(UPDATED_NAME_TAG)
            .currentMember(UPDATED_CURRENT_MEMBER)
            .membershipStartDate(UPDATED_MEMBERSHIP_START_DATE)
            .membershipExpirationDate(UPDATED_MEMBERSHIP_EXPIRATION_DATE)
            .isHeadOfHouse(UPDATED_IS_HEAD_OF_HOUSE)
            .isDeceased(UPDATED_IS_DECEASED);

        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerson))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getPrefix()).isEqualTo(UPDATED_PREFIX);
        assertThat(testPerson.getPreferredName()).isEqualTo(UPDATED_PREFERRED_NAME);
        assertThat(testPerson.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPerson.getMiddleName()).isEqualTo(UPDATED_MIDDLE_NAME);
        assertThat(testPerson.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPerson.getSuffix()).isEqualTo(UPDATED_SUFFIX);
        assertThat(testPerson.getNameTag()).isEqualTo(UPDATED_NAME_TAG);
        assertThat(testPerson.getCurrentMember()).isEqualTo(UPDATED_CURRENT_MEMBER);
        assertThat(testPerson.getMembershipStartDate()).isEqualTo(UPDATED_MEMBERSHIP_START_DATE);
        assertThat(testPerson.getMembershipExpirationDate()).isEqualTo(UPDATED_MEMBERSHIP_EXPIRATION_DATE);
        assertThat(testPerson.getIsHeadOfHouse()).isEqualTo(UPDATED_IS_HEAD_OF_HOUSE);
        assertThat(testPerson.getIsDeceased()).isEqualTo(UPDATED_IS_DECEASED);
    }

    @Test
    @Transactional
    void patchNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, person.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(person))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(person))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(person)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeDelete = personRepository.findAll().size();

        // Delete the person
        restPersonMockMvc
            .perform(delete(ENTITY_API_URL_ID, person.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
