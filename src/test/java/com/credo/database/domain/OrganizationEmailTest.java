package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationEmailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationEmail.class);
        OrganizationEmail organizationEmail1 = new OrganizationEmail();
        organizationEmail1.setId(1L);
        OrganizationEmail organizationEmail2 = new OrganizationEmail();
        organizationEmail2.setId(organizationEmail1.getId());
        assertThat(organizationEmail1).isEqualTo(organizationEmail2);
        organizationEmail2.setId(2L);
        assertThat(organizationEmail1).isNotEqualTo(organizationEmail2);
        organizationEmail1.setId(null);
        assertThat(organizationEmail1).isNotEqualTo(organizationEmail2);
    }
}
