package ru.practicum.main.exception.Event;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthorizationFailureExceptionEvent extends RuntimeException {
    public AuthorizationFailureExceptionEvent(String messege) {
        super(messege);
    }
}
