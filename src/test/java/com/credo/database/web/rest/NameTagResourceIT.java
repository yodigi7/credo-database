package com.credo.database.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.credo.database.IntegrationTest;
import com.credo.database.domain.NameTag;
import com.credo.database.domain.Ticket;
import com.credo.database.repository.NameTagRepository;
import com.credo.database.service.criteria.NameTagCriteria;
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
 * Integration tests for the {@link NameTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NameTagResourceIT {

    private static final String DEFAULT_NAME_TAG = "AAAAAAAAAA";
    private static final String UPDATED_NAME_TAG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/name-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NameTagRepository nameTagRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNameTagMockMvc;

    private NameTag nameTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NameTag createEntity(EntityManager em) {
        NameTag nameTag = new NameTag().nameTag(DEFAULT_NAME_TAG);
        return nameTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NameTag createUpdatedEntity(EntityManager em) {
        NameTag nameTag = new NameTag().nameTag(UPDATED_NAME_TAG);
        return nameTag;
    }

    @BeforeEach
    public void initTest() {
        nameTag = createEntity(em);
    }

    @Test
    @Transactional
    void createNameTag() throws Exception {
        int databaseSizeBeforeCreate = nameTagRepository.findAll().size();
        // Create the NameTag
        restNameTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nameTag)))
            .andExpect(status().isCreated());

        // Validate the NameTag in the database
        List<NameTag> nameTagList = nameTagRepository.findAll();
        assertThat(nameTagList).hasSize(databaseSizeBeforeCreate + 1);
        NameTag testNameTag = nameTagList.get(nameTagList.size() - 1);
        assertThat(testNameTag.getNameTag()).isEqualTo(DEFAULT_NAME_TAG);
    }

    @Test
    @Transactional
    void createNameTagWithExistingId() throws Exception {
        // Create the NameTag with an existing ID
        nameTag.setId(1L);

        int databaseSizeBeforeCreate = nameTagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNameTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nameTag)))
            .andExpect(status().isBadRequest());

        // Validate the NameTag in the database
        List<NameTag> nameTagList = nameTagRepository.findAll();
        assertThat(nameTagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNameTags() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        // Get all the nameTagList
        restNameTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nameTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameTag").value(hasItem(DEFAULT_NAME_TAG)));
    }

    @Test
    @Transactional
    void getNameTag() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        // Get the nameTag
        restNameTagMockMvc
            .perform(get(ENTITY_API_URL_ID, nameTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nameTag.getId().intValue()))
            .andExpect(jsonPath("$.nameTag").value(DEFAULT_NAME_TAG));
    }

    @Test
    @Transactional
    void getNameTagsByIdFiltering() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        Long id = nameTag.getId();

        defaultNameTagShouldBeFound("id.equals=" + id);
        defaultNameTagShouldNotBeFound("id.notEquals=" + id);

        defaultNameTagShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNameTagShouldNotBeFound("id.greaterThan=" + id);

        defaultNameTagShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNameTagShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNameTagsByNameTagIsEqualToSomething() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        // Get all the nameTagList where nameTag equals to DEFAULT_NAME_TAG
        defaultNameTagShouldBeFound("nameTag.equals=" + DEFAULT_NAME_TAG);

        // Get all the nameTagList where nameTag equals to UPDATED_NAME_TAG
        defaultNameTagShouldNotBeFound("nameTag.equals=" + UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void getAllNameTagsByNameTagIsNotEqualToSomething() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        // Get all the nameTagList where nameTag not equals to DEFAULT_NAME_TAG
        defaultNameTagShouldNotBeFound("nameTag.notEquals=" + DEFAULT_NAME_TAG);

        // Get all the nameTagList where nameTag not equals to UPDATED_NAME_TAG
        defaultNameTagShouldBeFound("nameTag.notEquals=" + UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void getAllNameTagsByNameTagIsInShouldWork() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        // Get all the nameTagList where nameTag in DEFAULT_NAME_TAG or UPDATED_NAME_TAG
        defaultNameTagShouldBeFound("nameTag.in=" + DEFAULT_NAME_TAG + "," + UPDATED_NAME_TAG);

        // Get all the nameTagList where nameTag equals to UPDATED_NAME_TAG
        defaultNameTagShouldNotBeFound("nameTag.in=" + UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void getAllNameTagsByNameTagIsNullOrNotNull() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        // Get all the nameTagList where nameTag is not null
        defaultNameTagShouldBeFound("nameTag.specified=true");

        // Get all the nameTagList where nameTag is null
        defaultNameTagShouldNotBeFound("nameTag.specified=false");
    }

    @Test
    @Transactional
    void getAllNameTagsByNameTagContainsSomething() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        // Get all the nameTagList where nameTag contains DEFAULT_NAME_TAG
        defaultNameTagShouldBeFound("nameTag.contains=" + DEFAULT_NAME_TAG);

        // Get all the nameTagList where nameTag contains UPDATED_NAME_TAG
        defaultNameTagShouldNotBeFound("nameTag.contains=" + UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void getAllNameTagsByNameTagNotContainsSomething() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        // Get all the nameTagList where nameTag does not contain DEFAULT_NAME_TAG
        defaultNameTagShouldNotBeFound("nameTag.doesNotContain=" + DEFAULT_NAME_TAG);

        // Get all the nameTagList where nameTag does not contain UPDATED_NAME_TAG
        defaultNameTagShouldBeFound("nameTag.doesNotContain=" + UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void getAllNameTagsByTicketIsEqualToSomething() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);
        Ticket ticket = TicketResourceIT.createEntity(em);
        em.persist(ticket);
        em.flush();
        nameTag.setTicket(ticket);
        nameTagRepository.saveAndFlush(nameTag);
        Long ticketId = ticket.getId();

        // Get all the nameTagList where ticket equals to ticketId
        defaultNameTagShouldBeFound("ticketId.equals=" + ticketId);

        // Get all the nameTagList where ticket equals to (ticketId + 1)
        defaultNameTagShouldNotBeFound("ticketId.equals=" + (ticketId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNameTagShouldBeFound(String filter) throws Exception {
        restNameTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nameTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameTag").value(hasItem(DEFAULT_NAME_TAG)));

        // Check, that the count call also returns 1
        restNameTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNameTagShouldNotBeFound(String filter) throws Exception {
        restNameTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNameTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNameTag() throws Exception {
        // Get the nameTag
        restNameTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNameTag() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        int databaseSizeBeforeUpdate = nameTagRepository.findAll().size();

        // Update the nameTag
        NameTag updatedNameTag = nameTagRepository.findById(nameTag.getId()).get();
        // Disconnect from session so that the updates on updatedNameTag are not directly saved in db
        em.detach(updatedNameTag);
        updatedNameTag.nameTag(UPDATED_NAME_TAG);

        restNameTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNameTag.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNameTag))
            )
            .andExpect(status().isOk());

        // Validate the NameTag in the database
        List<NameTag> nameTagList = nameTagRepository.findAll();
        assertThat(nameTagList).hasSize(databaseSizeBeforeUpdate);
        NameTag testNameTag = nameTagList.get(nameTagList.size() - 1);
        assertThat(testNameTag.getNameTag()).isEqualTo(UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void putNonExistingNameTag() throws Exception {
        int databaseSizeBeforeUpdate = nameTagRepository.findAll().size();
        nameTag.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNameTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nameTag.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nameTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the NameTag in the database
        List<NameTag> nameTagList = nameTagRepository.findAll();
        assertThat(nameTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNameTag() throws Exception {
        int databaseSizeBeforeUpdate = nameTagRepository.findAll().size();
        nameTag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNameTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nameTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the NameTag in the database
        List<NameTag> nameTagList = nameTagRepository.findAll();
        assertThat(nameTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNameTag() throws Exception {
        int databaseSizeBeforeUpdate = nameTagRepository.findAll().size();
        nameTag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNameTagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nameTag)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NameTag in the database
        List<NameTag> nameTagList = nameTagRepository.findAll();
        assertThat(nameTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNameTagWithPatch() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        int databaseSizeBeforeUpdate = nameTagRepository.findAll().size();

        // Update the nameTag using partial update
        NameTag partialUpdatedNameTag = new NameTag();
        partialUpdatedNameTag.setId(nameTag.getId());

        partialUpdatedNameTag.nameTag(UPDATED_NAME_TAG);

        restNameTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNameTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNameTag))
            )
            .andExpect(status().isOk());

        // Validate the NameTag in the database
        List<NameTag> nameTagList = nameTagRepository.findAll();
        assertThat(nameTagList).hasSize(databaseSizeBeforeUpdate);
        NameTag testNameTag = nameTagList.get(nameTagList.size() - 1);
        assertThat(testNameTag.getNameTag()).isEqualTo(UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void fullUpdateNameTagWithPatch() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        int databaseSizeBeforeUpdate = nameTagRepository.findAll().size();

        // Update the nameTag using partial update
        NameTag partialUpdatedNameTag = new NameTag();
        partialUpdatedNameTag.setId(nameTag.getId());

        partialUpdatedNameTag.nameTag(UPDATED_NAME_TAG);

        restNameTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNameTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNameTag))
            )
            .andExpect(status().isOk());

        // Validate the NameTag in the database
        List<NameTag> nameTagList = nameTagRepository.findAll();
        assertThat(nameTagList).hasSize(databaseSizeBeforeUpdate);
        NameTag testNameTag = nameTagList.get(nameTagList.size() - 1);
        assertThat(testNameTag.getNameTag()).isEqualTo(UPDATED_NAME_TAG);
    }

    @Test
    @Transactional
    void patchNonExistingNameTag() throws Exception {
        int databaseSizeBeforeUpdate = nameTagRepository.findAll().size();
        nameTag.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNameTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nameTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nameTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the NameTag in the database
        List<NameTag> nameTagList = nameTagRepository.findAll();
        assertThat(nameTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNameTag() throws Exception {
        int databaseSizeBeforeUpdate = nameTagRepository.findAll().size();
        nameTag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNameTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nameTag))
            )
            .andExpect(status().isBadRequest());

        // Validate the NameTag in the database
        List<NameTag> nameTagList = nameTagRepository.findAll();
        assertThat(nameTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNameTag() throws Exception {
        int databaseSizeBeforeUpdate = nameTagRepository.findAll().size();
        nameTag.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNameTagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nameTag)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NameTag in the database
        List<NameTag> nameTagList = nameTagRepository.findAll();
        assertThat(nameTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNameTag() throws Exception {
        // Initialize the database
        nameTagRepository.saveAndFlush(nameTag);

        int databaseSizeBeforeDelete = nameTagRepository.findAll().size();

        // Delete the nameTag
        restNameTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, nameTag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NameTag> nameTagList = nameTagRepository.findAll();
        assertThat(nameTagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
