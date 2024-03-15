package ru.practicum.main.evente.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.main.evente.StateAction;
import ru.practicum.main.evente.location.Location;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Builder
public class EventDtoRequestUpdateStateAction {
    @Length(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Length(min = 20, max = 7000)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @Positive
    private Long participantLimit;
    private Boolean requestModeration;
    @Length(min = 3, max = 120)
    private String title;
    private StateAction stateAction;
}
