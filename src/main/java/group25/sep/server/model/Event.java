package group25.sep.server.model;

import group25.sep.server.model.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    private String type;

    private String description;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "estimate_budget")
    private BigDecimal estimateBudget;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "budget_id", referencedColumnName = "id", nullable = true)
    private Budget budget;
}