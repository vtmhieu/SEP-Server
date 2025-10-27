package group25.sep.server.service;

import group25.sep.server.model.Budget;
import group25.sep.server.model.BudgetItem;
import group25.sep.server.repository.BudgetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Budget not found with id: " + id
                ));
    }

    @Override
    public Budget createEventBudget(Budget budgetRequest) {
        if (budgetRequest == null) {
            return null;
        }

        budgetRequest.setTotalAmount(calculateTotal(budgetRequest.getItems()));

        return budgetRepository.save(budgetRequest);
    }

    private BigDecimal calculateTotal(List<BudgetItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return items.stream()
                .map(BudgetItem::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}