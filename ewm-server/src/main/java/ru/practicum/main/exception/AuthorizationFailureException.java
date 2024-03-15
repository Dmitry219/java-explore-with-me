package ru.practicum.main.exception;

public class AuthorizationFailureException extends RuntimeException {
    public AuthorizationFailureException(String messege) {
        super(messege);
    }
}
