package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParishPhoneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParishPhone.class);
        ParishPhone parishPhone1 = new ParishPhone();
        parishPhone1.setId(1L);
        ParishPhone parishPhone2 = new ParishPhone();
        parishPhone2.setId(parishPhone1.getId());
        assertThat(parishPhone1).isEqualTo(parishPhone2);
        parishPhone2.setId(2L);
        assertThat(parishPhone1).isNotEqualTo(parishPhone2);
        parishPhone1.setId(null);
        assertThat(parishPhone1).isNotEqualTo(parishPhone2);
    }
}
