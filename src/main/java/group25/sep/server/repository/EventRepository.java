package group25.sep.server.repository;


import group25.sep.server.model.Event;
import group25.sep.server.model.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByStatus(EventStatus status);
    List<Event> findAllByStatus(EventStatus status);

}