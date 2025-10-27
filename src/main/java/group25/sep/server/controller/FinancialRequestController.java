package group25.sep.server.controller;

import group25.sep.server.model.FinancialRequest;
import group25.sep.server.model.enums.Department;
import group25.sep.server.model.enums.FinancialRequestStatus;
import group25.sep.server.service.FinancialRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/financial-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FinancialRequestController {

    private final FinancialRequestService financialRequestService;

    @PostMapping
    public ResponseEntity<FinancialRequest> createFinancialRequest(@RequestBody FinancialRequest financialRequest) {
        try {
            FinancialRequest createdRequest = financialRequestService.createFinancialRequest(financialRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialRequest> getFinancialRequestById(@PathVariable Long id) {
        return financialRequestService.getFinancialRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<FinancialRequest>> getAllFinancialRequests() {
        List<FinancialRequest> requests = financialRequestService.getAllFinancialRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<FinancialRequest>> getFinancialRequestsByStatus(
            @PathVariable FinancialRequestStatus status) {
        List<FinancialRequest> requests = financialRequestService.getFinancialRequestsByStatus(status);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<FinancialRequest>> getFinancialRequestsByDepartment(
            @PathVariable Department department) {
        List<FinancialRequest> requests = financialRequestService.getFinancialRequestsByDepartment(department);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/project/{projectReference}")
    public ResponseEntity<List<FinancialRequest>> getFinancialRequestsByProjectReference(
            @PathVariable String projectReference) {
        List<FinancialRequest> requests = financialRequestService
                .getFinancialRequestsByProjectReference(projectReference);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/amount-range")
    public ResponseEntity<List<FinancialRequest>> getFinancialRequestsByAmountRange(
            @RequestParam BigDecimal minAmount,
            @RequestParam BigDecimal maxAmount) {
        List<FinancialRequest> requests = financialRequestService.getFinancialRequestsByAmountRange(minAmount,
                maxAmount);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/min-amount/{minAmount}")
    public ResponseEntity<List<FinancialRequest>> getFinancialRequestsByMinAmount(@PathVariable BigDecimal minAmount) {
        List<FinancialRequest> requests = financialRequestService.getFinancialRequestsByMinAmount(minAmount);
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<FinancialRequest> updateFinancialRequestStatus(@PathVariable Long id,
            @RequestBody Map<String, FinancialRequestStatus> request) {
        try {
            FinancialRequestStatus status = request.get("status");
            FinancialRequest updatedRequest = financialRequestService.updateFinancialRequestStatus(id, status);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}/fm-notes")
    public ResponseEntity<FinancialRequest> updateFmNotes(@PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String fmNotes = request.get("fmNotes");
            FinancialRequest updatedRequest = financialRequestService.updateFmNotes(id, fmNotes);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialRequest(@PathVariable Long id) {
        try {
            financialRequestService.deleteFinancialRequest(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
