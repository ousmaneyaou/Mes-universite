package nig.campus.com.repository;

import nig.campus.com.domain.Depot;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Depot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepotRepository extends JpaRepository<Depot, Long> {}
