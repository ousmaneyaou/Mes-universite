package nig.campus.com.service;

import java.util.Optional;
import nig.campus.com.domain.Session;
import nig.campus.com.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Session}.
 */
@Service
@Transactional
public class SessionService {

    private final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Save a session.
     *
     * @param session the entity to save.
     * @return the persisted entity.
     */
    public Session save(Session session) {
        log.debug("Request to save Session : {}", session);
        return sessionRepository.save(session);
    }

    /**
     * Update a session.
     *
     * @param session the entity to save.
     * @return the persisted entity.
     */
    public Session update(Session session) {
        log.debug("Request to update Session : {}", session);
        return sessionRepository.save(session);
    }

    /**
     * Partially update a session.
     *
     * @param session the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Session> partialUpdate(Session session) {
        log.debug("Request to partially update Session : {}", session);

        return sessionRepository
            .findById(session.getId())
            .map(existingSession -> {
                if (session.getIntitule() != null) {
                    existingSession.setIntitule(session.getIntitule());
                }
                if (session.getDateDebut() != null) {
                    existingSession.setDateDebut(session.getDateDebut());
                }
                if (session.getDateFin() != null) {
                    existingSession.setDateFin(session.getDateFin());
                }

                return existingSession;
            })
            .map(sessionRepository::save);
    }

    /**
     * Get all the sessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Session> findAll(Pageable pageable) {
        log.debug("Request to get all Sessions");
        return sessionRepository.findAll(pageable);
    }

    /**
     * Get one session by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Session> findOne(Long id) {
        log.debug("Request to get Session : {}", id);
        return sessionRepository.findById(id);
    }

    /**
     * Delete the session by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Session : {}", id);
        sessionRepository.deleteById(id);
    }
}
