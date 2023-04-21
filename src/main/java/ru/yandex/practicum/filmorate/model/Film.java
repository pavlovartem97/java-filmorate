package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    Integer id;

    @NotBlank(message = "Film name cannot be blank")
    String name;

    @NotBlank(message = "Film description cannot be blank")
    @Size(min = 1, max = 200, message = "Description max length cannot be over 200 characters")
    String description;

    @NotNull(message = "Release date should be set")
    LocalDate releaseDate;

    @Positive
    Integer duration;

    final Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));

    Mpa mpa;

    final Set<Director> directors = new TreeSet<>(Comparator.comparing(Director::getId));
}
