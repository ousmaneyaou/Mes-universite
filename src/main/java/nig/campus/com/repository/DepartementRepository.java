package nig.campus.com.repository;

import nig.campus.com.domain.Departement;
import nig.campus.com.domain.Faculte;
import nig.campus.com.domain.Universite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Departement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartementRepository extends JpaRepository<Departement, Long> {
    List<Departement> findAllByFaculte(Faculte fac);
}
