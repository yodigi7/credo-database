package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RibbonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ribbon.class);
        Ribbon ribbon1 = new Ribbon();
        ribbon1.setId(1L);
        Ribbon ribbon2 = new Ribbon();
        ribbon2.setId(ribbon1.getId());
        assertThat(ribbon1).isEqualTo(ribbon2);
        ribbon2.setId(2L);
        assertThat(ribbon1).isNotEqualTo(ribbon2);
        ribbon1.setId(null);
        assertThat(ribbon1).isNotEqualTo(ribbon2);
    }
}
