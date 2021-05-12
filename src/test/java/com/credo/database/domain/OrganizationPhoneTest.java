package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationPhoneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationPhone.class);
        OrganizationPhone organizationPhone1 = new OrganizationPhone();
        organizationPhone1.setId(1L);
        OrganizationPhone organizationPhone2 = new OrganizationPhone();
        organizationPhone2.setId(organizationPhone1.getId());
        assertThat(organizationPhone1).isEqualTo(organizationPhone2);
        organizationPhone2.setId(2L);
        assertThat(organizationPhone1).isNotEqualTo(organizationPhone2);
        organizationPhone1.setId(null);
        assertThat(organizationPhone1).isNotEqualTo(organizationPhone2);
    }
}
