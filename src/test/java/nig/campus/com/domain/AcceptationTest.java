package nig.campus.com.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nig.campus.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcceptationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Acceptation.class);
        Acceptation acceptation1 = new Acceptation();
        acceptation1.setId(1L);
        Acceptation acceptation2 = new Acceptation();
        acceptation2.setId(acceptation1.getId());
        assertThat(acceptation1).isEqualTo(acceptation2);
        acceptation2.setId(2L);
        assertThat(acceptation1).isNotEqualTo(acceptation2);
        acceptation1.setId(null);
        assertThat(acceptation1).isNotEqualTo(acceptation2);
    }
}
