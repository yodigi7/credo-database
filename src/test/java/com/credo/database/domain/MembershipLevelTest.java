package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MembershipLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MembershipLevel.class);
        MembershipLevel membershipLevel1 = new MembershipLevel();
        membershipLevel1.setId(1L);
        MembershipLevel membershipLevel2 = new MembershipLevel();
        membershipLevel2.setId(membershipLevel1.getId());
        assertThat(membershipLevel1).isEqualTo(membershipLevel2);
        membershipLevel2.setId(2L);
        assertThat(membershipLevel1).isNotEqualTo(membershipLevel2);
        membershipLevel1.setId(null);
        assertThat(membershipLevel1).isNotEqualTo(membershipLevel2);
    }
}
