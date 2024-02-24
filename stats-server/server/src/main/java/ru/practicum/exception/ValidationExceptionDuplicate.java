package ru.practicum.exception;

public class ValidationExceptionDuplicate extends RuntimeException {
    public ValidationExceptionDuplicate(String messege) {
        super(messege);
    }
}
