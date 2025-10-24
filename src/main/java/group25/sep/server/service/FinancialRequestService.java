package group25.sep.server.service;

import group25.sep.server.model.FinancialRequest;
import group25.sep.server.model.enums.Department;
import group25.sep.server.model.enums.FinancialRequestStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FinancialRequestService {

    FinancialRequest createFinancialRequest(FinancialRequest financialRequest);

    Optional<FinancialRequest> getFinancialRequestById(Long id);

    List<FinancialRequest> getAllFinancialRequests();

    List<FinancialRequest> getFinancialRequestsByStatus(FinancialRequestStatus status);

    List<FinancialRequest> getFinancialRequestsByDepartment(Department department);

    List<FinancialRequest> getFinancialRequestsByProjectReference(String projectReference);

    List<FinancialRequest> getFinancialRequestsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);

    List<FinancialRequest> getFinancialRequestsByMinAmount(BigDecimal minAmount);

    FinancialRequest updateFinancialRequestStatus(Long id, FinancialRequestStatus status);

    FinancialRequest updateFmNotes(Long id, String fmNotes);

    void deleteFinancialRequest(Long id);
}
