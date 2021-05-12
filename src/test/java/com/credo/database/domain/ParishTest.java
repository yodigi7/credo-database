package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ParishTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Parish.class);
        Parish parish1 = new Parish();
        parish1.setId(1L);
        Parish parish2 = new Parish();
        parish2.setId(parish1.getId());
        assertThat(parish1).isEqualTo(parish2);
        parish2.setId(2L);
        assertThat(parish1).isNotEqualTo(parish2);
        parish1.setId(null);
        assertThat(parish1).isNotEqualTo(parish2);
    }
}
