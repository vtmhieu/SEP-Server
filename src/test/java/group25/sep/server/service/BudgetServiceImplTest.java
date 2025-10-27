package group25.sep.server.service;

import group25.sep.server.model.Budget;
import group25.sep.server.model.BudgetItem;
import group25.sep.server.repository.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BudgetServiceImplTest {

    private BudgetRepository budgetRepository;
    private BudgetServiceImpl budgetService;

    @BeforeEach
    void setUp() {
        budgetRepository = mock(BudgetRepository.class);
        budgetService = new BudgetServiceImpl(budgetRepository);
    }

    @Test
    void getBudgetByIdSuccessfully() {
        Budget budget = new Budget();
        budget.setId(1L);
        budget.setDescription("Team Building Event");

        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget));

        Budget result = budgetService.getBudgetById(1L);

        verify(budgetRepository, times(1)).findById(1L);
        assertNotNull(result);
        assertEquals(budget, result);
        assertEquals("Team Building Event", result.getDescription());
    }

    @Test
    void getBudgetByIdWithNotFound() {
        when(budgetRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> budgetService.getBudgetById(1L));
        assertTrue(ex.getMessage().contains("Budget not found with id: 1"));
    }

    @Test
    void createEventBudgetSuccessfully() {
        BudgetItem item1 = new BudgetItem();
        item1.setAmount(BigDecimal.valueOf(2000));

        BudgetItem item2 = new BudgetItem();
        item2.setAmount(BigDecimal.valueOf(3000));

        Budget budget = new Budget();
        budget.setItems(List.of(item1, item2));

        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget result = budgetService.createEventBudget(budget);

        assertEquals(BigDecimal.valueOf(5000), result.getTotalAmount());
        verify(budgetRepository).save(budget);
    }

    @Test
    void createEventWithNullBudget() {
        Budget result = budgetService.createEventBudget(null);
        assertNull(result);
        verify(budgetRepository, never()).save(any());
    }

    @Test
    void createEventBudgetWhenNoItems() {
        Budget budget = new Budget();
        budget.setItems(List.of());

        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget result = budgetService.createEventBudget(budget);

        assertEquals(BigDecimal.ZERO, result.getTotalAmount());
        verify(budgetRepository).save(budget);
    }

    @Test
    void createEventBudgetWhenNullAmountsInItems() {
        BudgetItem item1 = new BudgetItem();
        item1.setAmount(null);

        BudgetItem item2 = new BudgetItem();
        item2.setAmount(BigDecimal.valueOf(2500));

        Budget budget = new Budget();
        budget.setItems(List.of(item1, item2));

        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        Budget result = budgetService.createEventBudget(budget);

        assertEquals(BigDecimal.valueOf(2500), result.getTotalAmount());
        verify(budgetRepository).save(budget);
    }
}
