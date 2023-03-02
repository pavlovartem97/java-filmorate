package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmValidationTests {

    private Validator validator;

    private static final LocalDate releaseDate = LocalDate.of(1980, 10, 10);

    @Autowired
    private FilmValidator filmValidator;

    @BeforeEach
    void beforeEach() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    void filmCreateTest() {
        Film film = Film.builder()
                .description("description").duration(100).name("Film name").releaseDate(releaseDate).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());

        assertDoesNotThrow(() -> filmValidator.validate(film));
    }

    @Test
    void filmCreateFailNameTest() {
        Film film = Film.builder().description("desc").duration(100).releaseDate(releaseDate).build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertDoesNotThrow(() -> filmValidator.validate(film));
    }

    @Test
    void filmCreateFailDescriptionTest() {
        Film film = Film.builder().name("fileName").duration(100).releaseDate(releaseDate)
                .description("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                        "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, а именно " +
                        "20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.")
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertDoesNotThrow(() -> filmValidator.validate(film));
    }

    @Test
    void filmCreateFailReleaseDateTest() {
        Film film = Film.builder().name("fileName").duration(100).releaseDate(LocalDate.of(1890, 03, 25))
                .description("Film description")
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());

        assertThrows(ValidationException.class, () -> filmValidator.validate(film));
    }

    @Test
    void filmCreateFailDurationTest() {
        Film film = Film.builder().name("fileName").duration(-200).releaseDate(releaseDate)
                .description("Film description")
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertDoesNotThrow(() -> filmValidator.validate(film));
    }
}
