package ru.practicum.main.evente.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.main.evente.StatusType;


import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private StatusType status;
}
