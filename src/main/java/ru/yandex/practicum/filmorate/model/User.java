package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private Integer id;

    private String name;

    @Email(message = "Почта задана в неверном формате")
    private String email;

    @NotBlank(message = "Логин не может быть пустым или содержать только пробелы")
    private String login;

    @Past(message = "некорректная дата рождения")
    private LocalDate birthday;
}
