package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NameTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NameTag.class);
        NameTag nameTag1 = new NameTag();
        nameTag1.setId(1L);
        NameTag nameTag2 = new NameTag();
        nameTag2.setId(nameTag1.getId());
        assertThat(nameTag1).isEqualTo(nameTag2);
        nameTag2.setId(2L);
        assertThat(nameTag1).isNotEqualTo(nameTag2);
        nameTag1.setId(null);
        assertThat(nameTag1).isNotEqualTo(nameTag2);
    }
}
