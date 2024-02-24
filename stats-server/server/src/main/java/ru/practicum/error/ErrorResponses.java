package ru.practicum.error;

public class ErrorResponses {
    private final String error;

    public ErrorResponses(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
