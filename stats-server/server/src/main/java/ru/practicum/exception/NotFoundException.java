package ru.practicum.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String messege) {
        super(messege);
    }
}
