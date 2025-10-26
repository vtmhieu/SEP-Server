package group25.sep.server.service;

import group25.sep.server.dto.EventPatchRequest;
import group25.sep.server.model.Event;

import java.util.List;

public interface EventService {
    Event createEvent(Event event);
    Event getEventById(Long id);

    List<Event> getEventsByStatus(String status);
    List<Event> getAllEvents();
    Event updateEventStatus(Long id, String status);
    void deleteEvent(Long id);
    Event updateEventPartial(Long eventId, EventPatchRequest request);
}