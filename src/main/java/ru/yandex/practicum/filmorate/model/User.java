package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Integer id;

    String name;

    @Email(message = "Email is in wrong format")
    String email;

    @NotEmpty(message = "Login cannot be empty")
    @Pattern(regexp = "\\S+", message = "Login cannot contain spaces")
    String login;

    @Past(message = "Birthday is in the wrong format")
    LocalDate birthday;
}
