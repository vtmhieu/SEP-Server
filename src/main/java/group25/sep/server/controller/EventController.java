package group25.sep.server.controller;

import group25.sep.server.model.Event;
import group25.sep.server.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @GetMapping("/{status}")
    public ResponseEntity<List<Event>> getEventsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(eventService.getEventsByStatus(status));
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<Event> updateEventStatus(@PathVariable Long id, @PathVariable String status) {
        Event updatedEvent = eventService.updateEventStatus(id, status);
        return ResponseEntity.ok(updatedEvent);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
