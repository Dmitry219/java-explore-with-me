package ru.practicum.main.QueryEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.main.evente.StatusType;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AdminEventFilterRequest {
    private List<Long> users;
    private List<StatusType> states;
    private List<Long> categories;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd;
    @Positive
    private int from;
    @Positive
    private int size;
}
