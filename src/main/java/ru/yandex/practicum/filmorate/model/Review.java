package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class Review {

    private Integer reviewId;

    @NotBlank
    private String content;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer filmId;

    @NotNull
    private Boolean isPositive;

    private Integer useful;
}
