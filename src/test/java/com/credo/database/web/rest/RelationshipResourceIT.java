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
import com.credo.database.domain.Relationship;
import com.credo.database.repository.RelationshipRepository;
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
 * Integration tests for the {@link RelationshipResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RelationshipResourceIT {

    private static final String DEFAULT_RELATIONSHIP = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/relationships";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRelationshipMockMvc;

    private Relationship relationship;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relationship createEntity(EntityManager em) {
        Relationship relationship = new Relationship().relationship(DEFAULT_RELATIONSHIP);
        return relationship;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Relationship createUpdatedEntity(EntityManager em) {
        Relationship relationship = new Relationship().relationship(UPDATED_RELATIONSHIP);
        return relationship;
    }

    @BeforeEach
    public void initTest() {
        relationship = createEntity(em);
    }

    @Test
    @Transactional
    void createRelationship() throws Exception {
        int databaseSizeBeforeCreate = relationshipRepository.findAll().size();
        // Create the Relationship
        restRelationshipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isCreated());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeCreate + 1);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getRelationship()).isEqualTo(DEFAULT_RELATIONSHIP);
    }

    @Test
    @Transactional
    void createRelationshipWithExistingId() throws Exception {
        // Create the Relationship with an existing ID
        relationship.setId(1L);

        int databaseSizeBeforeCreate = relationshipRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelationshipMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRelationships() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList
        restRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].relationship").value(hasItem(DEFAULT_RELATIONSHIP)));
    }

    @Test
    @Transactional
    void getRelationship() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get the relationship
        restRelationshipMockMvc
            .perform(get(ENTITY_API_URL_ID, relationship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(relationship.getId().intValue()))
            .andExpect(jsonPath("$.relationship").value(DEFAULT_RELATIONSHIP));
    }

    @Test
    @Transactional
    void getRelationshipsByIdFiltering() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        Long id = relationship.getId();

        defaultRelationshipShouldBeFound("id.equals=" + id);
        defaultRelationshipShouldNotBeFound("id.notEquals=" + id);

        defaultRelationshipShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRelationshipShouldNotBeFound("id.greaterThan=" + id);

        defaultRelationshipShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRelationshipShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRelationshipsByRelationshipIsEqualToSomething() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList where relationship equals to DEFAULT_RELATIONSHIP
        defaultRelationshipShouldBeFound("relationship.equals=" + DEFAULT_RELATIONSHIP);

        // Get all the relationshipList where relationship equals to UPDATED_RELATIONSHIP
        defaultRelationshipShouldNotBeFound("relationship.equals=" + UPDATED_RELATIONSHIP);
    }

    @Test
    @Transactional
    void getAllRelationshipsByRelationshipIsNotEqualToSomething() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList where relationship not equals to DEFAULT_RELATIONSHIP
        defaultRelationshipShouldNotBeFound("relationship.notEquals=" + DEFAULT_RELATIONSHIP);

        // Get all the relationshipList where relationship not equals to UPDATED_RELATIONSHIP
        defaultRelationshipShouldBeFound("relationship.notEquals=" + UPDATED_RELATIONSHIP);
    }

    @Test
    @Transactional
    void getAllRelationshipsByRelationshipIsInShouldWork() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList where relationship in DEFAULT_RELATIONSHIP or UPDATED_RELATIONSHIP
        defaultRelationshipShouldBeFound("relationship.in=" + DEFAULT_RELATIONSHIP + "," + UPDATED_RELATIONSHIP);

        // Get all the relationshipList where relationship equals to UPDATED_RELATIONSHIP
        defaultRelationshipShouldNotBeFound("relationship.in=" + UPDATED_RELATIONSHIP);
    }

    @Test
    @Transactional
    void getAllRelationshipsByRelationshipIsNullOrNotNull() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList where relationship is not null
        defaultRelationshipShouldBeFound("relationship.specified=true");

        // Get all the relationshipList where relationship is null
        defaultRelationshipShouldNotBeFound("relationship.specified=false");
    }

    @Test
    @Transactional
    void getAllRelationshipsByRelationshipContainsSomething() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList where relationship contains DEFAULT_RELATIONSHIP
        defaultRelationshipShouldBeFound("relationship.contains=" + DEFAULT_RELATIONSHIP);

        // Get all the relationshipList where relationship contains UPDATED_RELATIONSHIP
        defaultRelationshipShouldNotBeFound("relationship.contains=" + UPDATED_RELATIONSHIP);
    }

    @Test
    @Transactional
    void getAllRelationshipsByRelationshipNotContainsSomething() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        // Get all the relationshipList where relationship does not contain DEFAULT_RELATIONSHIP
        defaultRelationshipShouldNotBeFound("relationship.doesNotContain=" + DEFAULT_RELATIONSHIP);

        // Get all the relationshipList where relationship does not contain UPDATED_RELATIONSHIP
        defaultRelationshipShouldBeFound("relationship.doesNotContain=" + UPDATED_RELATIONSHIP);
    }

    @Test
    @Transactional
    void getAllRelationshipsByPeopleIsEqualToSomething() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);
        Person people = PersonResourceIT.createEntity(em);
        em.persist(people);
        em.flush();
        relationship.addPeople(people);
        relationshipRepository.saveAndFlush(relationship);
        Long peopleId = people.getId();

        // Get all the relationshipList where people equals to peopleId
        defaultRelationshipShouldBeFound("peopleId.equals=" + peopleId);

        // Get all the relationshipList where people equals to (peopleId + 1)
        defaultRelationshipShouldNotBeFound("peopleId.equals=" + (peopleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRelationshipShouldBeFound(String filter) throws Exception {
        restRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].relationship").value(hasItem(DEFAULT_RELATIONSHIP)));

        // Check, that the count call also returns 1
        restRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRelationshipShouldNotBeFound(String filter) throws Exception {
        restRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRelationshipMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRelationship() throws Exception {
        // Get the relationship
        restRelationshipMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRelationship() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();

        // Update the relationship
        Relationship updatedRelationship = relationshipRepository.findById(relationship.getId()).get();
        // Disconnect from session so that the updates on updatedRelationship are not directly saved in db
        em.detach(updatedRelationship);
        updatedRelationship.relationship(UPDATED_RELATIONSHIP);

        restRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRelationship.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRelationship))
            )
            .andExpect(status().isOk());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
    }

    @Test
    @Transactional
    void putNonExistingRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, relationship.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(relationship)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRelationshipWithPatch() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();

        // Update the relationship using partial update
        Relationship partialUpdatedRelationship = new Relationship();
        partialUpdatedRelationship.setId(relationship.getId());

        partialUpdatedRelationship.relationship(UPDATED_RELATIONSHIP);

        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelationship))
            )
            .andExpect(status().isOk());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
    }

    @Test
    @Transactional
    void fullUpdateRelationshipWithPatch() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();

        // Update the relationship using partial update
        Relationship partialUpdatedRelationship = new Relationship();
        partialUpdatedRelationship.setId(relationship.getId());

        partialUpdatedRelationship.relationship(UPDATED_RELATIONSHIP);

        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRelationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRelationship))
            )
            .andExpect(status().isOk());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
        Relationship testRelationship = relationshipList.get(relationshipList.size() - 1);
        assertThat(testRelationship.getRelationship()).isEqualTo(UPDATED_RELATIONSHIP);
    }

    @Test
    @Transactional
    void patchNonExistingRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, relationship.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isBadRequest());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRelationship() throws Exception {
        int databaseSizeBeforeUpdate = relationshipRepository.findAll().size();
        relationship.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRelationshipMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(relationship))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Relationship in the database
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRelationship() throws Exception {
        // Initialize the database
        relationshipRepository.saveAndFlush(relationship);

        int databaseSizeBeforeDelete = relationshipRepository.findAll().size();

        // Delete the relationship
        restRelationshipMockMvc
            .perform(delete(ENTITY_API_URL_ID, relationship.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Relationship> relationshipList = relationshipRepository.findAll();
        assertThat(relationshipList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
