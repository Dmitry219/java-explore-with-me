package ru.practicum.main.exception.User;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NullPointerExceptionpUser extends RuntimeException {
    public NullPointerExceptionpUser(String messege) {
        super(messege);
    }
}
