package group25.sep.server.model;

import jakarta.persistence.*;
import lombok.*;


import group25.sep.server.model.enums.TaskPriority;
import group25.sep.server.model.enums.TaskStatus;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "project_reference")
    private String projectReference;

    private String description;
    private String assignee;
    private String subteam;
    private String comments;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;
}
