package ru.practicum.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.AuthorizationFailureException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationExceptionDuplicate;
import ru.practicum.exception.ValidationUserException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(value = {AuthorizationFailureException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponses handleAuthorizationFailure(final RuntimeException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return new ErrorResponses(
                String.format("Отказ в авторизации \"%s\".", e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponses handleValidationException(final MethodArgumentNotValidException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return new ErrorResponses(
                String.format("Ошибка с полем \"%s\".", e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponses handleValidationException(final ValidationUserException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return new ErrorResponses(
                String.format("Ошибка с полем \"%s\".", e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponses handleValidationException(final NullPointerException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return new ErrorResponses(
                String.format("Ошибка с полем \"%s\".", e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponses handleValidationException(final IllegalArgumentException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return new ErrorResponses(
                String.format("%s", e.getMessage())
        );
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponses handleNullPointerException(final Exception e) {
        log.error("Произошла ошибка, не корректные данные {}", e.getMessage());
        return new ErrorResponses(
                String.format("Ошибка \"%s\".", e.getMessage())
        );
    }

    @ExceptionHandler(value = {ValidationExceptionDuplicate.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponses handleValidationExceptionDublicate(final RuntimeException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return new ErrorResponses(
                String.format("Ошибка с полем \"%s\".", e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponses handleThrowable(final Throwable e) {
        log.error("Ошибка сервиса! {}", e.getMessage());
        return new ErrorResponses(
                "Произошла непредвиденная ошибка."
        );
    }
}
