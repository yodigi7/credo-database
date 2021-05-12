package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonEmailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonEmail.class);
        PersonEmail personEmail1 = new PersonEmail();
        personEmail1.setId(1L);
        PersonEmail personEmail2 = new PersonEmail();
        personEmail2.setId(personEmail1.getId());
        assertThat(personEmail1).isEqualTo(personEmail2);
        personEmail2.setId(2L);
        assertThat(personEmail1).isNotEqualTo(personEmail2);
        personEmail1.setId(null);
        assertThat(personEmail1).isNotEqualTo(personEmail2);
    }
}
