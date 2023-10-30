package nig.campus.com.repository;

import nig.campus.com.domain.AnneeScolaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AnneeScolaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnneeScolaireRepository extends JpaRepository<AnneeScolaire, Long> {}
