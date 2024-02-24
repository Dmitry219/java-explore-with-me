package ru.practicum.exception;

public class AuthorizationFailureException extends RuntimeException {
    public AuthorizationFailureException(String messege) {
        super(messege);
    }
}
