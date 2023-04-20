package ru.yandex.practicum.filmorate.model.enumerate;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EventType {
    LIKE ("LIKE"),
    REVIEW ("REVIEW"),
    FRIEND ("FRIEND");

    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public static EventType getByType(String type) {
        return Arrays.stream(EventType.values()).filter(f -> f.type.equals(type)).findAny().orElse(null);
    }
}
