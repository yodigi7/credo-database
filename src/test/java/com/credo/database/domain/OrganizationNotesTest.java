package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationNotesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationNotes.class);
        OrganizationNotes organizationNotes1 = new OrganizationNotes();
        organizationNotes1.setId(1L);
        OrganizationNotes organizationNotes2 = new OrganizationNotes();
        organizationNotes2.setId(organizationNotes1.getId());
        assertThat(organizationNotes1).isEqualTo(organizationNotes2);
        organizationNotes2.setId(2L);
        assertThat(organizationNotes1).isNotEqualTo(organizationNotes2);
        organizationNotes1.setId(null);
        assertThat(organizationNotes1).isNotEqualTo(organizationNotes2);
    }
}
