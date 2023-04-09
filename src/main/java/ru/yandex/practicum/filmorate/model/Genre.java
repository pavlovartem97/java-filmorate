package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
public class Genre {
    @EqualsAndHashCode.Include
    Integer id;

    @EqualsAndHashCode.Exclude
    String name;
}
