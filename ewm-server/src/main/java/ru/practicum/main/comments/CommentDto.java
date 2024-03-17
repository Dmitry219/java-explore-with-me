package ru.practicum.main.comments;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private Long id;
    @NotBlank
    @Length(min = 5, max = 1000)
    private String text;
    private String authorName;
    private LocalDateTime created;
}
