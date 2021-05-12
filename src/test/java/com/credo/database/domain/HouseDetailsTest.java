package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HouseDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HouseDetails.class);
        HouseDetails houseDetails1 = new HouseDetails();
        houseDetails1.setId(1L);
        HouseDetails houseDetails2 = new HouseDetails();
        houseDetails2.setId(houseDetails1.getId());
        assertThat(houseDetails1).isEqualTo(houseDetails2);
        houseDetails2.setId(2L);
        assertThat(houseDetails1).isNotEqualTo(houseDetails2);
        houseDetails1.setId(null);
        assertThat(houseDetails1).isNotEqualTo(houseDetails2);
    }
}
