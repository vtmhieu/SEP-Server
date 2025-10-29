package group25.sep.server.service;

import group25.sep.server.model.FinancialRequest;
import group25.sep.server.model.enums.Department;
import group25.sep.server.model.enums.FinancialRequestStatus;
import group25.sep.server.repository.FinancialRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FinancialRequestServiceImpl implements FinancialRequestService {

    private final FinancialRequestRepository financialRequestRepository;

    @Override
    public FinancialRequest createFinancialRequest(FinancialRequest financialRequest) {
        return financialRequestRepository.save(financialRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FinancialRequest> getFinancialRequestById(Long id) {
        return financialRequestRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialRequest> getAllFinancialRequests() {
        return financialRequestRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialRequest> getFinancialRequestsByStatus(FinancialRequestStatus status) {
        return financialRequestRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialRequest> getFinancialRequestsByDepartment(Department department) {
        return financialRequestRepository.findByRequestingDepartment(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialRequest> getFinancialRequestsByProjectReference(String projectReference) {
        return financialRequestRepository.findByProjectReference(projectReference);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialRequest> getFinancialRequestsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        return financialRequestRepository.findByRequiredAmountBetween(minAmount, maxAmount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FinancialRequest> getFinancialRequestsByMinAmount(BigDecimal minAmount) {
        return financialRequestRepository.findByRequiredAmountGreaterThanEqual(minAmount);
    }

    @Override
    public FinancialRequest updateFinancialRequestStatus(Long id, FinancialRequestStatus status) {
        FinancialRequest request = financialRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Financial request not found with id: " + id));

        request.setStatus(status);
        return financialRequestRepository.save(request);
    }

    @Override
    public FinancialRequest updateFmNotes(Long id, String fmNotes) {
        FinancialRequest request = financialRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Financial request not found with id: " + id));

        request.setFmNotes(fmNotes);
        return financialRequestRepository.save(request);
    }

    @Override
    public void deleteFinancialRequest(Long id) {
        financialRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Financial request not found with id: " + id));

        financialRequestRepository.deleteById(id);
    }
}
