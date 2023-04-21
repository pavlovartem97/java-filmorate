package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {

    Integer reviewId;

    @NotBlank
    String content;

    @NotNull
    Integer userId;

    @NotNull
    Integer filmId;

    @NotNull
    Boolean isPositive;

    Integer useful;
}
