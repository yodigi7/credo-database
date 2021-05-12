package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RelationshipTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Relationship.class);
        Relationship relationship1 = new Relationship();
        relationship1.setId(1L);
        Relationship relationship2 = new Relationship();
        relationship2.setId(relationship1.getId());
        assertThat(relationship1).isEqualTo(relationship2);
        relationship2.setId(2L);
        assertThat(relationship1).isNotEqualTo(relationship2);
        relationship1.setId(null);
        assertThat(relationship1).isNotEqualTo(relationship2);
    }
}
