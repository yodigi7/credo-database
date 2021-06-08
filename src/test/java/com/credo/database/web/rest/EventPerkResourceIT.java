package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.Event;
import com.credo.database.domain.EventPerk;
import com.credo.database.domain.Person;
import com.credo.database.repository.EventPerkRepository;
import com.credo.database.service.criteria.EventPerkCriteria;
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
 * Integration tests for the {@link EventPerkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventPerkResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_MINIMUM_PRICE = 1D;
    private static final Double UPDATED_MINIMUM_PRICE = 2D;
    private static final Double SMALLER_MINIMUM_PRICE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/event-perks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventPerkRepository eventPerkRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventPerkMockMvc;

    private EventPerk eventPerk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventPerk createEntity(EntityManager em) {
        EventPerk eventPerk = new EventPerk().name(DEFAULT_NAME).minimumPrice(DEFAULT_MINIMUM_PRICE);
        return eventPerk;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventPerk createUpdatedEntity(EntityManager em) {
        EventPerk eventPerk = new EventPerk().name(UPDATED_NAME).minimumPrice(UPDATED_MINIMUM_PRICE);
        return eventPerk;
    }

    @BeforeEach
    public void initTest() {
        eventPerk = createEntity(em);
    }

    @Test
    @Transactional
    void createEventPerk() throws Exception {
        int databaseSizeBeforeCreate = eventPerkRepository.findAll().size();
        // Create the EventPerk
        restEventPerkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPerk)))
            .andExpect(status().isCreated());

        // Validate the EventPerk in the database
        List<EventPerk> eventPerkList = eventPerkRepository.findAll();
        assertThat(eventPerkList).hasSize(databaseSizeBeforeCreate + 1);
        EventPerk testEventPerk = eventPerkList.get(eventPerkList.size() - 1);
        assertThat(testEventPerk.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEventPerk.getMinimumPrice()).isEqualTo(DEFAULT_MINIMUM_PRICE);
    }

    @Test
    @Transactional
    void createEventPerkWithExistingId() throws Exception {
        // Create the EventPerk with an existing ID
        eventPerk.setId(1L);

        int databaseSizeBeforeCreate = eventPerkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventPerkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPerk)))
            .andExpect(status().isBadRequest());

        // Validate the EventPerk in the database
        List<EventPerk> eventPerkList = eventPerkRepository.findAll();
        assertThat(eventPerkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEventPerks() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList
        restEventPerkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventPerk.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].minimumPrice").value(hasItem(DEFAULT_MINIMUM_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getEventPerk() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get the eventPerk
        restEventPerkMockMvc
            .perform(get(ENTITY_API_URL_ID, eventPerk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventPerk.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.minimumPrice").value(DEFAULT_MINIMUM_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getEventPerksByIdFiltering() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        Long id = eventPerk.getId();

        defaultEventPerkShouldBeFound("id.equals=" + id);
        defaultEventPerkShouldNotBeFound("id.notEquals=" + id);

        defaultEventPerkShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventPerkShouldNotBeFound("id.greaterThan=" + id);

        defaultEventPerkShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventPerkShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventPerksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where name equals to DEFAULT_NAME
        defaultEventPerkShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the eventPerkList where name equals to UPDATED_NAME
        defaultEventPerkShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventPerksByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where name not equals to DEFAULT_NAME
        defaultEventPerkShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the eventPerkList where name not equals to UPDATED_NAME
        defaultEventPerkShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventPerksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEventPerkShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the eventPerkList where name equals to UPDATED_NAME
        defaultEventPerkShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventPerksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where name is not null
        defaultEventPerkShouldBeFound("name.specified=true");

        // Get all the eventPerkList where name is null
        defaultEventPerkShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPerksByNameContainsSomething() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where name contains DEFAULT_NAME
        defaultEventPerkShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the eventPerkList where name contains UPDATED_NAME
        defaultEventPerkShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventPerksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where name does not contain DEFAULT_NAME
        defaultEventPerkShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the eventPerkList where name does not contain UPDATED_NAME
        defaultEventPerkShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEventPerksByMinimumPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where minimumPrice equals to DEFAULT_MINIMUM_PRICE
        defaultEventPerkShouldBeFound("minimumPrice.equals=" + DEFAULT_MINIMUM_PRICE);

        // Get all the eventPerkList where minimumPrice equals to UPDATED_MINIMUM_PRICE
        defaultEventPerkShouldNotBeFound("minimumPrice.equals=" + UPDATED_MINIMUM_PRICE);
    }

    @Test
    @Transactional
    void getAllEventPerksByMinimumPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where minimumPrice not equals to DEFAULT_MINIMUM_PRICE
        defaultEventPerkShouldNotBeFound("minimumPrice.notEquals=" + DEFAULT_MINIMUM_PRICE);

        // Get all the eventPerkList where minimumPrice not equals to UPDATED_MINIMUM_PRICE
        defaultEventPerkShouldBeFound("minimumPrice.notEquals=" + UPDATED_MINIMUM_PRICE);
    }

    @Test
    @Transactional
    void getAllEventPerksByMinimumPriceIsInShouldWork() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where minimumPrice in DEFAULT_MINIMUM_PRICE or UPDATED_MINIMUM_PRICE
        defaultEventPerkShouldBeFound("minimumPrice.in=" + DEFAULT_MINIMUM_PRICE + "," + UPDATED_MINIMUM_PRICE);

        // Get all the eventPerkList where minimumPrice equals to UPDATED_MINIMUM_PRICE
        defaultEventPerkShouldNotBeFound("minimumPrice.in=" + UPDATED_MINIMUM_PRICE);
    }

    @Test
    @Transactional
    void getAllEventPerksByMinimumPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where minimumPrice is not null
        defaultEventPerkShouldBeFound("minimumPrice.specified=true");

        // Get all the eventPerkList where minimumPrice is null
        defaultEventPerkShouldNotBeFound("minimumPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllEventPerksByMinimumPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where minimumPrice is greater than or equal to DEFAULT_MINIMUM_PRICE
        defaultEventPerkShouldBeFound("minimumPrice.greaterThanOrEqual=" + DEFAULT_MINIMUM_PRICE);

        // Get all the eventPerkList where minimumPrice is greater than or equal to UPDATED_MINIMUM_PRICE
        defaultEventPerkShouldNotBeFound("minimumPrice.greaterThanOrEqual=" + UPDATED_MINIMUM_PRICE);
    }

    @Test
    @Transactional
    void getAllEventPerksByMinimumPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where minimumPrice is less than or equal to DEFAULT_MINIMUM_PRICE
        defaultEventPerkShouldBeFound("minimumPrice.lessThanOrEqual=" + DEFAULT_MINIMUM_PRICE);

        // Get all the eventPerkList where minimumPrice is less than or equal to SMALLER_MINIMUM_PRICE
        defaultEventPerkShouldNotBeFound("minimumPrice.lessThanOrEqual=" + SMALLER_MINIMUM_PRICE);
    }

    @Test
    @Transactional
    void getAllEventPerksByMinimumPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where minimumPrice is less than DEFAULT_MINIMUM_PRICE
        defaultEventPerkShouldNotBeFound("minimumPrice.lessThan=" + DEFAULT_MINIMUM_PRICE);

        // Get all the eventPerkList where minimumPrice is less than UPDATED_MINIMUM_PRICE
        defaultEventPerkShouldBeFound("minimumPrice.lessThan=" + UPDATED_MINIMUM_PRICE);
    }

    @Test
    @Transactional
    void getAllEventPerksByMinimumPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        // Get all the eventPerkList where minimumPrice is greater than DEFAULT_MINIMUM_PRICE
        defaultEventPerkShouldNotBeFound("minimumPrice.greaterThan=" + DEFAULT_MINIMUM_PRICE);

        // Get all the eventPerkList where minimumPrice is greater than SMALLER_MINIMUM_PRICE
        defaultEventPerkShouldBeFound("minimumPrice.greaterThan=" + SMALLER_MINIMUM_PRICE);
    }

    @Test
    @Transactional
    void getAllEventPerksByEventIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);
        Event event = EventResourceIT.createEntity(em);
        em.persist(event);
        em.flush();
        eventPerk.setEvent(event);
        eventPerkRepository.saveAndFlush(eventPerk);
        Long eventId = event.getId();

        // Get all the eventPerkList where event equals to eventId
        defaultEventPerkShouldBeFound("eventId.equals=" + eventId);

        // Get all the eventPerkList where event equals to (eventId + 1)
        defaultEventPerkShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    @Test
    @Transactional
    void getAllEventPerksByPersonIsEqualToSomething() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);
        Person person = PersonResourceIT.createEntity(em);
        em.persist(person);
        em.flush();
        eventPerk.setPerson(person);
        eventPerkRepository.saveAndFlush(eventPerk);
        Long personId = person.getId();

        // Get all the eventPerkList where person equals to personId
        defaultEventPerkShouldBeFound("personId.equals=" + personId);

        // Get all the eventPerkList where person equals to (personId + 1)
        defaultEventPerkShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventPerkShouldBeFound(String filter) throws Exception {
        restEventPerkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventPerk.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].minimumPrice").value(hasItem(DEFAULT_MINIMUM_PRICE.doubleValue())));

        // Check, that the count call also returns 1
        restEventPerkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventPerkShouldNotBeFound(String filter) throws Exception {
        restEventPerkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventPerkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEventPerk() throws Exception {
        // Get the eventPerk
        restEventPerkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEventPerk() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        int databaseSizeBeforeUpdate = eventPerkRepository.findAll().size();

        // Update the eventPerk
        EventPerk updatedEventPerk = eventPerkRepository.findById(eventPerk.getId()).get();
        // Disconnect from session so that the updates on updatedEventPerk are not directly saved in db
        em.detach(updatedEventPerk);
        updatedEventPerk.name(UPDATED_NAME).minimumPrice(UPDATED_MINIMUM_PRICE);

        restEventPerkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEventPerk.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEventPerk))
            )
            .andExpect(status().isOk());

        // Validate the EventPerk in the database
        List<EventPerk> eventPerkList = eventPerkRepository.findAll();
        assertThat(eventPerkList).hasSize(databaseSizeBeforeUpdate);
        EventPerk testEventPerk = eventPerkList.get(eventPerkList.size() - 1);
        assertThat(testEventPerk.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventPerk.getMinimumPrice()).isEqualTo(UPDATED_MINIMUM_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingEventPerk() throws Exception {
        int databaseSizeBeforeUpdate = eventPerkRepository.findAll().size();
        eventPerk.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventPerkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventPerk.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPerk))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPerk in the database
        List<EventPerk> eventPerkList = eventPerkRepository.findAll();
        assertThat(eventPerkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventPerk() throws Exception {
        int databaseSizeBeforeUpdate = eventPerkRepository.findAll().size();
        eventPerk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPerkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventPerk))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPerk in the database
        List<EventPerk> eventPerkList = eventPerkRepository.findAll();
        assertThat(eventPerkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventPerk() throws Exception {
        int databaseSizeBeforeUpdate = eventPerkRepository.findAll().size();
        eventPerk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPerkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventPerk)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventPerk in the database
        List<EventPerk> eventPerkList = eventPerkRepository.findAll();
        assertThat(eventPerkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventPerkWithPatch() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        int databaseSizeBeforeUpdate = eventPerkRepository.findAll().size();

        // Update the eventPerk using partial update
        EventPerk partialUpdatedEventPerk = new EventPerk();
        partialUpdatedEventPerk.setId(eventPerk.getId());

        partialUpdatedEventPerk.name(UPDATED_NAME);

        restEventPerkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventPerk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventPerk))
            )
            .andExpect(status().isOk());

        // Validate the EventPerk in the database
        List<EventPerk> eventPerkList = eventPerkRepository.findAll();
        assertThat(eventPerkList).hasSize(databaseSizeBeforeUpdate);
        EventPerk testEventPerk = eventPerkList.get(eventPerkList.size() - 1);
        assertThat(testEventPerk.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventPerk.getMinimumPrice()).isEqualTo(DEFAULT_MINIMUM_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateEventPerkWithPatch() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        int databaseSizeBeforeUpdate = eventPerkRepository.findAll().size();

        // Update the eventPerk using partial update
        EventPerk partialUpdatedEventPerk = new EventPerk();
        partialUpdatedEventPerk.setId(eventPerk.getId());

        partialUpdatedEventPerk.name(UPDATED_NAME).minimumPrice(UPDATED_MINIMUM_PRICE);

        restEventPerkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventPerk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventPerk))
            )
            .andExpect(status().isOk());

        // Validate the EventPerk in the database
        List<EventPerk> eventPerkList = eventPerkRepository.findAll();
        assertThat(eventPerkList).hasSize(databaseSizeBeforeUpdate);
        EventPerk testEventPerk = eventPerkList.get(eventPerkList.size() - 1);
        assertThat(testEventPerk.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEventPerk.getMinimumPrice()).isEqualTo(UPDATED_MINIMUM_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingEventPerk() throws Exception {
        int databaseSizeBeforeUpdate = eventPerkRepository.findAll().size();
        eventPerk.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventPerkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventPerk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventPerk))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPerk in the database
        List<EventPerk> eventPerkList = eventPerkRepository.findAll();
        assertThat(eventPerkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventPerk() throws Exception {
        int databaseSizeBeforeUpdate = eventPerkRepository.findAll().size();
        eventPerk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPerkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventPerk))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventPerk in the database
        List<EventPerk> eventPerkList = eventPerkRepository.findAll();
        assertThat(eventPerkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventPerk() throws Exception {
        int databaseSizeBeforeUpdate = eventPerkRepository.findAll().size();
        eventPerk.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventPerkMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventPerk))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventPerk in the database
        List<EventPerk> eventPerkList = eventPerkRepository.findAll();
        assertThat(eventPerkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventPerk() throws Exception {
        // Initialize the database
        eventPerkRepository.saveAndFlush(eventPerk);

        int databaseSizeBeforeDelete = eventPerkRepository.findAll().size();

        // Delete the eventPerk
        restEventPerkMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventPerk.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventPerk> eventPerkList = eventPerkRepository.findAll();
        assertThat(eventPerkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
