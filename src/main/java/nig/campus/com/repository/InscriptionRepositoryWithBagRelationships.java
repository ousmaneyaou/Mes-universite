package nig.campus.com.repository;

import java.util.List;
import java.util.Optional;
import nig.campus.com.domain.Inscription;
import org.springframework.data.domain.Page;

public interface InscriptionRepositoryWithBagRelationships {
    Optional<Inscription> fetchBagRelationships(Optional<Inscription> inscription);

    List<Inscription> fetchBagRelationships(List<Inscription> inscriptions);

    Page<Inscription> fetchBagRelationships(Page<Inscription> inscriptions);
}
