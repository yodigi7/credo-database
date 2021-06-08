package com.credo.database.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.credo.database.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventPerkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventPerk.class);
        EventPerk eventPerk1 = new EventPerk();
        eventPerk1.setId(1L);
        EventPerk eventPerk2 = new EventPerk();
        eventPerk2.setId(eventPerk1.getId());
        assertThat(eventPerk1).isEqualTo(eventPerk2);
        eventPerk2.setId(2L);
        assertThat(eventPerk1).isNotEqualTo(eventPerk2);
        eventPerk1.setId(null);
        assertThat(eventPerk1).isNotEqualTo(eventPerk2);
    }
}
