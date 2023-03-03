package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmValidator filmValidator;

    private final InMemoryFilmStorage filmStorage;

    private static int filmId;

    @Autowired
    public FilmController(FilmValidator filmValidator, InMemoryFilmStorage filmStorage) {
        this.filmValidator = filmValidator;
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Film list getting " + filmStorage.getAllFilm());
        return new ArrayList<>(filmStorage.getAllFilm());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        filmValidator.validate(film);
        film.setId(++filmId);
        filmStorage.addFilm(film);
        log.info("New film creating: " + film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        filmValidator.validate(film);
        filmStorage.updateFilm(film);
        log.info("Film updating: " + film);
        return film;
    }
}
