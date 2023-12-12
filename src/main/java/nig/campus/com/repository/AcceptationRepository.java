package nig.campus.com.repository;

import java.util.List;
import nig.campus.com.domain.Acceptation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Acceptation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AcceptationRepository extends JpaRepository<Acceptation, Long> {
    @Query("select acceptation from Acceptation acceptation where acceptation.user.login = ?#{principal.username}")
    List<Acceptation> findByUserIsCurrentUser();
}
