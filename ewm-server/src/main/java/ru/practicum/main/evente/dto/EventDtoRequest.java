package ru.practicum.main.evente.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import org.hibernate.validator.constraints.Length;
import ru.practicum.main.evente.location.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
public class EventDtoRequest {
    @NotBlank(message = "annotation не должно быть пустым")
    @Length(min = 20, max = 2000)
    private String annotation;
    private long category;
    @NotBlank(message = "описание не может быть пустым")
    @Length(min = 20, max = 7000)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;
    @PositiveOrZero
    private long participantLimit = 0L;
    private boolean requestModeration = true;
    @Length(min = 3, max = 120)
    private String title;
}
