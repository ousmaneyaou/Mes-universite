package nig.campus.com.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import nig.campus.com.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FiliereDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FiliereDTO.class);
        FiliereDTO filiereDTO1 = new FiliereDTO();
        filiereDTO1.setId(1L);
        FiliereDTO filiereDTO2 = new FiliereDTO();
        assertThat(filiereDTO1).isNotEqualTo(filiereDTO2);
        filiereDTO2.setId(filiereDTO1.getId());
        assertThat(filiereDTO1).isEqualTo(filiereDTO2);
        filiereDTO2.setId(2L);
        assertThat(filiereDTO1).isNotEqualTo(filiereDTO2);
        filiereDTO1.setId(null);
        assertThat(filiereDTO1).isNotEqualTo(filiereDTO2);
    }
}
