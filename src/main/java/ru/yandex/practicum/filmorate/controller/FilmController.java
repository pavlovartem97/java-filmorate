package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmValidator filmValidator;

    private final FilmStorage filmStorage;

    private final FilmService filmService;

    private static int filmId;

    @Autowired
    public FilmController(FilmValidator filmValidator, FilmStorage filmStorage, FilmService filmService) {
        this.filmValidator = filmValidator;
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Film list getting " + filmStorage.getAllFilm());
        return new ArrayList<>(filmStorage.getAllFilm());
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        Film film = filmStorage.getFilmById(id);
        log.info("Get film by id " + film);
        return film;
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

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> userId) {
        if (!id.isPresent() || !userId.isPresent()) {
            throw new RuntimeException("");
        }
        filmService.addLike(id.get(), userId.get());
        log.info("User " + userId + " likes film " + id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> userId) {
        if (!id.isPresent() || !userId.isPresent()) {
            throw new RuntimeException("");
        }
        filmService.addLike(id.get(), userId.get());
        log.info("User " + userId + " removes like from film " + id);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") Integer count) {
        List<Film> topFilms = filmService.topFilms(count);
        log.info("Get top films " + topFilms);
        return topFilms;
    }
}
