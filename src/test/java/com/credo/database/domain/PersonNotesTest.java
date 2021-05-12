package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonNotesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonNotes.class);
        PersonNotes personNotes1 = new PersonNotes();
        personNotes1.setId(1L);
        PersonNotes personNotes2 = new PersonNotes();
        personNotes2.setId(personNotes1.getId());
        assertThat(personNotes1).isEqualTo(personNotes2);
        personNotes2.setId(2L);
        assertThat(personNotes1).isNotEqualTo(personNotes2);
        personNotes1.setId(null);
        assertThat(personNotes1).isNotEqualTo(personNotes2);
    }
}
