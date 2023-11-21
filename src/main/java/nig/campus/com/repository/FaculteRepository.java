package nig.campus.com.repository;

import nig.campus.com.domain.Faculte;
import nig.campus.com.domain.Universite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Faculte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FaculteRepository extends JpaRepository<Faculte, Long> {

   // List<Faculte> findAllByUniversite(Universite uni);
}
