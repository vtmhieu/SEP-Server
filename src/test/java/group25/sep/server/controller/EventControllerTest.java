package group25.sep.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import group25.sep.server.dto.EventPatchRequest;
import group25.sep.server.model.Budget;
import group25.sep.server.model.BudgetItem;
import group25.sep.server.model.Event;
import group25.sep.server.model.enums.EventStatus;
import group25.sep.server.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventController eventController;

    private Long createdEventId;

    private Event baseEventRequest;

    @Autowired
    private EventService eventService;

    @BeforeEach
    void setup(TestInfo testInfo) throws Exception {
        if (testInfo.getDisplayName().contains("getAllEventsWhenListIsEmpty")) {
            return;
        }

        baseEventRequest = new Event();
        baseEventRequest.setName("Workshop");
        baseEventRequest.setRecordID("REC-001");
        baseEventRequest.setLocation("Main Hall");
        baseEventRequest.setType("Educational");
        baseEventRequest.setDescription("Software design workshop");
        baseEventRequest.setAttendees(100);
        baseEventRequest.setStartDate(new Date());
        baseEventRequest.setEndDate(new Date());
        baseEventRequest.setStatus(EventStatus.PENDING);
        baseEventRequest.setEstimateBudget(BigDecimal.valueOf(20000));

        mockMvc = MockMvcBuilders
                .standaloneSetup(eventController)
                .setControllerAdvice(new ExceptionHandlerTest())
                .build();

        objectMapper = new ObjectMapper();

        String eventJson = objectMapper.writeValueAsString(baseEventRequest);

        MvcResult result = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();

        if (status == 200 && jsonResponse != null && !jsonResponse.isEmpty()) {
            Event createdEvent = objectMapper.readValue(jsonResponse, Event.class);
            this.createdEventId = createdEvent.getId();
        }
    }

    @Test
    void createEventSuccessfullyWithRequiredData() throws Exception {
        Event minimal = new Event();
        minimal.setName(baseEventRequest.getName());
        minimal.setStatus(baseEventRequest.getStatus());

        MvcResult result = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minimal)))
                .andExpect(status().isOk())
                .andReturn();

        Event event = objectMapper.readValue(result.getResponse().getContentAsString(), Event.class);
        assertNotNull(event.getId());
        assertEquals(baseEventRequest.getName(), event.getName());
        assertEquals(baseEventRequest.getStatus(), event.getStatus());
    }

    @Test
    void createEventSuccessfullyWithAllData() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(baseEventRequest)))
                .andExpect(status().isOk())
                .andReturn();

        Event actualEvent = objectMapper.readValue(result.getResponse().getContentAsString(), Event.class);

        assertNotNull(actualEvent.getId());
        assertEquals(baseEventRequest.getName(), actualEvent.getName());
        assertEquals(baseEventRequest.getRecordID(), actualEvent.getRecordID());
        assertEquals(baseEventRequest.getLocation(), actualEvent.getLocation());
        assertEquals(baseEventRequest.getStatus(), actualEvent.getStatus());
        assertEquals(baseEventRequest.getEstimateBudget(), actualEvent.getEstimateBudget());
    }

    @Test
    void createEventWhenNameIsNull() throws Exception {
        Event invalidEvent = new Event();
        invalidEvent.setName(null);
        invalidEvent.setStatus(EventStatus.PENDING);

        MvcResult result = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEvent)))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();
        Map<String, Object> responseBody = objectMapper.readValue(jsonResponse, Map.class);

        assertEquals(400, status);
        assertEquals(400, responseBody.get("status"));
        assertEquals("Bad Request", responseBody.get("error"));
        assertEquals("Event, event name, and status must not be null or empty", responseBody.get("message"));
    }

    @Test
    void createEventWhenStatusIsNull() throws Exception {
        Event invalidEvent = new Event();
        invalidEvent.setName("Conference");
        invalidEvent.setStatus(null);

        MvcResult result = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEvent)))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();
        Map<String, Object> responseBody = objectMapper.readValue(jsonResponse, Map.class);

        assertEquals(400, status);
        assertEquals(400, responseBody.get("status"));
        assertEquals("Bad Request", responseBody.get("error"));
        assertEquals("Event, event name, and status must not be null or empty", responseBody.get("message"));
    }

    @Test
    void getAllEventsWhenListIsEmpty() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/events"))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();

        assertEquals(200, status);

        List<Event> events = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Event.class)
        );

        System.out.println(events);
        assertNotNull(events);
        assertTrue(events.isEmpty());
    }

    @Test
    void getAllEventsWhenListHasData() throws Exception {
        List<Event> mockEvents = List.of(
                Event.builder().id(1L).name("Workshop").status(EventStatus.PENDING).build(),
                Event.builder().id(2L).name("Seminar").status(EventStatus.APPROVED).build(),
                Event.builder().id(3L).name("Conference").status(EventStatus.REJECTED).build()
        );

        EventService mockService = org.mockito.Mockito.mock(EventService.class);
        when(mockService.getAllEvents()).thenReturn(mockEvents);

        ReflectionTestUtils.setField(eventController, "eventService", mockService);

        MvcResult result = mockMvc.perform(get("/api/events"))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();

        assertEquals(200, status, "Expected HTTP 200 OK");

        List<Event> responseEvents = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Event.class)
        );

        assertNotNull(responseEvents);
        assertEquals(3, responseEvents.size());
        assertTrue(responseEvents.stream().anyMatch(e -> e.getName().equals("Workshop")));
        assertTrue(responseEvents.stream().anyMatch(e -> e.getName().equals("Seminar")));
        assertTrue(responseEvents.stream().anyMatch(e -> e.getName().equals("Conference")));
    }

    @Test
    void getEventByIdSuccessfully() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/events/" + createdEventId))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();

        Event fetchedEvent = objectMapper.readValue(jsonResponse, Event.class);

        assertEquals(200, status);
        assertNotNull(fetchedEvent);
        assertEquals(createdEventId, fetchedEvent.getId());
        assertEquals(baseEventRequest.getName(), fetchedEvent.getName());
        assertEquals(baseEventRequest.getStatus(), fetchedEvent.getStatus());
    }

    @Test
    void getEventByIdNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/events/9999"))
                .andReturn();

        int status = result.getResponse().getStatus();
        String responseBody = result.getResponse().getContentAsString();

        assertEquals(404, status);
        assertTrue(responseBody.contains("Event not found with id: 9999"));
    }

    @Test
    void getEventsByStatusSuccessfully() throws Exception {
        String requestJson = objectMapper.writeValueAsString(baseEventRequest);

        MvcResult createResult = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andReturn();

        int createStatus = createResult.getResponse().getStatus();
        assertEquals(200, createStatus);

        MvcResult result = mockMvc.perform(get("/api/events/status/pending"))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();

        List<Event> events = objectMapper.readValue(
                jsonResponse,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Event.class)
        );

        assertEquals(200, status);
        assertNotNull(events);
        assertFalse(events.isEmpty());
    }

    @Test
    void getEventsByStatusWithInvalidStatus() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/events/status/invalidStatus"))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();

        assertEquals(400, status);

        if (!jsonResponse.isEmpty()) {
            Map<String, Object> body = objectMapper.readValue(jsonResponse, Map.class);
            assertEquals(400, body.get("status"));
            assertEquals("Bad Request", body.get("error"));
            assertTrue(((String) body.get("message")).contains("Invalid event status: invalidStatus"));
        }
    }

    @Test
    void updateEventStatusSuccessfully() throws Exception {
        MvcResult result = mockMvc.perform(
                        put("/api/events/" + createdEventId + "/status/accepted")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();

        Event updatedEvent = objectMapper.readValue(jsonResponse, Event.class);

        assertEquals(200, status);
        assertNotNull(updatedEvent);
        assertEquals(createdEventId, updatedEvent.getId());
        assertEquals(EventStatus.ACCEPTED, updatedEvent.getStatus());
    }

    @Test
    void updateEventStatusWithEventNotFound() throws Exception {
        MvcResult result = mockMvc.perform(
                        put("/api/events/9999/status/approved")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();

        assertEquals(404, status);
        assertTrue(jsonResponse.contains("Event not found with id: 9999"));
    }

    @Test
    void updateEventStatusWithInvalidStatus() throws Exception {
        MvcResult result = mockMvc.perform(
                        put("/api/events/" + createdEventId + "/status/WRONG")
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();

        assertEquals(400, status);
        assertTrue(jsonResponse.contains("Invalid event status: WRONG")
        );
    }

    @Test
    void updateEventPartialSuccessfully() throws Exception {
        BudgetItem item1 = new BudgetItem();
        item1.setDescription("Catering");
        item1.setAmount(BigDecimal.valueOf(2000));

        BudgetItem item2 = new BudgetItem();
        item2.setDescription("Decorations");
        item2.setAmount(BigDecimal.valueOf(3000));

        Budget budget = new Budget();
        budget.setDescription("Workshop Budget");
        budget.setItems(List.of(item1, item2));

        EventPatchRequest patch = new EventPatchRequest();
        patch.setStatus("budgeted");
        patch.setBudget(budget);

        MvcResult result = mockMvc.perform(
                        patch("/api/events/" + this.createdEventId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patch)))
                .andReturn();

        int status = result.getResponse().getStatus();
        String jsonResponse = result.getResponse().getContentAsString();
        Event updated = objectMapper.readValue(jsonResponse, Event.class);

        assertEquals(200, status);
        assertNotNull(updated);
        assertEquals(EventStatus.BUDGETED, updated.getStatus());
        assertNotNull(updated.getBudget());
        assertEquals("Workshop Budget", updated.getBudget().getDescription());
        assertEquals(2, updated.getBudget().getItems().size());
        assertEquals(BigDecimal.valueOf(5000), updated.getBudget().getTotalAmount());
    }

    @Test
    void deleteEventSuccessfully() throws Exception {
        MvcResult result = mockMvc.perform(delete("/api/events/" + createdEventId))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(204, status);
    }

    @Test
    void deleteEventNotFound() throws Exception {
        MvcResult result = mockMvc.perform(delete("/api/events/9999"))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(404, status);

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Event not found with id: 9999"));
    }
}
