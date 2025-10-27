package group25.sep.server.service;

import group25.sep.server.dto.EventPatchRequest;
import group25.sep.server.model.Budget;
import group25.sep.server.model.Event;
import group25.sep.server.model.enums.EventStatus;
import group25.sep.server.repository.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import group25.sep.server.service.BudgetService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final BudgetService budgetService;

    public EventServiceImpl(EventRepository eventRepository, BudgetService budgetService) {
        this.eventRepository = eventRepository;
        this.budgetService = budgetService;
    }

    @Override
    public Event createEvent(Event event) {
        if (event == null || event.getName() == null || event.getName().trim().isEmpty() || event.getStatus() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Event, event name, and status must not be null or empty"
            );
        }
        return eventRepository.save(event);
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with id: " + id)
                );
    }
    @Override
    public List<Event> getEventsByStatus(String status) {
        try {
            EventStatus eventStatus = EventStatus.valueOf(status.toUpperCase());
            List<Event> events = eventRepository.findAllByStatus(eventStatus);
            return events;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid event status: " + status
            );
        }
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Event updateEventStatus(Long id, String status) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with id: " + id)
                );

        try {
            EventStatus newStatus = EventStatus.valueOf(status.toUpperCase());
            event.setStatus(newStatus);
            return eventRepository.save(event);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid event status: " + status
            );
        }
    }

    @Override
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Event not found with id: " + id
            );
        }
        eventRepository.deleteById(id);
    }


    @Override
    public Event updateEventPartial(Long eventId, EventPatchRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found with id: " + eventId)
                );

        if (request.getStatus() != null) {
            try {
                event.setStatus(EventStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid event status: " + request.getStatus()
                );
            }
        }

        if (request.getBudget() != null) {
            Budget savedBudget = budgetService.createEventBudget(request.getBudget());
            event.setBudget(savedBudget);
        }

        return eventRepository.save(event);
    }
}