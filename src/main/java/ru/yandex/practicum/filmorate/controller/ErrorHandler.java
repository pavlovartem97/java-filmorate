package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException e) {
        log.error(e.getMessage(), e.getStackTrace());
        return Map.of("Error", e.getMessage());
    }

    @ExceptionHandler({FilmNotFoundException.class, UserNotFoundException.class,
            GenreNotFoundException.class, MpaNotFoundException.class,
            ReviewNotFoundException.class, DirectorNotFoundException.class, OperationNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleFileNotFoundException(final RuntimeException e) {
        log.error(e.getMessage(), e.getStackTrace());
        return Map.of("Error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUserNotFoundException(final RuntimeException e) {
        log.error(e.getMessage(), e.getStackTrace());
        return Map.of("Error", e.getMessage());
    }
}
