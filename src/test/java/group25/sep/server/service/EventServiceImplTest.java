package group25.sep.server.service;

import group25.sep.server.dto.EventPatchRequest;
import group25.sep.server.model.Budget;
import group25.sep.server.model.BudgetItem;
import group25.sep.server.model.Event;
import group25.sep.server.model.enums.EventStatus;
import group25.sep.server.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEventSuccessfullyWithBasicData() {
        Event event = new Event();
        event.setName("Conference");
        event.setStatus(EventStatus.PENDING);
        when(eventRepository.save(event)).thenReturn(event);

        Event result = eventService.createEvent(event);

        verify(eventRepository, times(1)).save(event);
        assertEquals("Conference", result.getName());
    }

    @Test
    void createEventSuccessfully() {
        LocalDateTime eventDate = LocalDateTime.now();
        Date startDate = new Date();
        Date endDate = new Date();

        Event inputEvent = Event.builder()
                .name("Workshop")
                .recordID("REC-101")
                .location("Main Hall")
                .type("Educational")
                .description("Technical workshop on software design")
                .attendees(50)
                .startDate(startDate)
                .endDate(endDate)
                .status(EventStatus.PENDING)
                .estimateBudget(BigDecimal.valueOf(10000))
                .budget(null)
                .build();

        Event expectedEvent = Event.builder()
                .name("Workshop")
                .recordID("REC-101")
                .location("Main Hall")
                .type("Educational")
                .description("Technical workshop on software design")
                .attendees(50)
                .startDate(startDate)
                .endDate(endDate)
                .status(EventStatus.PENDING)
                .estimateBudget(BigDecimal.valueOf(10000))
                .budget(null)
                .build();

        when(eventRepository.save(inputEvent)).thenReturn(expectedEvent);

        Event actualEvent = eventService.createEvent(inputEvent);

        verify(eventRepository, times(1)).save(inputEvent);
        assertEquals(expectedEvent, actualEvent);
    }

    @Test
    void createNullEvent() {
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> eventService.createEvent(null)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Event, event name, and status must not be null or empty"));
    }

    @Test
    void createEventWhenNameIsMissing() {
        Event event = Event.builder()
                .status(EventStatus.PENDING)
                .build();

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> eventService.createEvent(event)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Event, event name, and status must not be null or empty"));
    }

    @Test
    void createEventWhenNameIsBlank() {
        Event event = Event.builder()
                .name("   ")
                .status(EventStatus.PENDING)
                .build();

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> eventService.createEvent(event)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Event, event name, and status must not be null or empty"));
    }

    @Test
    void createEventWhenStatusIsMissing() {
        Event event = Event.builder()
                .name("Tech Meetup")
                .build();

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> eventService.createEvent(event)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Event, event name, and status must not be null or empty"));
    }

    @Test
    void getEventByIdSuccessfully() {
        Event event = new Event();
        event.setId(1L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Event result = eventService.getEventById(1L);

        assertEquals(1L, result.getId());
        verify(eventRepository).findById(1L);
    }

    @Test
    void getEventByIdNotFound() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> eventService.getEventById(1L));
        assertTrue(ex.getMessage().contains("Event not found with id: 1"));
    }

    @Test
    void getEventsByStatusSuccessfully() {
        Event e1 = new Event(); e1.setStatus(EventStatus.PENDING);
        when(eventRepository.findAllByStatus(EventStatus.PENDING))
                .thenReturn(List.of(e1));

        List<Event> result = eventService.getEventsByStatus("pending");

        assertEquals(1, result.size());
        assertEquals(EventStatus.PENDING, result.get(0).getStatus());
    }

    @Test
    void getEventsByStatusByInvalidStatus() {
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> eventService.getEventsByStatus("invalid")
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Invalid event status: invalid"));
    }

    @Test
    void getEventsByStatusWhenNoEventsFound() {
        when(eventRepository.findAllByStatus(EventStatus.PENDING))
                .thenReturn(Collections.emptyList());

        List<Event> result = eventService.getEventsByStatus("pending");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(eventRepository, times(1)).findAllByStatus(EventStatus.PENDING);
    }


    @Test
    void getAllEventsSuccessfully() {
        List<Event> mockEvents = List.of(new Event(), new Event());
        when(eventRepository.findAll()).thenReturn(mockEvents);

        List<Event> result = eventService.getAllEvents();

        assertEquals(2, result.size());
        verify(eventRepository).findAll();
    }


    @Test
    void updateEventStatusSuccessfully() {
        Event event = new Event();
        event.setId(1L);
        event.setStatus(EventStatus.PENDING);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event updated = eventService.updateEventStatus(1L, "accepted");

        assertEquals(EventStatus.ACCEPTED, updated.getStatus());
        verify(eventRepository).save(event);
    }

    @Test
    void updateEventStatusWithInvalidStatus() {
        Event event = new Event();
        event.setId(1L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> eventService.updateEventStatus(1L, "invalid")
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Invalid event status: invalid"));
    }

    @Test
    void updateEventStatusWithEventNotFound() {
        when(eventRepository.findById(2L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> eventService.updateEventStatus(2L, "PENDING")
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Event not found with id: 2"));
    }

    @Test
    void deleteEventSuccessfully() {
        when(eventRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> eventService.deleteEvent(1L));
        verify(eventRepository, times(1)).deleteById(1L);
    }


    @Test
    void deleteEventWithNotFound() {
        doThrow(new EmptyResultDataAccessException(1))
                .when(eventRepository).deleteById(99L);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> eventService.deleteEvent(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Event not found with id: 99"));
    }

    @Test
    void updateEventPartialSuccessfully() {
        Event event = new Event();
        event.setId(1L);
        event.setStatus(EventStatus.ACCEPTED);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        BudgetItem item1 = new BudgetItem();
        item1.setDescription("Catering");
        item1.setAmount(BigDecimal.valueOf(2000));

        BudgetItem item2 = new BudgetItem();
        item2.setDescription("Decorations");
        item2.setAmount(BigDecimal.valueOf(3000));

        Budget inputBudget = Budget.builder()
                .description("Workshop Expenses")
                .items(List.of(item1, item2))
                .build();

        EventPatchRequest patch = new EventPatchRequest();
        patch.setStatus("budgeted");
        patch.setBudget(inputBudget);

        Budget saveBudgetResult = Budget.builder()
                .id(10L)
                .description("Workshop Expenses")
                .items(List.of(item1, item2))
                .totalAmount(BigDecimal.valueOf(5000))
                .build();

        Event expectedEvent = new Event();
        expectedEvent.setId(event.getId());
        expectedEvent.setStatus(EventStatus.BUDGETED);
        expectedEvent.setBudget(saveBudgetResult);

        when(budgetService.createEventBudget(any(Budget.class))).thenReturn(saveBudgetResult);
        when(eventRepository.save(any(Event.class))).thenReturn(expectedEvent);

        Event result = eventService.updateEventPartial(1L, patch);

        verify(budgetService, times(1)).createEventBudget(any(Budget.class));
        verify(eventRepository, times(1)).save(expectedEvent);

        assertEquals(expectedEvent, result);
    }

    @Test
    void updateEventPartialWithNotFoundWhenEvent() {
        when(eventRepository.findById(10L)).thenReturn(Optional.empty());

        EventPatchRequest patch = new EventPatchRequest();
        patch.setStatus("APPROVED");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> eventService.updateEventPartial(10L, patch)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Event not found with id: 10"));
    }

    @Test
    void updateEventPartialWithInvalidStatus() {
        Event event = new Event();
        event.setId(1L);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        EventPatchRequest patch = new EventPatchRequest();
        patch.setStatus("INVALID_STATUS");

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> eventService.updateEventPartial(1L, patch)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Invalid event status: INVALID_STATUS"));
    }
}
