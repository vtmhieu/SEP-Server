package group25.sep.server.service;

import group25.sep.server.model.RecruitmentRequest;
import group25.sep.server.model.enums.ContractType;
import group25.sep.server.model.enums.Department;
import group25.sep.server.model.enums.RecruitmentStatus;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRequestService {

    RecruitmentRequest createRecruitmentRequest(RecruitmentRequest recruitmentRequest);

    Optional<RecruitmentRequest> getRecruitmentRequestById(Long id);

    List<RecruitmentRequest> getAllRecruitmentRequests();

    List<RecruitmentRequest> getRecruitmentRequestsByStatus(RecruitmentStatus status);

    List<RecruitmentRequest> getRecruitmentRequestsByContractType(ContractType contractType);

    List<RecruitmentRequest> getRecruitmentRequestsByDepartment(Department department);

    List<RecruitmentRequest> searchRecruitmentRequestsByJobTitle(String jobTitle);

    List<RecruitmentRequest> getRecruitmentRequestsByMinExperience(Integer yearsOfExperience);

    RecruitmentRequest updateRecruitmentRequestStatus(Long id, RecruitmentStatus status);

    RecruitmentRequest updateHrNotes(Long id, String hrNotes);

    void deleteRecruitmentRequest(Long id);
}
