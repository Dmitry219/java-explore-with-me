package ru.practicum.main.error;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.practicum.main.exception.*;
import ru.practicum.main.exception.Event.AuthorizationFailureExceptionEvent;
import ru.practicum.main.exception.Event.NotFoundExceptionConflictEvent;
import ru.practicum.main.exception.Event.NullPointerExceptionpEvent;
import ru.practicum.main.exception.User.NotFoundExceptionConflictUser;
import ru.practicum.main.exception.User.NotFoundExceptionUser;
import ru.practicum.main.exception.User.NullPointerExceptionpUser;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    //---------------------User, Category--------------------------------------
    @ExceptionHandler(value = {NullPointerExceptionpUser.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)//400
    public ApiError handleNullPointerExceptionUser(final Exception e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Incorrectly made request.")
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(value = {NotFoundExceptionUser.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)//404
    public ApiError handleNotFoundExceptionUser(final Exception e) {
        log.error("Произошла ошибка, не корректные данные {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("The required object was not found.")
                .status(String.valueOf(HttpStatus.NOT_FOUND))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(value = {NotFoundExceptionConflictUser.class})
    @ResponseStatus(HttpStatus.CONFLICT)//409
    public ApiError handleNotFoundExceptionConflictUser(final Exception e) {
        log.error("Произошла ошибка, не корректные данные {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Integrity constraint has been violated.")
                .status(String.valueOf(HttpStatus.CONFLICT))
                .timestamp(LocalDateTime.now())
                .build();
    }

    //--------------------Close User, Category--------------------------------

    //---------------------Request--------------------------------------


    //--------------------Close Request--------------------------------

    //---------------------Event--------------------------------------
    @ExceptionHandler(value = {NullPointerExceptionpEvent.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)//400
    public ApiError handleNullPointerExceptionEvent(final Exception e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Event must not be published")
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(value = {NotFoundExceptionConflictEvent.class})
    @ResponseStatus(HttpStatus.CONFLICT)//409
    public ApiError handleNotFoundExceptionConflictEvent(final Exception e) {
        log.error("Произошла ошибка, не корректные данные {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("For the requested operation the conditions are not met")
                .status(String.valueOf(HttpStatus.CONFLICT))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(value = {AuthorizationFailureExceptionEvent.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleAuthorizationFailureExceptionEvent(final RuntimeException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("For the requested operation the conditions are not met.")
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .timestamp(LocalDateTime.now())
                .build();
    }

    //--------------------Close Event--------------------------------


    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final MissingServletRequestParameterException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());

        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Incorrectly made request.")
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(value = {AuthorizationFailureException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleAuthorizationFailure(final RuntimeException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Incorrectly made request.")
                .status(String.valueOf(HttpStatus.FORBIDDEN))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final MethodArgumentNotValidException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());

        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Incorrectly made request.")
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final ValidationUserException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Incorrectly made request.")
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final NullPointerException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Incorrectly made request.")
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final IllegalArgumentException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Incorrectly made request.")
                .status(String.valueOf(HttpStatus.BAD_REQUEST))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNullPointerExceptionNotFound(final Exception e) {
        log.error("Произошла ошибка, не корректные данные {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Incorrectly made request.")
                .status(String.valueOf(HttpStatus.NOT_FOUND))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(value = {NotFoundExceptionConflict.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNullPointerException(final Exception e) {
        log.error("Произошла ошибка, не корректные данные {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Integrity constraint has been violated.")
                .status(String.valueOf(HttpStatus.CONFLICT))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(final Exception e) {
        log.error("Произошла ошибка, не корректные данные {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Integrity constraint has been violated.")
                .status(String.valueOf(HttpStatus.CONFLICT))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleSqlExceptionHelper(final DataIntegrityViolationException e) {
        log.error("Произошла ошибка, не корректные данные {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Integrity constraint has been violated.")
                .status(String.valueOf(HttpStatus.CONFLICT))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(value = {ValidationExceptionDuplicate.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleValidationExceptionDublicate(final RuntimeException e) {
        log.error("Произошла ошибка валидации {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Integrity constraint has been violated.")
                .status(String.valueOf(HttpStatus.CONFLICT))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.error("Ошибка сервиса! {}", e.getMessage());
        return ApiError.builder()
                .errors(List.of(e.getStackTrace()))
                .message(String.valueOf(e.getMessage()))
                .reason("Ошибка сервиса!")
                .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                .timestamp(LocalDateTime.now())
                .build();
    }
}
