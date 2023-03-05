package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private final FilmValidator filmValidator;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, FilmValidator filmValidator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmValidator = filmValidator;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        film.getLikeIds().add(userId);
        log.info("User " + userId + " likes film " + filmId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        film.getLikeIds().remove(userId);
        log.info("User " + userId + " removes like from film " + filmId);
    }

    public List<Film> topFilms(int count) {
        List<Film> topFilms = filmStorage.getAllFilm().stream()
                .sorted((film1, film2) -> film2.getLikeIds().size() - film1.getLikeIds().size())
                .limit(count)
                .collect(Collectors.toList());
        log.info("Get top films " + topFilms);
        return topFilms;
    }

    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>(filmStorage.getAllFilm());
        log.info("Film list getting " + films);
        return films;
    }

    public Film getFilmById(int filmId) {
        Film film = filmStorage.getFilmById(filmId);
        log.info("Get film by id " + film);
        return film;
    }

    public void addFilm(Film film) {
        filmValidator.validate(film);
        filmStorage.addFilm(film);
        log.info("New film creating: " + film);
    }


    public void updateFilm(Film film) {
        filmValidator.validate(film);
        filmStorage.updateFilm(film);
        log.info("Film updating: " + film);
    }
}
