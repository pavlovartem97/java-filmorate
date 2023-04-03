package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FavouriteStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private final FilmValidator filmValidator;

    private final FavouriteStorage favouriteStorage;

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        favouriteStorage.addFavourite(film.getId(), user.getId());
        log.info("User " + userId + " likes film " + filmId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        favouriteStorage.removeFavoutite(film.getId(), user.getId());
        log.info("User " + userId + " removes like from film " + filmId);
    }

    public List<Film> topFilms(int count) {
        List<Film> topFilms = favouriteStorage.getTopFilm(count);
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
