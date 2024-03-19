package ru.practicum.main.comments;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDtoShort {
    private String text;
    private String authorName;
    private LocalDateTime created;
}
