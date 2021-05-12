package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParishEmailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParishEmail.class);
        ParishEmail parishEmail1 = new ParishEmail();
        parishEmail1.setId(1L);
        ParishEmail parishEmail2 = new ParishEmail();
        parishEmail2.setId(parishEmail1.getId());
        assertThat(parishEmail1).isEqualTo(parishEmail2);
        parishEmail2.setId(2L);
        assertThat(parishEmail1).isNotEqualTo(parishEmail2);
        parishEmail1.setId(null);
        assertThat(parishEmail1).isNotEqualTo(parishEmail2);
    }
}
