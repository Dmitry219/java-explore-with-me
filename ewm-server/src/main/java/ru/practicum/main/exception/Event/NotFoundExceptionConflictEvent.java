package ru.practicum.main.exception.Event;


import lombok.NoArgsConstructor;


@NoArgsConstructor
public class NotFoundExceptionConflictEvent extends RuntimeException {
    public NotFoundExceptionConflictEvent(String messege) {
        super(messege);
    }
}
