package nig.campus.com.repository;

import nig.campus.com.domain.Depot;
import nig.campus.com.domain.Faculte;
import nig.campus.com.domain.Universite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Depot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepotRepository extends JpaRepository<Depot, Long> {

    //List<Depot> findAllByFaculte(Faculte fac);

    List<Depot> findByNom(String nom);

}
