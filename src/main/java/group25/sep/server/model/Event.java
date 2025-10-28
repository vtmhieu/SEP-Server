package group25.sep.server.model;

import group25.sep.server.model.enums.EventStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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

    @Column(name = "record_id")
    private String recordID;

    private String location;

    private String type;

    private String description;

    private int attendees;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "estimate_budget")
    private BigDecimal estimateBudget;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "budget_id", referencedColumnName = "id", nullable = true)
    private Budget budget;


}