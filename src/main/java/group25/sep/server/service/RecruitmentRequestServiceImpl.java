package group25.sep.server.service;

import group25.sep.server.model.RecruitmentRequest;
import group25.sep.server.model.enums.ContractType;
import group25.sep.server.model.enums.Department;
import group25.sep.server.model.enums.RecruitmentStatus;
import group25.sep.server.repository.RecruitmentRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentRequestServiceImpl implements RecruitmentRequestService {

    private final RecruitmentRequestRepository recruitmentRequestRepository;

    @Override
    public RecruitmentRequest createRecruitmentRequest(RecruitmentRequest recruitmentRequest) {
        return recruitmentRequestRepository.save(recruitmentRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecruitmentRequest> getRecruitmentRequestById(Long id) {
        return recruitmentRequestRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruitmentRequest> getAllRecruitmentRequests() {
        return recruitmentRequestRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruitmentRequest> getRecruitmentRequestsByStatus(RecruitmentStatus status) {
        return recruitmentRequestRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruitmentRequest> getRecruitmentRequestsByContractType(ContractType contractType) {
        return recruitmentRequestRepository.findByContractType(contractType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruitmentRequest> getRecruitmentRequestsByDepartment(Department department) {
        return recruitmentRequestRepository.findByRequestingDepartment(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruitmentRequest> searchRecruitmentRequestsByJobTitle(String jobTitle) {
        return recruitmentRequestRepository.findByJobTitleContainingIgnoreCase(jobTitle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecruitmentRequest> getRecruitmentRequestsByMinExperience(Integer yearsOfExperience) {
        return recruitmentRequestRepository.findByYearsOfExperienceGreaterThanEqual(yearsOfExperience);
    }

    @Override
    public RecruitmentRequest updateRecruitmentRequestStatus(Long id, RecruitmentStatus status) {
        RecruitmentRequest request = recruitmentRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recruitment request not found with id: " + id));

        request.setStatus(status);
        return recruitmentRequestRepository.save(request);
    }

    @Override
    public RecruitmentRequest updateHrNotes(Long id, String hrNotes) {
        RecruitmentRequest request = recruitmentRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recruitment request not found with id: " + id));

        request.setHrNotes(hrNotes);
        return recruitmentRequestRepository.save(request);
    }

    @Override
    public void deleteRecruitmentRequest(Long id) {
        recruitmentRequestRepository.deleteById(id);
    }
}
