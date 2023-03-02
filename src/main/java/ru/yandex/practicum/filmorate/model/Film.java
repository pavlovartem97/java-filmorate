package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
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
}
