package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private Integer id;

    @NotBlank(message = "Название фильма не может быть пустым или содежрать только пробелы")
    private String name;

    @NotBlank(message = "Описание фильма не может быть пустым или содержать только пробелы")
    @Size(min = 1, max = 200, message = "Максимальная длина описания не может превышать 200 символов")
    private String description;

    @Past(message = "Дата релиза должна быть больше указанной величины")
    private LocalDate releaseDate;

    @Positive
    private Integer duration;
}
