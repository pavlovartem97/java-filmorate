package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;
import ru.yandex.practicum.filmorate.validators.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    Map<Integer, Film> films = new HashMap<>();

    private static int filmId = 0;

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Возвращен список фильмов " + films.toString());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        FilmValidator.validate(film);
        film.setId(++filmId);
        films.put(film.getId(), film);
        log.info("Создан новый фильм: " + film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        FilmValidator.validate(film);
        if (!films.containsKey(film.getId())) {
            log.error("Не найдено Id фильма " + film.getId());
            throw new ValidationException("Не найдено Id фильма " + film.getId());
        }
        films.put(film.getId(), film);
        log.info("Фильм успешно обновлен: " + film);
        return film;
    }
}
