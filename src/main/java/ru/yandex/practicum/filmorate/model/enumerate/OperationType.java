package ru.yandex.practicum.filmorate.model.enumerate;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OperationType {
    REMOVE ("REMOVE"),
    ADD ("ADD"),
    UPDATE ("UPDATE");

    private final String type;

    OperationType(String type) {
        this.type = type;
    }

    public static OperationType getByType(String type) {
        return Arrays.stream(OperationType.values()).filter(f -> f.type.equals(type)).findAny().orElse(null);
    }
}
