package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;

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
