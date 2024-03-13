package ru.practicum.main.exception.Event;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NullPointerExceptionpEvent extends RuntimeException {
    public NullPointerExceptionpEvent(String messege) {
        super(messege);
    }
}
