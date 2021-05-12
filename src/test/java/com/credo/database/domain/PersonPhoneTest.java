package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonPhoneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonPhone.class);
        PersonPhone personPhone1 = new PersonPhone();
        personPhone1.setId(1L);
        PersonPhone personPhone2 = new PersonPhone();
        personPhone2.setId(personPhone1.getId());
        assertThat(personPhone1).isEqualTo(personPhone2);
        personPhone2.setId(2L);
        assertThat(personPhone1).isNotEqualTo(personPhone2);
        personPhone1.setId(null);
        assertThat(personPhone1).isNotEqualTo(personPhone2);
    }
}
