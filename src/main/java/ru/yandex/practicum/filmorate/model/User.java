package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {
    private Integer id;

    private String name;

    @Email(message = "Email is in wrong format")
    private String email;

    @NotEmpty(message = "Login cannot be empty")
    @Pattern(regexp = "\\S+", message = "Login cannot contain spaces")
    private String login;

    @Past(message = "Birthday is in the wrong format")
    private LocalDate birthday;
}
