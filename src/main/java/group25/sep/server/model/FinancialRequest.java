package group25.sep.server.model;

import group25.sep.server.model.enums.Department;
import group25.sep.server.model.enums.FinancialRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "requesting_department", nullable = false)
    private Department requestingDepartment;

    @Column(name = "project_reference", nullable = false)
    private String projectReference;

    @Column(name = "required_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal requiredAmount;

    @Column(name = "currency", nullable = false, length = 3)
    @Builder.Default
    private String currency = "USD";

    @Column(name = "reason", columnDefinition = "TEXT", nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private FinancialRequestStatus status = FinancialRequestStatus.PENDING;

    @Column(name = "fm_notes", columnDefinition = "TEXT")
    private String fmNotes;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
