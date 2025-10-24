package group25.sep.server.controller;

import group25.sep.server.model.RecruitmentRequest;
import group25.sep.server.model.enums.ContractType;
import group25.sep.server.model.enums.Department;
import group25.sep.server.model.enums.RecruitmentStatus;
import group25.sep.server.service.RecruitmentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recruitment-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecruitmentRequestController {

    private final RecruitmentRequestService recruitmentRequestService;

    @PostMapping
    public ResponseEntity<RecruitmentRequest> createRecruitmentRequest(
            @RequestBody RecruitmentRequest recruitmentRequest) {
        try {
            RecruitmentRequest createdRequest = recruitmentRequestService.createRecruitmentRequest(recruitmentRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentRequest> getRecruitmentRequestById(@PathVariable Long id) {
        return recruitmentRequestService.getRecruitmentRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<RecruitmentRequest>> getAllRecruitmentRequests() {
        List<RecruitmentRequest> requests = recruitmentRequestService.getAllRecruitmentRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RecruitmentRequest>> getRecruitmentRequestsByStatus(
            @PathVariable RecruitmentStatus status) {
        List<RecruitmentRequest> requests = recruitmentRequestService.getRecruitmentRequestsByStatus(status);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/contract-type/{contractType}")
    public ResponseEntity<List<RecruitmentRequest>> getRecruitmentRequestsByContractType(
            @PathVariable ContractType contractType) {
        List<RecruitmentRequest> requests = recruitmentRequestService
                .getRecruitmentRequestsByContractType(contractType);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<RecruitmentRequest>> getRecruitmentRequestsByDepartment(
            @PathVariable Department department) {
        List<RecruitmentRequest> requests = recruitmentRequestService.getRecruitmentRequestsByDepartment(department);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecruitmentRequest>> searchRecruitmentRequestsByJobTitle(@RequestParam String jobTitle) {
        List<RecruitmentRequest> requests = recruitmentRequestService.searchRecruitmentRequestsByJobTitle(jobTitle);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/experience/{yearsOfExperience}")
    public ResponseEntity<List<RecruitmentRequest>> getRecruitmentRequestsByMinExperience(
            @PathVariable Integer yearsOfExperience) {
        List<RecruitmentRequest> requests = recruitmentRequestService
                .getRecruitmentRequestsByMinExperience(yearsOfExperience);
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RecruitmentRequest> updateRecruitmentRequestStatus(@PathVariable Long id,
            @RequestBody Map<String, RecruitmentStatus> request) {
        try {
            RecruitmentStatus status = request.get("status");
            RecruitmentRequest updatedRequest = recruitmentRequestService.updateRecruitmentRequestStatus(id, status);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}/hr-notes")
    public ResponseEntity<RecruitmentRequest> updateHrNotes(@PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String hrNotes = request.get("hrNotes");
            RecruitmentRequest updatedRequest = recruitmentRequestService.updateHrNotes(id, hrNotes);
            return ResponseEntity.ok(updatedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitmentRequest(@PathVariable Long id) {
        try {
            recruitmentRequestService.deleteRecruitmentRequest(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
