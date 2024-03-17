package ru.practicum.main.comments;

import lombok.experimental.UtilityClass;
import ru.practicum.main.evente.Event;
import ru.practicum.main.users.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapping {

    public  Comment toComment(CommentDto commentDto, Event event, User user) {
        return Comment.builder()
                .text(commentDto.getText())
                .event(event)
                .author(user)
                .created(LocalDateTime.now())
                .build();
    }

    public  CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public  CommentDtoShort toCommentDtoShort(Comment comment) {
        return CommentDtoShort.builder()
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
