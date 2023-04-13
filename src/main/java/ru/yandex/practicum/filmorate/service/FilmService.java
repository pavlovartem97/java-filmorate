package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private final FilmValidator filmValidator;

    public void addLike(int filmId, int userId) {
        checkFilmIdAndUserId(filmId, userId);
        filmStorage.addFavourite(filmId, userId);
        log.info("User " + userId + " likes film " + filmId);
    }

    public void removeLike(int filmId, int userId) {
        checkFilmIdAndUserId(filmId, userId);
        filmStorage.removeFavoutite(filmId, userId);
        log.info("User " + userId + " removes like from film " + filmId);
    }

    public Collection<Film> topFilms(Map<String, Object> filters) {
        Collection<Film> topFilms = filmStorage.findTopFilms(filters);
        log.info("Get top films " + topFilms);
        return topFilms;
    }

    public Collection<Film> getAllFilms() {
        Collection<Film> films = filmStorage.findAll();
        log.info("Film list getting " + films);
        return films;
    }

    public Film getFilmById(int filmId) {
        Film film = filmStorage.findFilmById(filmId)
                .orElseThrow(() -> {
                    throw new FilmNotFoundException("Film is not found: " + filmId);
                });
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
        checkFilm(film.getId());
        filmStorage.updateFilm(film);
        log.info("Film updating: " + film);
    }

    public void deleteFilm(int filmId) {
        checkFilm(filmId);
        filmStorage.deleteFilm(filmId);
        log.info("Film removed: " + filmId);
    }

    private void checkFilmIdAndUserId(int filmId, int userId) {
        if (!filmStorage.contains(filmId)) {
            throw new FilmNotFoundException("Film is not found: " + filmId);
        }
        if (!userStorage.contains(userId)) {
            throw new UserNotFoundException("User is not found: " + userId);
        }
    }

    private void checkFilm(int filmId) {
        if (!filmStorage.contains(filmId)) {
            throw new FilmNotFoundException("Film is not found: " + filmId);
        }
    }
}
