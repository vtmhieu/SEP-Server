package group25.sep.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import group25.sep.server.model.Budget;
import group25.sep.server.model.BudgetItem;
import group25.sep.server.repository.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BudgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BudgetRepository budgetRepository;

    private Budget savedBudget;

    @BeforeEach
    void setup() {
        BudgetItem item1 = new BudgetItem();
        item1.setDescription("Venue");
        item1.setAmount(BigDecimal.valueOf(5000));

        BudgetItem item2 = new BudgetItem();
        item2.setDescription("Catering");
        item2.setAmount(BigDecimal.valueOf(2000));

        Budget budget = new Budget();
        budget.setDescription("Annual Meetup Budget");
        budget.setItems(List.of(item1, item2));
        budget.setTotalAmount(BigDecimal.valueOf(7000));

        savedBudget = budgetRepository.save(budget);
    }

    @Test
    void getBudgetByIdSuccessfully() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/1/budget/" + savedBudget.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertEquals(200, status);

        String jsonResponse = result.getResponse().getContentAsString();
        Budget response = objectMapper.readValue(jsonResponse, Budget.class);

        assertNotNull(response);
        assertEquals(savedBudget.getId(), response.getId());
        assertEquals("Annual Meetup Budget", response.getDescription());
        assertEquals(0, response.getTotalAmount().compareTo(BigDecimal.valueOf(7000)));
    }

    @Test
    void getBudgetByIdNotFound() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/1/budget/9999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        String responseBody = result.getResponse().getContentAsString();

        System.out.println("Response: " + responseBody);

        assertEquals(404, status);
        assertTrue(responseBody.contains("Budget not found with id: 9999"));
        assertTrue(responseBody.contains("\"status\":404"));
    }
}
