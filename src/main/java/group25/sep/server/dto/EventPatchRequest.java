package group25.sep.server.dto;

import group25.sep.server.model.Budget;
import lombok.Data;

@Data
public class EventPatchRequest {
    private String status;
    private Budget budget;
}

