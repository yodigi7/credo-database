package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.Person;
import com.credo.database.domain.Ribbon;
import com.credo.database.repository.RibbonRepository;
import com.credo.database.service.criteria.RibbonCriteria;
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
 * Integration tests for the {@link RibbonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RibbonResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ribbons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RibbonRepository ribbonRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRibbonMockMvc;

    private Ribbon ribbon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ribbon createEntity(EntityManager em) {
        Ribbon ribbon = new Ribbon().name(DEFAULT_NAME);
        return ribbon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ribbon createUpdatedEntity(EntityManager em) {
        Ribbon ribbon = new Ribbon().name(UPDATED_NAME);
        return ribbon;
    }

    @BeforeEach
    public void initTest() {
        ribbon = createEntity(em);
    }

    @Test
    @Transactional
    void createRibbon() throws Exception {
        int databaseSizeBeforeCreate = ribbonRepository.findAll().size();
        // Create the Ribbon
        restRibbonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ribbon)))
            .andExpect(status().isCreated());

        // Validate the Ribbon in the database
        List<Ribbon> ribbonList = ribbonRepository.findAll();
        assertThat(ribbonList).hasSize(databaseSizeBeforeCreate + 1);
        Ribbon testRibbon = ribbonList.get(ribbonList.size() - 1);
        assertThat(testRibbon.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createRibbonWithExistingId() throws Exception {
        // Create the Ribbon with an existing ID
        ribbon.setId(1L);

        int databaseSizeBeforeCreate = ribbonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRibbonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ribbon)))
            .andExpect(status().isBadRequest());

        // Validate the Ribbon in the database
        List<Ribbon> ribbonList = ribbonRepository.findAll();
        assertThat(ribbonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRibbons() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        // Get all the ribbonList
        restRibbonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ribbon.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getRibbon() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        // Get the ribbon
        restRibbonMockMvc
            .perform(get(ENTITY_API_URL_ID, ribbon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ribbon.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getRibbonsByIdFiltering() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        Long id = ribbon.getId();

        defaultRibbonShouldBeFound("id.equals=" + id);
        defaultRibbonShouldNotBeFound("id.notEquals=" + id);

        defaultRibbonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRibbonShouldNotBeFound("id.greaterThan=" + id);

        defaultRibbonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRibbonShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRibbonsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        // Get all the ribbonList where name equals to DEFAULT_NAME
        defaultRibbonShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the ribbonList where name equals to UPDATED_NAME
        defaultRibbonShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRibbonsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        // Get all the ribbonList where name not equals to DEFAULT_NAME
        defaultRibbonShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the ribbonList where name not equals to UPDATED_NAME
        defaultRibbonShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRibbonsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        // Get all the ribbonList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRibbonShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the ribbonList where name equals to UPDATED_NAME
        defaultRibbonShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRibbonsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        // Get all the ribbonList where name is not null
        defaultRibbonShouldBeFound("name.specified=true");

        // Get all the ribbonList where name is null
        defaultRibbonShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllRibbonsByNameContainsSomething() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        // Get all the ribbonList where name contains DEFAULT_NAME
        defaultRibbonShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the ribbonList where name contains UPDATED_NAME
        defaultRibbonShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRibbonsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        // Get all the ribbonList where name does not contain DEFAULT_NAME
        defaultRibbonShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the ribbonList where name does not contain UPDATED_NAME
        defaultRibbonShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRibbonsByPeopleIsEqualToSomething() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);
        Person people = PersonResourceIT.createEntity(em);
        em.persist(people);
        em.flush();
        ribbon.addPeople(people);
        ribbonRepository.saveAndFlush(ribbon);
        Long peopleId = people.getId();

        // Get all the ribbonList where people equals to peopleId
        defaultRibbonShouldBeFound("peopleId.equals=" + peopleId);

        // Get all the ribbonList where people equals to (peopleId + 1)
        defaultRibbonShouldNotBeFound("peopleId.equals=" + (peopleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRibbonShouldBeFound(String filter) throws Exception {
        restRibbonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ribbon.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restRibbonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRibbonShouldNotBeFound(String filter) throws Exception {
        restRibbonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRibbonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRibbon() throws Exception {
        // Get the ribbon
        restRibbonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRibbon() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        int databaseSizeBeforeUpdate = ribbonRepository.findAll().size();

        // Update the ribbon
        Ribbon updatedRibbon = ribbonRepository.findById(ribbon.getId()).get();
        // Disconnect from session so that the updates on updatedRibbon are not directly saved in db
        em.detach(updatedRibbon);
        updatedRibbon.name(UPDATED_NAME);

        restRibbonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRibbon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRibbon))
            )
            .andExpect(status().isOk());

        // Validate the Ribbon in the database
        List<Ribbon> ribbonList = ribbonRepository.findAll();
        assertThat(ribbonList).hasSize(databaseSizeBeforeUpdate);
        Ribbon testRibbon = ribbonList.get(ribbonList.size() - 1);
        assertThat(testRibbon.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingRibbon() throws Exception {
        int databaseSizeBeforeUpdate = ribbonRepository.findAll().size();
        ribbon.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRibbonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ribbon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ribbon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ribbon in the database
        List<Ribbon> ribbonList = ribbonRepository.findAll();
        assertThat(ribbonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRibbon() throws Exception {
        int databaseSizeBeforeUpdate = ribbonRepository.findAll().size();
        ribbon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRibbonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ribbon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ribbon in the database
        List<Ribbon> ribbonList = ribbonRepository.findAll();
        assertThat(ribbonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRibbon() throws Exception {
        int databaseSizeBeforeUpdate = ribbonRepository.findAll().size();
        ribbon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRibbonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ribbon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ribbon in the database
        List<Ribbon> ribbonList = ribbonRepository.findAll();
        assertThat(ribbonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRibbonWithPatch() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        int databaseSizeBeforeUpdate = ribbonRepository.findAll().size();

        // Update the ribbon using partial update
        Ribbon partialUpdatedRibbon = new Ribbon();
        partialUpdatedRibbon.setId(ribbon.getId());

        partialUpdatedRibbon.name(UPDATED_NAME);

        restRibbonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRibbon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRibbon))
            )
            .andExpect(status().isOk());

        // Validate the Ribbon in the database
        List<Ribbon> ribbonList = ribbonRepository.findAll();
        assertThat(ribbonList).hasSize(databaseSizeBeforeUpdate);
        Ribbon testRibbon = ribbonList.get(ribbonList.size() - 1);
        assertThat(testRibbon.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateRibbonWithPatch() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        int databaseSizeBeforeUpdate = ribbonRepository.findAll().size();

        // Update the ribbon using partial update
        Ribbon partialUpdatedRibbon = new Ribbon();
        partialUpdatedRibbon.setId(ribbon.getId());

        partialUpdatedRibbon.name(UPDATED_NAME);

        restRibbonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRibbon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRibbon))
            )
            .andExpect(status().isOk());

        // Validate the Ribbon in the database
        List<Ribbon> ribbonList = ribbonRepository.findAll();
        assertThat(ribbonList).hasSize(databaseSizeBeforeUpdate);
        Ribbon testRibbon = ribbonList.get(ribbonList.size() - 1);
        assertThat(testRibbon.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingRibbon() throws Exception {
        int databaseSizeBeforeUpdate = ribbonRepository.findAll().size();
        ribbon.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRibbonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ribbon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ribbon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ribbon in the database
        List<Ribbon> ribbonList = ribbonRepository.findAll();
        assertThat(ribbonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRibbon() throws Exception {
        int databaseSizeBeforeUpdate = ribbonRepository.findAll().size();
        ribbon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRibbonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ribbon))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ribbon in the database
        List<Ribbon> ribbonList = ribbonRepository.findAll();
        assertThat(ribbonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRibbon() throws Exception {
        int databaseSizeBeforeUpdate = ribbonRepository.findAll().size();
        ribbon.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRibbonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ribbon)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ribbon in the database
        List<Ribbon> ribbonList = ribbonRepository.findAll();
        assertThat(ribbonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRibbon() throws Exception {
        // Initialize the database
        ribbonRepository.saveAndFlush(ribbon);

        int databaseSizeBeforeDelete = ribbonRepository.findAll().size();

        // Delete the ribbon
        restRibbonMockMvc
            .perform(delete(ENTITY_API_URL_ID, ribbon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ribbon> ribbonList = ribbonRepository.findAll();
        assertThat(ribbonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
