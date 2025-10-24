package group25.sep.server.model;

import group25.sep.server.model.enums.ContractType;
import group25.sep.server.model.enums.Department;
import group25.sep.server.model.enums.RecruitmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "recruitment_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", nullable = false)
    private ContractType contractType;

    @Enumerated(EnumType.STRING)
    @Column(name = "requesting_department", nullable = false)
    private Department requestingDepartment;

    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "job_description", columnDefinition = "TEXT", nullable = false)
    private String jobDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RecruitmentStatus status = RecruitmentStatus.OPEN;

    @Column(name = "hr_notes", columnDefinition = "TEXT")
    private String hrNotes;

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
