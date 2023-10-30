package nig.campus.com.repository;

import nig.campus.com.domain.Bachelier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bachelier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BachelierRepository extends JpaRepository<Bachelier, Long> {}
