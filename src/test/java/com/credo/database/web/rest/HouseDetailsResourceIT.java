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
import com.credo.database.domain.HouseAddress;
import com.credo.database.domain.HouseDetails;
import com.credo.database.domain.Person;
import com.credo.database.repository.HouseDetailsRepository;
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
 * Integration tests for the {@link HouseDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HouseDetailsResourceIT {

    private static final String DEFAULT_MAILING_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_MAILING_LABEL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/house-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HouseDetailsRepository houseDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHouseDetailsMockMvc;

    private HouseDetails houseDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HouseDetails createEntity(EntityManager em) {
        HouseDetails houseDetails = new HouseDetails().mailingLabel(DEFAULT_MAILING_LABEL);
        return houseDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HouseDetails createUpdatedEntity(EntityManager em) {
        HouseDetails houseDetails = new HouseDetails().mailingLabel(UPDATED_MAILING_LABEL);
        return houseDetails;
    }

    @BeforeEach
    public void initTest() {
        houseDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createHouseDetails() throws Exception {
        int databaseSizeBeforeCreate = houseDetailsRepository.findAll().size();
        // Create the HouseDetails
        restHouseDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(houseDetails)))
            .andExpect(status().isCreated());

        // Validate the HouseDetails in the database
        List<HouseDetails> houseDetailsList = houseDetailsRepository.findAll();
        assertThat(houseDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        HouseDetails testHouseDetails = houseDetailsList.get(houseDetailsList.size() - 1);
        assertThat(testHouseDetails.getMailingLabel()).isEqualTo(DEFAULT_MAILING_LABEL);
    }

    @Test
    @Transactional
    void createHouseDetailsWithExistingId() throws Exception {
        // Create the HouseDetails with an existing ID
        houseDetails.setId(1L);

        int databaseSizeBeforeCreate = houseDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHouseDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(houseDetails)))
            .andExpect(status().isBadRequest());

        // Validate the HouseDetails in the database
        List<HouseDetails> houseDetailsList = houseDetailsRepository.findAll();
        assertThat(houseDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHouseDetails() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        // Get all the houseDetailsList
        restHouseDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(houseDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].mailingLabel").value(hasItem(DEFAULT_MAILING_LABEL)));
    }

    @Test
    @Transactional
    void getHouseDetails() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        // Get the houseDetails
        restHouseDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, houseDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(houseDetails.getId().intValue()))
            .andExpect(jsonPath("$.mailingLabel").value(DEFAULT_MAILING_LABEL));
    }

    @Test
    @Transactional
    void getHouseDetailsByIdFiltering() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        Long id = houseDetails.getId();

        defaultHouseDetailsShouldBeFound("id.equals=" + id);
        defaultHouseDetailsShouldNotBeFound("id.notEquals=" + id);

        defaultHouseDetailsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHouseDetailsShouldNotBeFound("id.greaterThan=" + id);

        defaultHouseDetailsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHouseDetailsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHouseDetailsByMailingLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        // Get all the houseDetailsList where mailingLabel equals to DEFAULT_MAILING_LABEL
        defaultHouseDetailsShouldBeFound("mailingLabel.equals=" + DEFAULT_MAILING_LABEL);

        // Get all the houseDetailsList where mailingLabel equals to UPDATED_MAILING_LABEL
        defaultHouseDetailsShouldNotBeFound("mailingLabel.equals=" + UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void getAllHouseDetailsByMailingLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        // Get all the houseDetailsList where mailingLabel not equals to DEFAULT_MAILING_LABEL
        defaultHouseDetailsShouldNotBeFound("mailingLabel.notEquals=" + DEFAULT_MAILING_LABEL);

        // Get all the houseDetailsList where mailingLabel not equals to UPDATED_MAILING_LABEL
        defaultHouseDetailsShouldBeFound("mailingLabel.notEquals=" + UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void getAllHouseDetailsByMailingLabelIsInShouldWork() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        // Get all the houseDetailsList where mailingLabel in DEFAULT_MAILING_LABEL or UPDATED_MAILING_LABEL
        defaultHouseDetailsShouldBeFound("mailingLabel.in=" + DEFAULT_MAILING_LABEL + "," + UPDATED_MAILING_LABEL);

        // Get all the houseDetailsList where mailingLabel equals to UPDATED_MAILING_LABEL
        defaultHouseDetailsShouldNotBeFound("mailingLabel.in=" + UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void getAllHouseDetailsByMailingLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        // Get all the houseDetailsList where mailingLabel is not null
        defaultHouseDetailsShouldBeFound("mailingLabel.specified=true");

        // Get all the houseDetailsList where mailingLabel is null
        defaultHouseDetailsShouldNotBeFound("mailingLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllHouseDetailsByMailingLabelContainsSomething() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        // Get all the houseDetailsList where mailingLabel contains DEFAULT_MAILING_LABEL
        defaultHouseDetailsShouldBeFound("mailingLabel.contains=" + DEFAULT_MAILING_LABEL);

        // Get all the houseDetailsList where mailingLabel contains UPDATED_MAILING_LABEL
        defaultHouseDetailsShouldNotBeFound("mailingLabel.contains=" + UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void getAllHouseDetailsByMailingLabelNotContainsSomething() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        // Get all the houseDetailsList where mailingLabel does not contain DEFAULT_MAILING_LABEL
        defaultHouseDetailsShouldNotBeFound("mailingLabel.doesNotContain=" + DEFAULT_MAILING_LABEL);

        // Get all the houseDetailsList where mailingLabel does not contain UPDATED_MAILING_LABEL
        defaultHouseDetailsShouldBeFound("mailingLabel.doesNotContain=" + UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void getAllHouseDetailsByHeadOfHouseIsEqualToSomething() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);
        Person headOfHouse = PersonResourceIT.createEntity(em);
        em.persist(headOfHouse);
        em.flush();
        houseDetails.setHeadOfHouse(headOfHouse);
        houseDetailsRepository.saveAndFlush(houseDetails);
        Long headOfHouseId = headOfHouse.getId();

        // Get all the houseDetailsList where headOfHouse equals to headOfHouseId
        defaultHouseDetailsShouldBeFound("headOfHouseId.equals=" + headOfHouseId);

        // Get all the houseDetailsList where headOfHouse equals to (headOfHouseId + 1)
        defaultHouseDetailsShouldNotBeFound("headOfHouseId.equals=" + (headOfHouseId + 1));
    }

    @Test
    @Transactional
    void getAllHouseDetailsByAddressesIsEqualToSomething() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);
        HouseAddress addresses = HouseAddressResourceIT.createEntity(em);
        em.persist(addresses);
        em.flush();
        houseDetails.addAddresses(addresses);
        houseDetailsRepository.saveAndFlush(houseDetails);
        Long addressesId = addresses.getId();

        // Get all the houseDetailsList where addresses equals to addressesId
        defaultHouseDetailsShouldBeFound("addressesId.equals=" + addressesId);

        // Get all the houseDetailsList where addresses equals to (addressesId + 1)
        defaultHouseDetailsShouldNotBeFound("addressesId.equals=" + (addressesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHouseDetailsShouldBeFound(String filter) throws Exception {
        restHouseDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(houseDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].mailingLabel").value(hasItem(DEFAULT_MAILING_LABEL)));

        // Check, that the count call also returns 1
        restHouseDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHouseDetailsShouldNotBeFound(String filter) throws Exception {
        restHouseDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHouseDetailsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHouseDetails() throws Exception {
        // Get the houseDetails
        restHouseDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewHouseDetails() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        int databaseSizeBeforeUpdate = houseDetailsRepository.findAll().size();

        // Update the houseDetails
        HouseDetails updatedHouseDetails = houseDetailsRepository.findById(houseDetails.getId()).get();
        // Disconnect from session so that the updates on updatedHouseDetails are not directly saved in db
        em.detach(updatedHouseDetails);
        updatedHouseDetails.mailingLabel(UPDATED_MAILING_LABEL);

        restHouseDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHouseDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHouseDetails))
            )
            .andExpect(status().isOk());

        // Validate the HouseDetails in the database
        List<HouseDetails> houseDetailsList = houseDetailsRepository.findAll();
        assertThat(houseDetailsList).hasSize(databaseSizeBeforeUpdate);
        HouseDetails testHouseDetails = houseDetailsList.get(houseDetailsList.size() - 1);
        assertThat(testHouseDetails.getMailingLabel()).isEqualTo(UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void putNonExistingHouseDetails() throws Exception {
        int databaseSizeBeforeUpdate = houseDetailsRepository.findAll().size();
        houseDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHouseDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, houseDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(houseDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseDetails in the database
        List<HouseDetails> houseDetailsList = houseDetailsRepository.findAll();
        assertThat(houseDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHouseDetails() throws Exception {
        int databaseSizeBeforeUpdate = houseDetailsRepository.findAll().size();
        houseDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(houseDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseDetails in the database
        List<HouseDetails> houseDetailsList = houseDetailsRepository.findAll();
        assertThat(houseDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHouseDetails() throws Exception {
        int databaseSizeBeforeUpdate = houseDetailsRepository.findAll().size();
        houseDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(houseDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HouseDetails in the database
        List<HouseDetails> houseDetailsList = houseDetailsRepository.findAll();
        assertThat(houseDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHouseDetailsWithPatch() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        int databaseSizeBeforeUpdate = houseDetailsRepository.findAll().size();

        // Update the houseDetails using partial update
        HouseDetails partialUpdatedHouseDetails = new HouseDetails();
        partialUpdatedHouseDetails.setId(houseDetails.getId());

        restHouseDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHouseDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHouseDetails))
            )
            .andExpect(status().isOk());

        // Validate the HouseDetails in the database
        List<HouseDetails> houseDetailsList = houseDetailsRepository.findAll();
        assertThat(houseDetailsList).hasSize(databaseSizeBeforeUpdate);
        HouseDetails testHouseDetails = houseDetailsList.get(houseDetailsList.size() - 1);
        assertThat(testHouseDetails.getMailingLabel()).isEqualTo(DEFAULT_MAILING_LABEL);
    }

    @Test
    @Transactional
    void fullUpdateHouseDetailsWithPatch() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        int databaseSizeBeforeUpdate = houseDetailsRepository.findAll().size();

        // Update the houseDetails using partial update
        HouseDetails partialUpdatedHouseDetails = new HouseDetails();
        partialUpdatedHouseDetails.setId(houseDetails.getId());

        partialUpdatedHouseDetails.mailingLabel(UPDATED_MAILING_LABEL);

        restHouseDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHouseDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHouseDetails))
            )
            .andExpect(status().isOk());

        // Validate the HouseDetails in the database
        List<HouseDetails> houseDetailsList = houseDetailsRepository.findAll();
        assertThat(houseDetailsList).hasSize(databaseSizeBeforeUpdate);
        HouseDetails testHouseDetails = houseDetailsList.get(houseDetailsList.size() - 1);
        assertThat(testHouseDetails.getMailingLabel()).isEqualTo(UPDATED_MAILING_LABEL);
    }

    @Test
    @Transactional
    void patchNonExistingHouseDetails() throws Exception {
        int databaseSizeBeforeUpdate = houseDetailsRepository.findAll().size();
        houseDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHouseDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, houseDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(houseDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseDetails in the database
        List<HouseDetails> houseDetailsList = houseDetailsRepository.findAll();
        assertThat(houseDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHouseDetails() throws Exception {
        int databaseSizeBeforeUpdate = houseDetailsRepository.findAll().size();
        houseDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(houseDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the HouseDetails in the database
        List<HouseDetails> houseDetailsList = houseDetailsRepository.findAll();
        assertThat(houseDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHouseDetails() throws Exception {
        int databaseSizeBeforeUpdate = houseDetailsRepository.findAll().size();
        houseDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHouseDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(houseDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HouseDetails in the database
        List<HouseDetails> houseDetailsList = houseDetailsRepository.findAll();
        assertThat(houseDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHouseDetails() throws Exception {
        // Initialize the database
        houseDetailsRepository.saveAndFlush(houseDetails);

        int databaseSizeBeforeDelete = houseDetailsRepository.findAll().size();

        // Delete the houseDetails
        restHouseDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, houseDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HouseDetails> houseDetailsList = houseDetailsRepository.findAll();
        assertThat(houseDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
