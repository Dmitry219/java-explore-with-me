package ru.practicum.main.exception.User;


import lombok.NoArgsConstructor;



@NoArgsConstructor
public class NotFoundExceptionConflictUser extends RuntimeException {
    public NotFoundExceptionConflictUser(String messege) {
        super(messege);
    }
}
