package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.Event;
import com.credo.database.domain.NameTag;
import com.credo.database.domain.Person;
import com.credo.database.domain.Ticket;
import com.credo.database.domain.Transaction;
import com.credo.database.repository.TicketRepository;
import com.credo.database.service.criteria.TicketCriteria;
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
 * Integration tests for the {@link TicketResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TicketResourceIT {

    private static final Integer DEFAULT_COUNT = 0;
    private static final Integer UPDATED_COUNT = 1;
    private static final Integer SMALLER_COUNT = 0 - 1;

    private static final Double DEFAULT_COST_PER_TICKET = 0D;
    private static final Double UPDATED_COST_PER_TICKET = 1D;
    private static final Double SMALLER_COST_PER_TICKET = 0D - 1D;

    private static final Boolean DEFAULT_PICKED_UP = false;
    private static final Boolean UPDATED_PICKED_UP = true;

    private static final String ENTITY_API_URL = "/api/tickets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTicketMockMvc;

    private Ticket ticket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createEntity(EntityManager em) {
        Ticket ticket = new Ticket().count(DEFAULT_COUNT).costPerTicket(DEFAULT_COST_PER_TICKET).pickedUp(DEFAULT_PICKED_UP);
        return ticket;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createUpdatedEntity(EntityManager em) {
        Ticket ticket = new Ticket().count(UPDATED_COUNT).costPerTicket(UPDATED_COST_PER_TICKET).pickedUp(UPDATED_PICKED_UP);
        return ticket;
    }

    @BeforeEach
    public void initTest() {
        ticket = createEntity(em);
    }

    @Test
    @Transactional
    void createTicket() throws Exception {
        int databaseSizeBeforeCreate = ticketRepository.findAll().size();
        // Create the Ticket
        restTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticket)))
            .andExpect(status().isCreated());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeCreate + 1);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testTicket.getCostPerTicket()).isEqualTo(DEFAULT_COST_PER_TICKET);
        assertThat(testTicket.getPickedUp()).isEqualTo(DEFAULT_PICKED_UP);
    }

    @Test
    @Transactional
    void createTicketWithExistingId() throws Exception {
        // Create the Ticket with an existing ID
        ticket.setId(1L);

        int databaseSizeBeforeCreate = ticketRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTicketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticket)))
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTickets() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList
        restTicketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].costPerTicket").value(hasItem(DEFAULT_COST_PER_TICKET.doubleValue())))
            .andExpect(jsonPath("$.[*].pickedUp").value(hasItem(DEFAULT_PICKED_UP.booleanValue())));
    }

    @Test
    @Transactional
    void getTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get the ticket
        restTicketMockMvc
            .perform(get(ENTITY_API_URL_ID, ticket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ticket.getId().intValue()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT))
            .andExpect(jsonPath("$.costPerTicket").value(DEFAULT_COST_PER_TICKET.doubleValue()))
            .andExpect(jsonPath("$.pickedUp").value(DEFAULT_PICKED_UP.booleanValue()));
    }

    @Test
    @Transactional
    void getTicketsByIdFiltering() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        Long id = ticket.getId();

        defaultTicketShouldBeFound("id.equals=" + id);
        defaultTicketShouldNotBeFound("id.notEquals=" + id);

        defaultTicketShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTicketShouldNotBeFound("id.greaterThan=" + id);

        defaultTicketShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTicketShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTicketsByCountIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where count equals to DEFAULT_COUNT
        defaultTicketShouldBeFound("count.equals=" + DEFAULT_COUNT);

        // Get all the ticketList where count equals to UPDATED_COUNT
        defaultTicketShouldNotBeFound("count.equals=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllTicketsByCountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where count not equals to DEFAULT_COUNT
        defaultTicketShouldNotBeFound("count.notEquals=" + DEFAULT_COUNT);

        // Get all the ticketList where count not equals to UPDATED_COUNT
        defaultTicketShouldBeFound("count.notEquals=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllTicketsByCountIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where count in DEFAULT_COUNT or UPDATED_COUNT
        defaultTicketShouldBeFound("count.in=" + DEFAULT_COUNT + "," + UPDATED_COUNT);

        // Get all the ticketList where count equals to UPDATED_COUNT
        defaultTicketShouldNotBeFound("count.in=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllTicketsByCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where count is not null
        defaultTicketShouldBeFound("count.specified=true");

        // Get all the ticketList where count is null
        defaultTicketShouldNotBeFound("count.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where count is greater than or equal to DEFAULT_COUNT
        defaultTicketShouldBeFound("count.greaterThanOrEqual=" + DEFAULT_COUNT);

        // Get all the ticketList where count is greater than or equal to UPDATED_COUNT
        defaultTicketShouldNotBeFound("count.greaterThanOrEqual=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllTicketsByCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where count is less than or equal to DEFAULT_COUNT
        defaultTicketShouldBeFound("count.lessThanOrEqual=" + DEFAULT_COUNT);

        // Get all the ticketList where count is less than or equal to SMALLER_COUNT
        defaultTicketShouldNotBeFound("count.lessThanOrEqual=" + SMALLER_COUNT);
    }

    @Test
    @Transactional
    void getAllTicketsByCountIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where count is less than DEFAULT_COUNT
        defaultTicketShouldNotBeFound("count.lessThan=" + DEFAULT_COUNT);

        // Get all the ticketList where count is less than UPDATED_COUNT
        defaultTicketShouldBeFound("count.lessThan=" + UPDATED_COUNT);
    }

    @Test
    @Transactional
    void getAllTicketsByCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where count is greater than DEFAULT_COUNT
        defaultTicketShouldNotBeFound("count.greaterThan=" + DEFAULT_COUNT);

        // Get all the ticketList where count is greater than SMALLER_COUNT
        defaultTicketShouldBeFound("count.greaterThan=" + SMALLER_COUNT);
    }

    @Test
    @Transactional
    void getAllTicketsByCostPerTicketIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where costPerTicket equals to DEFAULT_COST_PER_TICKET
        defaultTicketShouldBeFound("costPerTicket.equals=" + DEFAULT_COST_PER_TICKET);

        // Get all the ticketList where costPerTicket equals to UPDATED_COST_PER_TICKET
        defaultTicketShouldNotBeFound("costPerTicket.equals=" + UPDATED_COST_PER_TICKET);
    }

    @Test
    @Transactional
    void getAllTicketsByCostPerTicketIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where costPerTicket not equals to DEFAULT_COST_PER_TICKET
        defaultTicketShouldNotBeFound("costPerTicket.notEquals=" + DEFAULT_COST_PER_TICKET);

        // Get all the ticketList where costPerTicket not equals to UPDATED_COST_PER_TICKET
        defaultTicketShouldBeFound("costPerTicket.notEquals=" + UPDATED_COST_PER_TICKET);
    }

    @Test
    @Transactional
    void getAllTicketsByCostPerTicketIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where costPerTicket in DEFAULT_COST_PER_TICKET or UPDATED_COST_PER_TICKET
        defaultTicketShouldBeFound("costPerTicket.in=" + DEFAULT_COST_PER_TICKET + "," + UPDATED_COST_PER_TICKET);

        // Get all the ticketList where costPerTicket equals to UPDATED_COST_PER_TICKET
        defaultTicketShouldNotBeFound("costPerTicket.in=" + UPDATED_COST_PER_TICKET);
    }

    @Test
    @Transactional
    void getAllTicketsByCostPerTicketIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where costPerTicket is not null
        defaultTicketShouldBeFound("costPerTicket.specified=true");

        // Get all the ticketList where costPerTicket is null
        defaultTicketShouldNotBeFound("costPerTicket.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByCostPerTicketIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where costPerTicket is greater than or equal to DEFAULT_COST_PER_TICKET
        defaultTicketShouldBeFound("costPerTicket.greaterThanOrEqual=" + DEFAULT_COST_PER_TICKET);

        // Get all the ticketList where costPerTicket is greater than or equal to UPDATED_COST_PER_TICKET
        defaultTicketShouldNotBeFound("costPerTicket.greaterThanOrEqual=" + UPDATED_COST_PER_TICKET);
    }

    @Test
    @Transactional
    void getAllTicketsByCostPerTicketIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where costPerTicket is less than or equal to DEFAULT_COST_PER_TICKET
        defaultTicketShouldBeFound("costPerTicket.lessThanOrEqual=" + DEFAULT_COST_PER_TICKET);

        // Get all the ticketList where costPerTicket is less than or equal to SMALLER_COST_PER_TICKET
        defaultTicketShouldNotBeFound("costPerTicket.lessThanOrEqual=" + SMALLER_COST_PER_TICKET);
    }

    @Test
    @Transactional
    void getAllTicketsByCostPerTicketIsLessThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where costPerTicket is less than DEFAULT_COST_PER_TICKET
        defaultTicketShouldNotBeFound("costPerTicket.lessThan=" + DEFAULT_COST_PER_TICKET);

        // Get all the ticketList where costPerTicket is less than UPDATED_COST_PER_TICKET
        defaultTicketShouldBeFound("costPerTicket.lessThan=" + UPDATED_COST_PER_TICKET);
    }

    @Test
    @Transactional
    void getAllTicketsByCostPerTicketIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where costPerTicket is greater than DEFAULT_COST_PER_TICKET
        defaultTicketShouldNotBeFound("costPerTicket.greaterThan=" + DEFAULT_COST_PER_TICKET);

        // Get all the ticketList where costPerTicket is greater than SMALLER_COST_PER_TICKET
        defaultTicketShouldBeFound("costPerTicket.greaterThan=" + SMALLER_COST_PER_TICKET);
    }

    @Test
    @Transactional
    void getAllTicketsByPickedUpIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where pickedUp equals to DEFAULT_PICKED_UP
        defaultTicketShouldBeFound("pickedUp.equals=" + DEFAULT_PICKED_UP);

        // Get all the ticketList where pickedUp equals to UPDATED_PICKED_UP
        defaultTicketShouldNotBeFound("pickedUp.equals=" + UPDATED_PICKED_UP);
    }

    @Test
    @Transactional
    void getAllTicketsByPickedUpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where pickedUp not equals to DEFAULT_PICKED_UP
        defaultTicketShouldNotBeFound("pickedUp.notEquals=" + DEFAULT_PICKED_UP);

        // Get all the ticketList where pickedUp not equals to UPDATED_PICKED_UP
        defaultTicketShouldBeFound("pickedUp.notEquals=" + UPDATED_PICKED_UP);
    }

    @Test
    @Transactional
    void getAllTicketsByPickedUpIsInShouldWork() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where pickedUp in DEFAULT_PICKED_UP or UPDATED_PICKED_UP
        defaultTicketShouldBeFound("pickedUp.in=" + DEFAULT_PICKED_UP + "," + UPDATED_PICKED_UP);

        // Get all the ticketList where pickedUp equals to UPDATED_PICKED_UP
        defaultTicketShouldNotBeFound("pickedUp.in=" + UPDATED_PICKED_UP);
    }

    @Test
    @Transactional
    void getAllTicketsByPickedUpIsNullOrNotNull() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        // Get all the ticketList where pickedUp is not null
        defaultTicketShouldBeFound("pickedUp.specified=true");

        // Get all the ticketList where pickedUp is null
        defaultTicketShouldNotBeFound("pickedUp.specified=false");
    }

    @Test
    @Transactional
    void getAllTicketsByPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        Person person = PersonResourceIT.createEntity(em);
        em.persist(person);
        em.flush();
        ticket.setPerson(person);
        ticketRepository.saveAndFlush(ticket);
        Long personId = person.getId();

        // Get all the ticketList where person equals to personId
        defaultTicketShouldBeFound("personId.equals=" + personId);

        // Get all the ticketList where person equals to (personId + 1)
        defaultTicketShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllTicketsByEventIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        Event event = EventResourceIT.createEntity(em);
        em.persist(event);
        em.flush();
        ticket.setEvent(event);
        ticketRepository.saveAndFlush(ticket);
        Long eventId = event.getId();

        // Get all the ticketList where event equals to eventId
        defaultTicketShouldBeFound("eventId.equals=" + eventId);

        // Get all the ticketList where event equals to (eventId + 1)
        defaultTicketShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    @Test
    @Transactional
    void getAllTicketsByTransactionIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        Transaction transaction = TransactionResourceIT.createEntity(em);
        em.persist(transaction);
        em.flush();
        ticket.setTransaction(transaction);
        transaction.setTickets(ticket);
        ticketRepository.saveAndFlush(ticket);
        Long transactionId = transaction.getId();

        // Get all the ticketList where transaction equals to transactionId
        defaultTicketShouldBeFound("transactionId.equals=" + transactionId);

        // Get all the ticketList where transaction equals to (transactionId + 1)
        defaultTicketShouldNotBeFound("transactionId.equals=" + (transactionId + 1));
    }

    @Test
    @Transactional
    void getAllTicketsByNameTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);
        NameTag nameTags = NameTagResourceIT.createEntity(em);
        em.persist(nameTags);
        em.flush();
        ticket.addNameTags(nameTags);
        ticketRepository.saveAndFlush(ticket);
        Long nameTagsId = nameTags.getId();

        // Get all the ticketList where nameTags equals to nameTagsId
        defaultTicketShouldBeFound("nameTagsId.equals=" + nameTagsId);

        // Get all the ticketList where nameTags equals to (nameTagsId + 1)
        defaultTicketShouldNotBeFound("nameTagsId.equals=" + (nameTagsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTicketShouldBeFound(String filter) throws Exception {
        restTicketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ticket.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT)))
            .andExpect(jsonPath("$.[*].costPerTicket").value(hasItem(DEFAULT_COST_PER_TICKET.doubleValue())))
            .andExpect(jsonPath("$.[*].pickedUp").value(hasItem(DEFAULT_PICKED_UP.booleanValue())));

        // Check, that the count call also returns 1
        restTicketMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTicketShouldNotBeFound(String filter) throws Exception {
        restTicketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTicketMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTicket() throws Exception {
        // Get the ticket
        restTicketMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Update the ticket
        Ticket updatedTicket = ticketRepository.findById(ticket.getId()).get();
        // Disconnect from session so that the updates on updatedTicket are not directly saved in db
        em.detach(updatedTicket);
        updatedTicket.count(UPDATED_COUNT).costPerTicket(UPDATED_COST_PER_TICKET).pickedUp(UPDATED_PICKED_UP);

        restTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTicket.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTicket))
            )
            .andExpect(status().isOk());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testTicket.getCostPerTicket()).isEqualTo(UPDATED_COST_PER_TICKET);
        assertThat(testTicket.getPickedUp()).isEqualTo(UPDATED_PICKED_UP);
    }

    @Test
    @Transactional
    void putNonExistingTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();
        ticket.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ticket.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticket))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();
        ticket.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ticket))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();
        ticket.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ticket)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTicketWithPatch() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Update the ticket using partial update
        Ticket partialUpdatedTicket = new Ticket();
        partialUpdatedTicket.setId(ticket.getId());

        partialUpdatedTicket.count(UPDATED_COUNT).pickedUp(UPDATED_PICKED_UP);

        restTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicket))
            )
            .andExpect(status().isOk());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testTicket.getCostPerTicket()).isEqualTo(DEFAULT_COST_PER_TICKET);
        assertThat(testTicket.getPickedUp()).isEqualTo(UPDATED_PICKED_UP);
    }

    @Test
    @Transactional
    void fullUpdateTicketWithPatch() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();

        // Update the ticket using partial update
        Ticket partialUpdatedTicket = new Ticket();
        partialUpdatedTicket.setId(ticket.getId());

        partialUpdatedTicket.count(UPDATED_COUNT).costPerTicket(UPDATED_COST_PER_TICKET).pickedUp(UPDATED_PICKED_UP);

        restTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTicket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTicket))
            )
            .andExpect(status().isOk());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        assertThat(testTicket.getCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testTicket.getCostPerTicket()).isEqualTo(UPDATED_COST_PER_TICKET);
        assertThat(testTicket.getPickedUp()).isEqualTo(UPDATED_PICKED_UP);
    }

    @Test
    @Transactional
    void patchNonExistingTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();
        ticket.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ticket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticket))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();
        ticket.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ticket))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTicket() throws Exception {
        int databaseSizeBeforeUpdate = ticketRepository.findAll().size();
        ticket.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTicketMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ticket)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ticket in the database
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTicket() throws Exception {
        // Initialize the database
        ticketRepository.saveAndFlush(ticket);

        int databaseSizeBeforeDelete = ticketRepository.findAll().size();

        // Delete the ticket
        restTicketMockMvc
            .perform(delete(ENTITY_API_URL_ID, ticket.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ticket> ticketList = ticketRepository.findAll();
        assertThat(ticketList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
