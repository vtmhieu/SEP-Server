package group25.sep.server.service;

import group25.sep.server.model.Budget;
import group25.sep.server.model.Event;

public interface BudgetService {

    Budget createBudget(Budget budget);

    Budget getBudgetById(Long id);
    Budget createEventBudget(Budget budgetRequest);
}
