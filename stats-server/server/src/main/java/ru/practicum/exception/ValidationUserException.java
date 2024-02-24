package ru.practicum.exception;

public class ValidationUserException extends RuntimeException {
    public ValidationUserException(String messege) {
        super(messege);
    }
}
