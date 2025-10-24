package group25.sep.server.repository;

import group25.sep.server.model.FinancialRequest;
import group25.sep.server.model.enums.Department;
import group25.sep.server.model.enums.FinancialRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FinancialRequestRepository extends JpaRepository<FinancialRequest, Long> {

    List<FinancialRequest> findByRequestedBySmPmId(Long smPmId);

    List<FinancialRequest> findByStatus(FinancialRequestStatus status);

    List<FinancialRequest> findByRequestingDepartment(Department department);

    List<FinancialRequest> findByProjectReference(String projectReference);

    @Query("SELECT f FROM FinancialRequest f WHERE f.requiredAmount BETWEEN :minAmount AND :maxAmount")
    List<FinancialRequest> findByRequiredAmountBetween(@Param("minAmount") BigDecimal minAmount,
            @Param("maxAmount") BigDecimal maxAmount);

    @Query("SELECT f FROM FinancialRequest f WHERE f.requiredAmount >= :minAmount")
    List<FinancialRequest> findByRequiredAmountGreaterThanEqual(@Param("minAmount") BigDecimal minAmount);
}
