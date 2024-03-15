package ru.practicum.main.exception.User;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class NotFoundExceptionUser extends RuntimeException {
    public NotFoundExceptionUser(String messege) {
        super(messege);
    }
}
