package group25.sep.server.repository;

import group25.sep.server.model.RecruitmentRequest;
import group25.sep.server.model.enums.ContractType;
import group25.sep.server.model.enums.Department;
import group25.sep.server.model.enums.RecruitmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitmentRequestRepository extends JpaRepository<RecruitmentRequest, Long> {

    List<RecruitmentRequest> findByStatus(RecruitmentStatus status);

    List<RecruitmentRequest> findByContractType(ContractType contractType);

    List<RecruitmentRequest> findByRequestingDepartment(Department department);

    List<RecruitmentRequest> findByJobTitleContainingIgnoreCase(String jobTitle);

    List<RecruitmentRequest> findByYearsOfExperienceGreaterThanEqual(Integer yearsOfExperience);
}
