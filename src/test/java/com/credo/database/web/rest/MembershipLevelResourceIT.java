package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.MembershipLevel;
import com.credo.database.domain.Person;
import com.credo.database.repository.MembershipLevelRepository;
import com.credo.database.service.criteria.MembershipLevelCriteria;
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
 * Integration tests for the {@link MembershipLevelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MembershipLevelResourceIT {

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;
    private static final Double SMALLER_COST = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/membership-levels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MembershipLevelRepository membershipLevelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMembershipLevelMockMvc;

    private MembershipLevel membershipLevel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipLevel createEntity(EntityManager em) {
        MembershipLevel membershipLevel = new MembershipLevel().level(DEFAULT_LEVEL).cost(DEFAULT_COST);
        return membershipLevel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipLevel createUpdatedEntity(EntityManager em) {
        MembershipLevel membershipLevel = new MembershipLevel().level(UPDATED_LEVEL).cost(UPDATED_COST);
        return membershipLevel;
    }

    @BeforeEach
    public void initTest() {
        membershipLevel = createEntity(em);
    }

    @Test
    @Transactional
    void createMembershipLevel() throws Exception {
        int databaseSizeBeforeCreate = membershipLevelRepository.findAll().size();
        // Create the MembershipLevel
        restMembershipLevelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membershipLevel))
            )
            .andExpect(status().isCreated());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeCreate + 1);
        MembershipLevel testMembershipLevel = membershipLevelList.get(membershipLevelList.size() - 1);
        assertThat(testMembershipLevel.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testMembershipLevel.getCost()).isEqualTo(DEFAULT_COST);
    }

    @Test
    @Transactional
    void createMembershipLevelWithExistingId() throws Exception {
        // Create the MembershipLevel with an existing ID
        membershipLevel.setId(1L);

        int databaseSizeBeforeCreate = membershipLevelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembershipLevelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membershipLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = membershipLevelRepository.findAll().size();
        // set the field null
        membershipLevel.setLevel(null);

        // Create the MembershipLevel, which fails.

        restMembershipLevelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membershipLevel))
            )
            .andExpect(status().isBadRequest());

        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCostIsRequired() throws Exception {
        int databaseSizeBeforeTest = membershipLevelRepository.findAll().size();
        // set the field null
        membershipLevel.setCost(null);

        // Create the MembershipLevel, which fails.

        restMembershipLevelMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membershipLevel))
            )
            .andExpect(status().isBadRequest());

        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMembershipLevels() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList
        restMembershipLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())));
    }

    @Test
    @Transactional
    void getMembershipLevel() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get the membershipLevel
        restMembershipLevelMockMvc
            .perform(get(ENTITY_API_URL_ID, membershipLevel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(membershipLevel.getId().intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()));
    }

    @Test
    @Transactional
    void getMembershipLevelsByIdFiltering() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        Long id = membershipLevel.getId();

        defaultMembershipLevelShouldBeFound("id.equals=" + id);
        defaultMembershipLevelShouldNotBeFound("id.notEquals=" + id);

        defaultMembershipLevelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMembershipLevelShouldNotBeFound("id.greaterThan=" + id);

        defaultMembershipLevelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMembershipLevelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where level equals to DEFAULT_LEVEL
        defaultMembershipLevelShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the membershipLevelList where level equals to UPDATED_LEVEL
        defaultMembershipLevelShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where level not equals to DEFAULT_LEVEL
        defaultMembershipLevelShouldNotBeFound("level.notEquals=" + DEFAULT_LEVEL);

        // Get all the membershipLevelList where level not equals to UPDATED_LEVEL
        defaultMembershipLevelShouldBeFound("level.notEquals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultMembershipLevelShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the membershipLevelList where level equals to UPDATED_LEVEL
        defaultMembershipLevelShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where level is not null
        defaultMembershipLevelShouldBeFound("level.specified=true");

        // Get all the membershipLevelList where level is null
        defaultMembershipLevelShouldNotBeFound("level.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByLevelContainsSomething() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where level contains DEFAULT_LEVEL
        defaultMembershipLevelShouldBeFound("level.contains=" + DEFAULT_LEVEL);

        // Get all the membershipLevelList where level contains UPDATED_LEVEL
        defaultMembershipLevelShouldNotBeFound("level.contains=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByLevelNotContainsSomething() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where level does not contain DEFAULT_LEVEL
        defaultMembershipLevelShouldNotBeFound("level.doesNotContain=" + DEFAULT_LEVEL);

        // Get all the membershipLevelList where level does not contain UPDATED_LEVEL
        defaultMembershipLevelShouldBeFound("level.doesNotContain=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByCostIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where cost equals to DEFAULT_COST
        defaultMembershipLevelShouldBeFound("cost.equals=" + DEFAULT_COST);

        // Get all the membershipLevelList where cost equals to UPDATED_COST
        defaultMembershipLevelShouldNotBeFound("cost.equals=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByCostIsNotEqualToSomething() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where cost not equals to DEFAULT_COST
        defaultMembershipLevelShouldNotBeFound("cost.notEquals=" + DEFAULT_COST);

        // Get all the membershipLevelList where cost not equals to UPDATED_COST
        defaultMembershipLevelShouldBeFound("cost.notEquals=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByCostIsInShouldWork() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where cost in DEFAULT_COST or UPDATED_COST
        defaultMembershipLevelShouldBeFound("cost.in=" + DEFAULT_COST + "," + UPDATED_COST);

        // Get all the membershipLevelList where cost equals to UPDATED_COST
        defaultMembershipLevelShouldNotBeFound("cost.in=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where cost is not null
        defaultMembershipLevelShouldBeFound("cost.specified=true");

        // Get all the membershipLevelList where cost is null
        defaultMembershipLevelShouldNotBeFound("cost.specified=false");
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where cost is greater than or equal to DEFAULT_COST
        defaultMembershipLevelShouldBeFound("cost.greaterThanOrEqual=" + DEFAULT_COST);

        // Get all the membershipLevelList where cost is greater than or equal to UPDATED_COST
        defaultMembershipLevelShouldNotBeFound("cost.greaterThanOrEqual=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where cost is less than or equal to DEFAULT_COST
        defaultMembershipLevelShouldBeFound("cost.lessThanOrEqual=" + DEFAULT_COST);

        // Get all the membershipLevelList where cost is less than or equal to SMALLER_COST
        defaultMembershipLevelShouldNotBeFound("cost.lessThanOrEqual=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByCostIsLessThanSomething() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where cost is less than DEFAULT_COST
        defaultMembershipLevelShouldNotBeFound("cost.lessThan=" + DEFAULT_COST);

        // Get all the membershipLevelList where cost is less than UPDATED_COST
        defaultMembershipLevelShouldBeFound("cost.lessThan=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        // Get all the membershipLevelList where cost is greater than DEFAULT_COST
        defaultMembershipLevelShouldNotBeFound("cost.greaterThan=" + DEFAULT_COST);

        // Get all the membershipLevelList where cost is greater than SMALLER_COST
        defaultMembershipLevelShouldBeFound("cost.greaterThan=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllMembershipLevelsByPeopleIsEqualToSomething() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);
        Person people = PersonResourceIT.createEntity(em);
        em.persist(people);
        em.flush();
        membershipLevel.addPeople(people);
        membershipLevelRepository.saveAndFlush(membershipLevel);
        Long peopleId = people.getId();

        // Get all the membershipLevelList where people equals to peopleId
        defaultMembershipLevelShouldBeFound("peopleId.equals=" + peopleId);

        // Get all the membershipLevelList where people equals to (peopleId + 1)
        defaultMembershipLevelShouldNotBeFound("peopleId.equals=" + (peopleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMembershipLevelShouldBeFound(String filter) throws Exception {
        restMembershipLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipLevel.getId().intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())));

        // Check, that the count call also returns 1
        restMembershipLevelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMembershipLevelShouldNotBeFound(String filter) throws Exception {
        restMembershipLevelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMembershipLevelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMembershipLevel() throws Exception {
        // Get the membershipLevel
        restMembershipLevelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMembershipLevel() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();

        // Update the membershipLevel
        MembershipLevel updatedMembershipLevel = membershipLevelRepository.findById(membershipLevel.getId()).get();
        // Disconnect from session so that the updates on updatedMembershipLevel are not directly saved in db
        em.detach(updatedMembershipLevel);
        updatedMembershipLevel.level(UPDATED_LEVEL).cost(UPDATED_COST);

        restMembershipLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMembershipLevel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMembershipLevel))
            )
            .andExpect(status().isOk());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);
        MembershipLevel testMembershipLevel = membershipLevelList.get(membershipLevelList.size() - 1);
        assertThat(testMembershipLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testMembershipLevel.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void putNonExistingMembershipLevel() throws Exception {
        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();
        membershipLevel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, membershipLevel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMembershipLevel() throws Exception {
        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();
        membershipLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipLevelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(membershipLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMembershipLevel() throws Exception {
        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();
        membershipLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipLevelMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(membershipLevel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMembershipLevelWithPatch() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();

        // Update the membershipLevel using partial update
        MembershipLevel partialUpdatedMembershipLevel = new MembershipLevel();
        partialUpdatedMembershipLevel.setId(membershipLevel.getId());

        partialUpdatedMembershipLevel.level(UPDATED_LEVEL).cost(UPDATED_COST);

        restMembershipLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembershipLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipLevel))
            )
            .andExpect(status().isOk());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);
        MembershipLevel testMembershipLevel = membershipLevelList.get(membershipLevelList.size() - 1);
        assertThat(testMembershipLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testMembershipLevel.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void fullUpdateMembershipLevelWithPatch() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();

        // Update the membershipLevel using partial update
        MembershipLevel partialUpdatedMembershipLevel = new MembershipLevel();
        partialUpdatedMembershipLevel.setId(membershipLevel.getId());

        partialUpdatedMembershipLevel.level(UPDATED_LEVEL).cost(UPDATED_COST);

        restMembershipLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMembershipLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMembershipLevel))
            )
            .andExpect(status().isOk());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);
        MembershipLevel testMembershipLevel = membershipLevelList.get(membershipLevelList.size() - 1);
        assertThat(testMembershipLevel.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testMembershipLevel.getCost()).isEqualTo(UPDATED_COST);
    }

    @Test
    @Transactional
    void patchNonExistingMembershipLevel() throws Exception {
        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();
        membershipLevel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, membershipLevel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(membershipLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMembershipLevel() throws Exception {
        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();
        membershipLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipLevelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(membershipLevel))
            )
            .andExpect(status().isBadRequest());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMembershipLevel() throws Exception {
        int databaseSizeBeforeUpdate = membershipLevelRepository.findAll().size();
        membershipLevel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMembershipLevelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(membershipLevel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MembershipLevel in the database
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMembershipLevel() throws Exception {
        // Initialize the database
        membershipLevelRepository.saveAndFlush(membershipLevel);

        int databaseSizeBeforeDelete = membershipLevelRepository.findAll().size();

        // Delete the membershipLevel
        restMembershipLevelMockMvc
            .perform(delete(ENTITY_API_URL_ID, membershipLevel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MembershipLevel> membershipLevelList = membershipLevelRepository.findAll();
        assertThat(membershipLevelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
