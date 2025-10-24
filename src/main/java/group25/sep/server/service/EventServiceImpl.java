package group25.sep.server.service;

import group25.sep.server.model.Event;
import group25.sep.server.model.enums.EventStatus;
import group25.sep.server.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));
    }
    @Override
    public List<Event> getEventsByStatus(String status) {
        try {
            EventStatus eventStatus = EventStatus.valueOf(status.toUpperCase());
            List<Event> events = eventRepository.findByStatus(eventStatus);

            if (events.isEmpty()) {
                throw new RuntimeException("No events found with status: " + status);
            }

            return events;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid event status: " + status, e);
        }
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event updateEventStatus(Long id, String status) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        try {
            EventStatus newStatus = EventStatus.valueOf(status.toUpperCase());
            event.setStatus(newStatus);
            return eventRepository.save(event);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid event status: " + status, e);
        }
    }
    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}