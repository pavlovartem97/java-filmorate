package ru.yandex.practicum.filmorate.exception;

public class UnacceptableQueryException extends RuntimeException {
    public UnacceptableQueryException(String message) {
        super(message);
    }
}

