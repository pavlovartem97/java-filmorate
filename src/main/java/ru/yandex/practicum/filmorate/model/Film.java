package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
public class Film {

    private Integer id;

    @NotBlank(message = "Film name cannot be blank")
    private String name;

    @NotBlank(message = "Film description cannot be blank")
    @Size(min = 1, max = 200, message = "Description max length cannot be over 200 characters")
    private String description;

    @NotNull(message = "Release date should be set")
    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    private final Set<Genre> genres = new TreeSet<>(Comparator.comparing(f -> f.getId()));

    private Mpa mpa;
}
