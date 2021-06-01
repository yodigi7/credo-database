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
import com.credo.database.domain.Event;
import com.credo.database.domain.MembershipLevel;
import com.credo.database.domain.Person;
import com.credo.database.domain.Ticket;
import com.credo.database.domain.Transaction;
import com.credo.database.repository.TransactionRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionResourceIT {

    private static final Double DEFAULT_TOTAL_AMOUNT = 0D;
    private static final Double UPDATED_TOTAL_AMOUNT = 1D;
    private static final Double SMALLER_TOTAL_AMOUNT = 0D - 1D;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_GENERIC_SUB_ITEMS_PURCHASED = "AAAAAAAAAA";
    private static final String UPDATED_GENERIC_SUB_ITEMS_PURCHASED = "BBBBBBBBBB";

    private static final Double DEFAULT_COST_SUB_ITEMS_PURCHASED = 0D;
    private static final Double UPDATED_COST_SUB_ITEMS_PURCHASED = 1D;
    private static final Double SMALLER_COST_SUB_ITEMS_PURCHASED = 0D - 1D;

    private static final Integer DEFAULT_NUMBER_OF_MEMBERSHIPS = 0;
    private static final Integer UPDATED_NUMBER_OF_MEMBERSHIPS = 1;
    private static final Integer SMALLER_NUMBER_OF_MEMBERSHIPS = 0 - 1;

    private static final Double DEFAULT_DONATION = 0D;
    private static final Double UPDATED_DONATION = 1D;
    private static final Double SMALLER_DONATION = 0D - 1D;

    private static final Double DEFAULT_EVENT_DONATION = 0D;
    private static final Double UPDATED_EVENT_DONATION = 1D;
    private static final Double SMALLER_EVENT_DONATION = 0D - 1D;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionMockMvc;

    private Transaction transaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .date(DEFAULT_DATE)
            .genericSubItemsPurchased(DEFAULT_GENERIC_SUB_ITEMS_PURCHASED)
            .costSubItemsPurchased(DEFAULT_COST_SUB_ITEMS_PURCHASED)
            .numberOfMemberships(DEFAULT_NUMBER_OF_MEMBERSHIPS)
            .donation(DEFAULT_DONATION)
            .eventDonation(DEFAULT_EVENT_DONATION)
            .notes(DEFAULT_NOTES);
        return transaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transaction createUpdatedEntity(EntityManager em) {
        Transaction transaction = new Transaction()
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .date(UPDATED_DATE)
            .genericSubItemsPurchased(UPDATED_GENERIC_SUB_ITEMS_PURCHASED)
            .costSubItemsPurchased(UPDATED_COST_SUB_ITEMS_PURCHASED)
            .numberOfMemberships(UPDATED_NUMBER_OF_MEMBERSHIPS)
            .donation(UPDATED_DONATION)
            .eventDonation(UPDATED_EVENT_DONATION)
            .notes(UPDATED_NOTES);
        return transaction;
    }

    @BeforeEach
    public void initTest() {
        transaction = createEntity(em);
    }

    @Test
    @Transactional
    void createTransaction() throws Exception {
        int databaseSizeBeforeCreate = transactionRepository.findAll().size();
        // Create the Transaction
        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isCreated());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate + 1);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testTransaction.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testTransaction.getGenericSubItemsPurchased()).isEqualTo(DEFAULT_GENERIC_SUB_ITEMS_PURCHASED);
        assertThat(testTransaction.getCostSubItemsPurchased()).isEqualTo(DEFAULT_COST_SUB_ITEMS_PURCHASED);
        assertThat(testTransaction.getNumberOfMemberships()).isEqualTo(DEFAULT_NUMBER_OF_MEMBERSHIPS);
        assertThat(testTransaction.getDonation()).isEqualTo(DEFAULT_DONATION);
        assertThat(testTransaction.getEventDonation()).isEqualTo(DEFAULT_EVENT_DONATION);
        assertThat(testTransaction.getNotes()).isEqualTo(DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void createTransactionWithExistingId() throws Exception {
        // Create the Transaction with an existing ID
        transaction.setId(1L);

        int databaseSizeBeforeCreate = transactionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTransactions() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].genericSubItemsPurchased").value(hasItem(DEFAULT_GENERIC_SUB_ITEMS_PURCHASED)))
            .andExpect(jsonPath("$.[*].costSubItemsPurchased").value(hasItem(DEFAULT_COST_SUB_ITEMS_PURCHASED.doubleValue())))
            .andExpect(jsonPath("$.[*].numberOfMemberships").value(hasItem(DEFAULT_NUMBER_OF_MEMBERSHIPS)))
            .andExpect(jsonPath("$.[*].donation").value(hasItem(DEFAULT_DONATION.doubleValue())))
            .andExpect(jsonPath("$.[*].eventDonation").value(hasItem(DEFAULT_EVENT_DONATION.doubleValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get the transaction
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, transaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
            .andExpect(jsonPath("$.totalAmount").value(DEFAULT_TOTAL_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.genericSubItemsPurchased").value(DEFAULT_GENERIC_SUB_ITEMS_PURCHASED))
            .andExpect(jsonPath("$.costSubItemsPurchased").value(DEFAULT_COST_SUB_ITEMS_PURCHASED.doubleValue()))
            .andExpect(jsonPath("$.numberOfMemberships").value(DEFAULT_NUMBER_OF_MEMBERSHIPS))
            .andExpect(jsonPath("$.donation").value(DEFAULT_DONATION.doubleValue()))
            .andExpect(jsonPath("$.eventDonation").value(DEFAULT_EVENT_DONATION.doubleValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getTransactionsByIdFiltering() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        Long id = transaction.getId();

        defaultTransactionShouldBeFound("id.equals=" + id);
        defaultTransactionShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionsByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where totalAmount equals to DEFAULT_TOTAL_AMOUNT
        defaultTransactionShouldBeFound("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the transactionList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultTransactionShouldNotBeFound("totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByTotalAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where totalAmount not equals to DEFAULT_TOTAL_AMOUNT
        defaultTransactionShouldNotBeFound("totalAmount.notEquals=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the transactionList where totalAmount not equals to UPDATED_TOTAL_AMOUNT
        defaultTransactionShouldBeFound("totalAmount.notEquals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where totalAmount in DEFAULT_TOTAL_AMOUNT or UPDATED_TOTAL_AMOUNT
        defaultTransactionShouldBeFound("totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT);

        // Get all the transactionList where totalAmount equals to UPDATED_TOTAL_AMOUNT
        defaultTransactionShouldNotBeFound("totalAmount.in=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where totalAmount is not null
        defaultTransactionShouldBeFound("totalAmount.specified=true");

        // Get all the transactionList where totalAmount is null
        defaultTransactionShouldNotBeFound("totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where totalAmount is greater than or equal to DEFAULT_TOTAL_AMOUNT
        defaultTransactionShouldBeFound("totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the transactionList where totalAmount is greater than or equal to UPDATED_TOTAL_AMOUNT
        defaultTransactionShouldNotBeFound("totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where totalAmount is less than or equal to DEFAULT_TOTAL_AMOUNT
        defaultTransactionShouldBeFound("totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the transactionList where totalAmount is less than or equal to SMALLER_TOTAL_AMOUNT
        defaultTransactionShouldNotBeFound("totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where totalAmount is less than DEFAULT_TOTAL_AMOUNT
        defaultTransactionShouldNotBeFound("totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the transactionList where totalAmount is less than UPDATED_TOTAL_AMOUNT
        defaultTransactionShouldBeFound("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where totalAmount is greater than DEFAULT_TOTAL_AMOUNT
        defaultTransactionShouldNotBeFound("totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT);

        // Get all the transactionList where totalAmount is greater than SMALLER_TOTAL_AMOUNT
        defaultTransactionShouldBeFound("totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where date equals to DEFAULT_DATE
        defaultTransactionShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the transactionList where date equals to UPDATED_DATE
        defaultTransactionShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where date not equals to DEFAULT_DATE
        defaultTransactionShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the transactionList where date not equals to UPDATED_DATE
        defaultTransactionShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where date in DEFAULT_DATE or UPDATED_DATE
        defaultTransactionShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the transactionList where date equals to UPDATED_DATE
        defaultTransactionShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where date is not null
        defaultTransactionShouldBeFound("date.specified=true");

        // Get all the transactionList where date is null
        defaultTransactionShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where date is greater than or equal to DEFAULT_DATE
        defaultTransactionShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the transactionList where date is greater than or equal to UPDATED_DATE
        defaultTransactionShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where date is less than or equal to DEFAULT_DATE
        defaultTransactionShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the transactionList where date is less than or equal to SMALLER_DATE
        defaultTransactionShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where date is less than DEFAULT_DATE
        defaultTransactionShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the transactionList where date is less than UPDATED_DATE
        defaultTransactionShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where date is greater than DEFAULT_DATE
        defaultTransactionShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the transactionList where date is greater than SMALLER_DATE
        defaultTransactionShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllTransactionsByGenericSubItemsPurchasedIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where genericSubItemsPurchased equals to DEFAULT_GENERIC_SUB_ITEMS_PURCHASED
        defaultTransactionShouldBeFound("genericSubItemsPurchased.equals=" + DEFAULT_GENERIC_SUB_ITEMS_PURCHASED);

        // Get all the transactionList where genericSubItemsPurchased equals to UPDATED_GENERIC_SUB_ITEMS_PURCHASED
        defaultTransactionShouldNotBeFound("genericSubItemsPurchased.equals=" + UPDATED_GENERIC_SUB_ITEMS_PURCHASED);
    }

    @Test
    @Transactional
    void getAllTransactionsByGenericSubItemsPurchasedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where genericSubItemsPurchased not equals to DEFAULT_GENERIC_SUB_ITEMS_PURCHASED
        defaultTransactionShouldNotBeFound("genericSubItemsPurchased.notEquals=" + DEFAULT_GENERIC_SUB_ITEMS_PURCHASED);

        // Get all the transactionList where genericSubItemsPurchased not equals to UPDATED_GENERIC_SUB_ITEMS_PURCHASED
        defaultTransactionShouldBeFound("genericSubItemsPurchased.notEquals=" + UPDATED_GENERIC_SUB_ITEMS_PURCHASED);
    }

    @Test
    @Transactional
    void getAllTransactionsByGenericSubItemsPurchasedIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where genericSubItemsPurchased in DEFAULT_GENERIC_SUB_ITEMS_PURCHASED or UPDATED_GENERIC_SUB_ITEMS_PURCHASED
        defaultTransactionShouldBeFound(
            "genericSubItemsPurchased.in=" + DEFAULT_GENERIC_SUB_ITEMS_PURCHASED + "," + UPDATED_GENERIC_SUB_ITEMS_PURCHASED
        );

        // Get all the transactionList where genericSubItemsPurchased equals to UPDATED_GENERIC_SUB_ITEMS_PURCHASED
        defaultTransactionShouldNotBeFound("genericSubItemsPurchased.in=" + UPDATED_GENERIC_SUB_ITEMS_PURCHASED);
    }

    @Test
    @Transactional
    void getAllTransactionsByGenericSubItemsPurchasedIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where genericSubItemsPurchased is not null
        defaultTransactionShouldBeFound("genericSubItemsPurchased.specified=true");

        // Get all the transactionList where genericSubItemsPurchased is null
        defaultTransactionShouldNotBeFound("genericSubItemsPurchased.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByGenericSubItemsPurchasedContainsSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where genericSubItemsPurchased contains DEFAULT_GENERIC_SUB_ITEMS_PURCHASED
        defaultTransactionShouldBeFound("genericSubItemsPurchased.contains=" + DEFAULT_GENERIC_SUB_ITEMS_PURCHASED);

        // Get all the transactionList where genericSubItemsPurchased contains UPDATED_GENERIC_SUB_ITEMS_PURCHASED
        defaultTransactionShouldNotBeFound("genericSubItemsPurchased.contains=" + UPDATED_GENERIC_SUB_ITEMS_PURCHASED);
    }

    @Test
    @Transactional
    void getAllTransactionsByGenericSubItemsPurchasedNotContainsSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where genericSubItemsPurchased does not contain DEFAULT_GENERIC_SUB_ITEMS_PURCHASED
        defaultTransactionShouldNotBeFound("genericSubItemsPurchased.doesNotContain=" + DEFAULT_GENERIC_SUB_ITEMS_PURCHASED);

        // Get all the transactionList where genericSubItemsPurchased does not contain UPDATED_GENERIC_SUB_ITEMS_PURCHASED
        defaultTransactionShouldBeFound("genericSubItemsPurchased.doesNotContain=" + UPDATED_GENERIC_SUB_ITEMS_PURCHASED);
    }

    @Test
    @Transactional
    void getAllTransactionsByCostSubItemsPurchasedIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where costSubItemsPurchased equals to DEFAULT_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldBeFound("costSubItemsPurchased.equals=" + DEFAULT_COST_SUB_ITEMS_PURCHASED);

        // Get all the transactionList where costSubItemsPurchased equals to UPDATED_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldNotBeFound("costSubItemsPurchased.equals=" + UPDATED_COST_SUB_ITEMS_PURCHASED);
    }

    @Test
    @Transactional
    void getAllTransactionsByCostSubItemsPurchasedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where costSubItemsPurchased not equals to DEFAULT_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldNotBeFound("costSubItemsPurchased.notEquals=" + DEFAULT_COST_SUB_ITEMS_PURCHASED);

        // Get all the transactionList where costSubItemsPurchased not equals to UPDATED_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldBeFound("costSubItemsPurchased.notEquals=" + UPDATED_COST_SUB_ITEMS_PURCHASED);
    }

    @Test
    @Transactional
    void getAllTransactionsByCostSubItemsPurchasedIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where costSubItemsPurchased in DEFAULT_COST_SUB_ITEMS_PURCHASED or UPDATED_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldBeFound(
            "costSubItemsPurchased.in=" + DEFAULT_COST_SUB_ITEMS_PURCHASED + "," + UPDATED_COST_SUB_ITEMS_PURCHASED
        );

        // Get all the transactionList where costSubItemsPurchased equals to UPDATED_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldNotBeFound("costSubItemsPurchased.in=" + UPDATED_COST_SUB_ITEMS_PURCHASED);
    }

    @Test
    @Transactional
    void getAllTransactionsByCostSubItemsPurchasedIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where costSubItemsPurchased is not null
        defaultTransactionShouldBeFound("costSubItemsPurchased.specified=true");

        // Get all the transactionList where costSubItemsPurchased is null
        defaultTransactionShouldNotBeFound("costSubItemsPurchased.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByCostSubItemsPurchasedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where costSubItemsPurchased is greater than or equal to DEFAULT_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldBeFound("costSubItemsPurchased.greaterThanOrEqual=" + DEFAULT_COST_SUB_ITEMS_PURCHASED);

        // Get all the transactionList where costSubItemsPurchased is greater than or equal to UPDATED_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldNotBeFound("costSubItemsPurchased.greaterThanOrEqual=" + UPDATED_COST_SUB_ITEMS_PURCHASED);
    }

    @Test
    @Transactional
    void getAllTransactionsByCostSubItemsPurchasedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where costSubItemsPurchased is less than or equal to DEFAULT_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldBeFound("costSubItemsPurchased.lessThanOrEqual=" + DEFAULT_COST_SUB_ITEMS_PURCHASED);

        // Get all the transactionList where costSubItemsPurchased is less than or equal to SMALLER_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldNotBeFound("costSubItemsPurchased.lessThanOrEqual=" + SMALLER_COST_SUB_ITEMS_PURCHASED);
    }

    @Test
    @Transactional
    void getAllTransactionsByCostSubItemsPurchasedIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where costSubItemsPurchased is less than DEFAULT_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldNotBeFound("costSubItemsPurchased.lessThan=" + DEFAULT_COST_SUB_ITEMS_PURCHASED);

        // Get all the transactionList where costSubItemsPurchased is less than UPDATED_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldBeFound("costSubItemsPurchased.lessThan=" + UPDATED_COST_SUB_ITEMS_PURCHASED);
    }

    @Test
    @Transactional
    void getAllTransactionsByCostSubItemsPurchasedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where costSubItemsPurchased is greater than DEFAULT_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldNotBeFound("costSubItemsPurchased.greaterThan=" + DEFAULT_COST_SUB_ITEMS_PURCHASED);

        // Get all the transactionList where costSubItemsPurchased is greater than SMALLER_COST_SUB_ITEMS_PURCHASED
        defaultTransactionShouldBeFound("costSubItemsPurchased.greaterThan=" + SMALLER_COST_SUB_ITEMS_PURCHASED);
    }

    @Test
    @Transactional
    void getAllTransactionsByNumberOfMembershipsIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where numberOfMemberships equals to DEFAULT_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldBeFound("numberOfMemberships.equals=" + DEFAULT_NUMBER_OF_MEMBERSHIPS);

        // Get all the transactionList where numberOfMemberships equals to UPDATED_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldNotBeFound("numberOfMemberships.equals=" + UPDATED_NUMBER_OF_MEMBERSHIPS);
    }

    @Test
    @Transactional
    void getAllTransactionsByNumberOfMembershipsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where numberOfMemberships not equals to DEFAULT_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldNotBeFound("numberOfMemberships.notEquals=" + DEFAULT_NUMBER_OF_MEMBERSHIPS);

        // Get all the transactionList where numberOfMemberships not equals to UPDATED_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldBeFound("numberOfMemberships.notEquals=" + UPDATED_NUMBER_OF_MEMBERSHIPS);
    }

    @Test
    @Transactional
    void getAllTransactionsByNumberOfMembershipsIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where numberOfMemberships in DEFAULT_NUMBER_OF_MEMBERSHIPS or UPDATED_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldBeFound("numberOfMemberships.in=" + DEFAULT_NUMBER_OF_MEMBERSHIPS + "," + UPDATED_NUMBER_OF_MEMBERSHIPS);

        // Get all the transactionList where numberOfMemberships equals to UPDATED_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldNotBeFound("numberOfMemberships.in=" + UPDATED_NUMBER_OF_MEMBERSHIPS);
    }

    @Test
    @Transactional
    void getAllTransactionsByNumberOfMembershipsIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where numberOfMemberships is not null
        defaultTransactionShouldBeFound("numberOfMemberships.specified=true");

        // Get all the transactionList where numberOfMemberships is null
        defaultTransactionShouldNotBeFound("numberOfMemberships.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByNumberOfMembershipsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where numberOfMemberships is greater than or equal to DEFAULT_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldBeFound("numberOfMemberships.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_MEMBERSHIPS);

        // Get all the transactionList where numberOfMemberships is greater than or equal to UPDATED_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldNotBeFound("numberOfMemberships.greaterThanOrEqual=" + UPDATED_NUMBER_OF_MEMBERSHIPS);
    }

    @Test
    @Transactional
    void getAllTransactionsByNumberOfMembershipsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where numberOfMemberships is less than or equal to DEFAULT_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldBeFound("numberOfMemberships.lessThanOrEqual=" + DEFAULT_NUMBER_OF_MEMBERSHIPS);

        // Get all the transactionList where numberOfMemberships is less than or equal to SMALLER_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldNotBeFound("numberOfMemberships.lessThanOrEqual=" + SMALLER_NUMBER_OF_MEMBERSHIPS);
    }

    @Test
    @Transactional
    void getAllTransactionsByNumberOfMembershipsIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where numberOfMemberships is less than DEFAULT_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldNotBeFound("numberOfMemberships.lessThan=" + DEFAULT_NUMBER_OF_MEMBERSHIPS);

        // Get all the transactionList where numberOfMemberships is less than UPDATED_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldBeFound("numberOfMemberships.lessThan=" + UPDATED_NUMBER_OF_MEMBERSHIPS);
    }

    @Test
    @Transactional
    void getAllTransactionsByNumberOfMembershipsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where numberOfMemberships is greater than DEFAULT_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldNotBeFound("numberOfMemberships.greaterThan=" + DEFAULT_NUMBER_OF_MEMBERSHIPS);

        // Get all the transactionList where numberOfMemberships is greater than SMALLER_NUMBER_OF_MEMBERSHIPS
        defaultTransactionShouldBeFound("numberOfMemberships.greaterThan=" + SMALLER_NUMBER_OF_MEMBERSHIPS);
    }

    @Test
    @Transactional
    void getAllTransactionsByDonationIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where donation equals to DEFAULT_DONATION
        defaultTransactionShouldBeFound("donation.equals=" + DEFAULT_DONATION);

        // Get all the transactionList where donation equals to UPDATED_DONATION
        defaultTransactionShouldNotBeFound("donation.equals=" + UPDATED_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDonationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where donation not equals to DEFAULT_DONATION
        defaultTransactionShouldNotBeFound("donation.notEquals=" + DEFAULT_DONATION);

        // Get all the transactionList where donation not equals to UPDATED_DONATION
        defaultTransactionShouldBeFound("donation.notEquals=" + UPDATED_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDonationIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where donation in DEFAULT_DONATION or UPDATED_DONATION
        defaultTransactionShouldBeFound("donation.in=" + DEFAULT_DONATION + "," + UPDATED_DONATION);

        // Get all the transactionList where donation equals to UPDATED_DONATION
        defaultTransactionShouldNotBeFound("donation.in=" + UPDATED_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDonationIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where donation is not null
        defaultTransactionShouldBeFound("donation.specified=true");

        // Get all the transactionList where donation is null
        defaultTransactionShouldNotBeFound("donation.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByDonationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where donation is greater than or equal to DEFAULT_DONATION
        defaultTransactionShouldBeFound("donation.greaterThanOrEqual=" + DEFAULT_DONATION);

        // Get all the transactionList where donation is greater than or equal to UPDATED_DONATION
        defaultTransactionShouldNotBeFound("donation.greaterThanOrEqual=" + UPDATED_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDonationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where donation is less than or equal to DEFAULT_DONATION
        defaultTransactionShouldBeFound("donation.lessThanOrEqual=" + DEFAULT_DONATION);

        // Get all the transactionList where donation is less than or equal to SMALLER_DONATION
        defaultTransactionShouldNotBeFound("donation.lessThanOrEqual=" + SMALLER_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDonationIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where donation is less than DEFAULT_DONATION
        defaultTransactionShouldNotBeFound("donation.lessThan=" + DEFAULT_DONATION);

        // Get all the transactionList where donation is less than UPDATED_DONATION
        defaultTransactionShouldBeFound("donation.lessThan=" + UPDATED_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByDonationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where donation is greater than DEFAULT_DONATION
        defaultTransactionShouldNotBeFound("donation.greaterThan=" + DEFAULT_DONATION);

        // Get all the transactionList where donation is greater than SMALLER_DONATION
        defaultTransactionShouldBeFound("donation.greaterThan=" + SMALLER_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByEventDonationIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where eventDonation equals to DEFAULT_EVENT_DONATION
        defaultTransactionShouldBeFound("eventDonation.equals=" + DEFAULT_EVENT_DONATION);

        // Get all the transactionList where eventDonation equals to UPDATED_EVENT_DONATION
        defaultTransactionShouldNotBeFound("eventDonation.equals=" + UPDATED_EVENT_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByEventDonationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where eventDonation not equals to DEFAULT_EVENT_DONATION
        defaultTransactionShouldNotBeFound("eventDonation.notEquals=" + DEFAULT_EVENT_DONATION);

        // Get all the transactionList where eventDonation not equals to UPDATED_EVENT_DONATION
        defaultTransactionShouldBeFound("eventDonation.notEquals=" + UPDATED_EVENT_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByEventDonationIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where eventDonation in DEFAULT_EVENT_DONATION or UPDATED_EVENT_DONATION
        defaultTransactionShouldBeFound("eventDonation.in=" + DEFAULT_EVENT_DONATION + "," + UPDATED_EVENT_DONATION);

        // Get all the transactionList where eventDonation equals to UPDATED_EVENT_DONATION
        defaultTransactionShouldNotBeFound("eventDonation.in=" + UPDATED_EVENT_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByEventDonationIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where eventDonation is not null
        defaultTransactionShouldBeFound("eventDonation.specified=true");

        // Get all the transactionList where eventDonation is null
        defaultTransactionShouldNotBeFound("eventDonation.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByEventDonationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where eventDonation is greater than or equal to DEFAULT_EVENT_DONATION
        defaultTransactionShouldBeFound("eventDonation.greaterThanOrEqual=" + DEFAULT_EVENT_DONATION);

        // Get all the transactionList where eventDonation is greater than or equal to UPDATED_EVENT_DONATION
        defaultTransactionShouldNotBeFound("eventDonation.greaterThanOrEqual=" + UPDATED_EVENT_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByEventDonationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where eventDonation is less than or equal to DEFAULT_EVENT_DONATION
        defaultTransactionShouldBeFound("eventDonation.lessThanOrEqual=" + DEFAULT_EVENT_DONATION);

        // Get all the transactionList where eventDonation is less than or equal to SMALLER_EVENT_DONATION
        defaultTransactionShouldNotBeFound("eventDonation.lessThanOrEqual=" + SMALLER_EVENT_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByEventDonationIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where eventDonation is less than DEFAULT_EVENT_DONATION
        defaultTransactionShouldNotBeFound("eventDonation.lessThan=" + DEFAULT_EVENT_DONATION);

        // Get all the transactionList where eventDonation is less than UPDATED_EVENT_DONATION
        defaultTransactionShouldBeFound("eventDonation.lessThan=" + UPDATED_EVENT_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByEventDonationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where eventDonation is greater than DEFAULT_EVENT_DONATION
        defaultTransactionShouldNotBeFound("eventDonation.greaterThan=" + DEFAULT_EVENT_DONATION);

        // Get all the transactionList where eventDonation is greater than SMALLER_EVENT_DONATION
        defaultTransactionShouldBeFound("eventDonation.greaterThan=" + SMALLER_EVENT_DONATION);
    }

    @Test
    @Transactional
    void getAllTransactionsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where notes equals to DEFAULT_NOTES
        defaultTransactionShouldBeFound("notes.equals=" + DEFAULT_NOTES);

        // Get all the transactionList where notes equals to UPDATED_NOTES
        defaultTransactionShouldNotBeFound("notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllTransactionsByNotesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where notes not equals to DEFAULT_NOTES
        defaultTransactionShouldNotBeFound("notes.notEquals=" + DEFAULT_NOTES);

        // Get all the transactionList where notes not equals to UPDATED_NOTES
        defaultTransactionShouldBeFound("notes.notEquals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllTransactionsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where notes in DEFAULT_NOTES or UPDATED_NOTES
        defaultTransactionShouldBeFound("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES);

        // Get all the transactionList where notes equals to UPDATED_NOTES
        defaultTransactionShouldNotBeFound("notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllTransactionsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where notes is not null
        defaultTransactionShouldBeFound("notes.specified=true");

        // Get all the transactionList where notes is null
        defaultTransactionShouldNotBeFound("notes.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionsByNotesContainsSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where notes contains DEFAULT_NOTES
        defaultTransactionShouldBeFound("notes.contains=" + DEFAULT_NOTES);

        // Get all the transactionList where notes contains UPDATED_NOTES
        defaultTransactionShouldNotBeFound("notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllTransactionsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        // Get all the transactionList where notes does not contain DEFAULT_NOTES
        defaultTransactionShouldNotBeFound("notes.doesNotContain=" + DEFAULT_NOTES);

        // Get all the transactionList where notes does not contain UPDATED_NOTES
        defaultTransactionShouldBeFound("notes.doesNotContain=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllTransactionsByTicketsIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);
        Ticket tickets = TicketResourceIT.createEntity(em);
        em.persist(tickets);
        em.flush();
        transaction.setTickets(tickets);
        transactionRepository.saveAndFlush(transaction);
        Long ticketsId = tickets.getId();

        // Get all the transactionList where tickets equals to ticketsId
        defaultTransactionShouldBeFound("ticketsId.equals=" + ticketsId);

        // Get all the transactionList where tickets equals to (ticketsId + 1)
        defaultTransactionShouldNotBeFound("ticketsId.equals=" + (ticketsId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionsByMembershipLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);
        MembershipLevel membershipLevel = MembershipLevelResourceIT.createEntity(em);
        em.persist(membershipLevel);
        em.flush();
        transaction.setMembershipLevel(membershipLevel);
        transactionRepository.saveAndFlush(transaction);
        Long membershipLevelId = membershipLevel.getId();

        // Get all the transactionList where membershipLevel equals to membershipLevelId
        defaultTransactionShouldBeFound("membershipLevelId.equals=" + membershipLevelId);

        // Get all the transactionList where membershipLevel equals to (membershipLevelId + 1)
        defaultTransactionShouldNotBeFound("membershipLevelId.equals=" + (membershipLevelId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionsByPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);
        Person person = PersonResourceIT.createEntity(em);
        em.persist(person);
        em.flush();
        transaction.setPerson(person);
        transactionRepository.saveAndFlush(transaction);
        Long personId = person.getId();

        // Get all the transactionList where person equals to personId
        defaultTransactionShouldBeFound("personId.equals=" + personId);

        // Get all the transactionList where person equals to (personId + 1)
        defaultTransactionShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionsByEventIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);
        Event event = EventResourceIT.createEntity(em);
        em.persist(event);
        em.flush();
        transaction.setEvent(event);
        transactionRepository.saveAndFlush(transaction);
        Long eventId = event.getId();

        // Get all the transactionList where event equals to eventId
        defaultTransactionShouldBeFound("eventId.equals=" + eventId);

        // Get all the transactionList where event equals to (eventId + 1)
        defaultTransactionShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionShouldBeFound(String filter) throws Exception {
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(DEFAULT_TOTAL_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].genericSubItemsPurchased").value(hasItem(DEFAULT_GENERIC_SUB_ITEMS_PURCHASED)))
            .andExpect(jsonPath("$.[*].costSubItemsPurchased").value(hasItem(DEFAULT_COST_SUB_ITEMS_PURCHASED.doubleValue())))
            .andExpect(jsonPath("$.[*].numberOfMemberships").value(hasItem(DEFAULT_NUMBER_OF_MEMBERSHIPS)))
            .andExpect(jsonPath("$.[*].donation").value(hasItem(DEFAULT_DONATION.doubleValue())))
            .andExpect(jsonPath("$.[*].eventDonation").value(hasItem(DEFAULT_EVENT_DONATION.doubleValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionShouldNotBeFound(String filter) throws Exception {
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransaction() throws Exception {
        // Get the transaction
        restTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction
        Transaction updatedTransaction = transactionRepository.findById(transaction.getId()).get();
        // Disconnect from session so that the updates on updatedTransaction are not directly saved in db
        em.detach(updatedTransaction);
        updatedTransaction
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .date(UPDATED_DATE)
            .genericSubItemsPurchased(UPDATED_GENERIC_SUB_ITEMS_PURCHASED)
            .costSubItemsPurchased(UPDATED_COST_SUB_ITEMS_PURCHASED)
            .numberOfMemberships(UPDATED_NUMBER_OF_MEMBERSHIPS)
            .donation(UPDATED_DONATION)
            .eventDonation(UPDATED_EVENT_DONATION)
            .notes(UPDATED_NOTES);

        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTransaction.getGenericSubItemsPurchased()).isEqualTo(UPDATED_GENERIC_SUB_ITEMS_PURCHASED);
        assertThat(testTransaction.getCostSubItemsPurchased()).isEqualTo(UPDATED_COST_SUB_ITEMS_PURCHASED);
        assertThat(testTransaction.getNumberOfMemberships()).isEqualTo(UPDATED_NUMBER_OF_MEMBERSHIPS);
        assertThat(testTransaction.getDonation()).isEqualTo(UPDATED_DONATION);
        assertThat(testTransaction.getEventDonation()).isEqualTo(UPDATED_EVENT_DONATION);
        assertThat(testTransaction.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void putNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transaction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transaction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction.date(UPDATED_DATE).notes(UPDATED_NOTES);

        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTotalAmount()).isEqualTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTransaction.getGenericSubItemsPurchased()).isEqualTo(DEFAULT_GENERIC_SUB_ITEMS_PURCHASED);
        assertThat(testTransaction.getCostSubItemsPurchased()).isEqualTo(DEFAULT_COST_SUB_ITEMS_PURCHASED);
        assertThat(testTransaction.getNumberOfMemberships()).isEqualTo(DEFAULT_NUMBER_OF_MEMBERSHIPS);
        assertThat(testTransaction.getDonation()).isEqualTo(DEFAULT_DONATION);
        assertThat(testTransaction.getEventDonation()).isEqualTo(DEFAULT_EVENT_DONATION);
        assertThat(testTransaction.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void fullUpdateTransactionWithPatch() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();

        // Update the transaction using partial update
        Transaction partialUpdatedTransaction = new Transaction();
        partialUpdatedTransaction.setId(transaction.getId());

        partialUpdatedTransaction
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .date(UPDATED_DATE)
            .genericSubItemsPurchased(UPDATED_GENERIC_SUB_ITEMS_PURCHASED)
            .costSubItemsPurchased(UPDATED_COST_SUB_ITEMS_PURCHASED)
            .numberOfMemberships(UPDATED_NUMBER_OF_MEMBERSHIPS)
            .donation(UPDATED_DONATION)
            .eventDonation(UPDATED_EVENT_DONATION)
            .notes(UPDATED_NOTES);

        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransaction))
            )
            .andExpect(status().isOk());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        assertThat(testTransaction.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testTransaction.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testTransaction.getGenericSubItemsPurchased()).isEqualTo(UPDATED_GENERIC_SUB_ITEMS_PURCHASED);
        assertThat(testTransaction.getCostSubItemsPurchased()).isEqualTo(UPDATED_COST_SUB_ITEMS_PURCHASED);
        assertThat(testTransaction.getNumberOfMemberships()).isEqualTo(UPDATED_NUMBER_OF_MEMBERSHIPS);
        assertThat(testTransaction.getDonation()).isEqualTo(UPDATED_DONATION);
        assertThat(testTransaction.getEventDonation()).isEqualTo(UPDATED_EVENT_DONATION);
        assertThat(testTransaction.getNotes()).isEqualTo(UPDATED_NOTES);
    }

    @Test
    @Transactional
    void patchNonExistingTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransaction() throws Exception {
        int databaseSizeBeforeUpdate = transactionRepository.findAll().size();
        transaction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(transaction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transaction in the database
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransaction() throws Exception {
        // Initialize the database
        transactionRepository.saveAndFlush(transaction);

        int databaseSizeBeforeDelete = transactionRepository.findAll().size();

        // Delete the transaction
        restTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, transaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transaction> transactionList = transactionRepository.findAll();
        assertThat(transactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
