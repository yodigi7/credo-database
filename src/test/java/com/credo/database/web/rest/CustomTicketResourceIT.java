package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.Ticket;
import com.credo.database.domain.Transaction;
import com.credo.database.repository.TicketRepository;
import com.credo.database.repository.TransactionRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TicketResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomTicketResourceIT {

    private static final Integer DEFAULT_COUNT = 0;
    private static final Integer UPDATED_COUNT = 1;
    private static final Integer SMALLER_COUNT = 0 - 1;

    private static final Double DEFAULT_COST_PER_TICKET = 0D;
    private static final Double UPDATED_COST_PER_TICKET = 1D;
    private static final Double SMALLER_COST_PER_TICKET = 0D - 1D;

    private static final Boolean DEFAULT_PICKED_UP = false;
    private static final Boolean UPDATED_PICKED_UP = true;

    private static final String ENTITY_API_URL = "/api/tickets";
    private static final String TRANSACTION_ENTITY_API_URL = "/api/transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Ticket ticket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createEntity() {
        Ticket ticket = new Ticket().count(DEFAULT_COUNT).costPerTicket(DEFAULT_COST_PER_TICKET).pickedUp(DEFAULT_PICKED_UP);
        return ticket;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ticket createUpdatedEntity() {
        Ticket ticket = new Ticket().count(UPDATED_COUNT).costPerTicket(UPDATED_COST_PER_TICKET).pickedUp(UPDATED_PICKED_UP);
        return ticket;
    }

    @BeforeEach
    public void initTest() {
        ticket = createEntity();
    }

    @Test
    @Transactional
    void createTicket() throws Exception {
        Ticket ticket = ticketRepository.saveAndFlush(new Ticket());
        Transaction transaction = transactionRepository.saveAndFlush(new Transaction().tickets(ticket));
        // ticket = ticketRepository.saveAndFlush(ticket.transaction(transaction));

        List<Transaction> transactionList = transactionRepository.findAll();
        Transaction testTransaction = transactionList.get(transactionList.size() - 1);
        List<Ticket> ticketList = ticketRepository.findAll();
        Ticket testTicket = ticketList.get(ticketList.size() - 1);
        // Validate the Ticket in the database
        assertThat(testTransaction.getTickets()).isNotNull();
        // assertThat(testTicket.getTransaction()).isNotNull();
    }
}
