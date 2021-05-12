package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationAddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationAddress.class);
        OrganizationAddress organizationAddress1 = new OrganizationAddress();
        organizationAddress1.setId(1L);
        OrganizationAddress organizationAddress2 = new OrganizationAddress();
        organizationAddress2.setId(organizationAddress1.getId());
        assertThat(organizationAddress1).isEqualTo(organizationAddress2);
        organizationAddress2.setId(2L);
        assertThat(organizationAddress1).isNotEqualTo(organizationAddress2);
        organizationAddress1.setId(null);
        assertThat(organizationAddress1).isNotEqualTo(organizationAddress2);
    }
}
