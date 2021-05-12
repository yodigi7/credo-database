package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HouseAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HouseAddress.class);
        HouseAddress houseAddress1 = new HouseAddress();
        houseAddress1.setId(1L);
        HouseAddress houseAddress2 = new HouseAddress();
        houseAddress2.setId(houseAddress1.getId());
        assertThat(houseAddress1).isEqualTo(houseAddress2);
        houseAddress2.setId(2L);
        assertThat(houseAddress1).isNotEqualTo(houseAddress2);
        houseAddress1.setId(null);
        assertThat(houseAddress1).isNotEqualTo(houseAddress2);
    }
}
