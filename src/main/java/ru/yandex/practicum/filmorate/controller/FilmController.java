package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmValidator filmValidator;

    private final Map<Integer, Film> films = new HashMap<>();

    private static int filmId;

    @Autowired
    public FilmController(FilmValidator filmValidator) {
        this.filmValidator = filmValidator;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Film list getting " + films.toString());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        filmValidator.validate(film);
        film.setId(++filmId);
        films.put(film.getId(), film);
        log.info("New film creating: " + film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        filmValidator.validate(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Can't find film with Id " + film.getId());
        }
        films.put(film.getId(), film);
        log.info("Film updating: " + film);
        return film;
    }
}
