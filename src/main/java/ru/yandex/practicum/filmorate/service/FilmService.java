package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DirectorNotFoundException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UnacceptableQueryException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enumerate.EventType;
import ru.yandex.practicum.filmorate.model.enumerate.OperationType;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmService {

    FilmStorage filmStorage;

    UserStorage userStorage;

    DirectorStorage directorStorage;

    FeedStorage feedDbStorage;

    FilmValidator filmValidator;

    public void addLike(int filmId, int userId) {
        checkFilmIdAndUserId(filmId, userId);
        filmStorage.addFavourite(filmId, userId);
        feedDbStorage.addFeed(userId, filmId, OperationType.ADD, EventType.LIKE);
        log.info("User {} likes film {}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        checkFilmIdAndUserId(filmId, userId);
        filmStorage.removeFavoutite(filmId, userId);
        feedDbStorage.addFeed(userId, filmId, OperationType.REMOVE, EventType.LIKE);
        log.info("User {} removes like from film {}", userId, filmId);
    }

    public Collection<Film> topFilms(Map<String, Object> filters) {
        Collection<Film> topFilms = filmStorage.findTopFilms(filters);
        log.info("Got top {} films", topFilms.size());
        return topFilms;
    }

    public Collection<Film> getAllFilms() {
        Collection<Film> films = filmStorage.findAll();
        log.info("Film list getting {}", films);
        return films;
    }

    public Film getFilmById(int filmId) {
        Film film = filmStorage.findFilmById(filmId)
                .orElseThrow(() -> {
                    throw new FilmNotFoundException(String.format("Film is not found: %d", filmId));
                });
        log.info("Get film by id " + film);
        return film;
    }

    public void addFilm(Film film) {
        filmValidator.validate(film);
        filmStorage.addFilm(film);
        log.info("New film creating: {}", film);
    }

    public void updateFilm(Film film) {
        filmValidator.validate(film);
        checkFilm(film.getId());
        filmStorage.updateFilm(film);
        log.info("Film updating: {}", film);
    }

    public void deleteFilm(int filmId) {
        checkFilm(filmId);
        filmStorage.deleteFilm(filmId);
        log.info("Film removed: {}", filmId);
    }


    public Collection<Film> getFilmsOfDirector(int directorID, String sortBy) {
        checkDirector(directorID);
        Collection<Film> films = filmStorage.getFilmsOfDirector(directorID, sortBy);
        log.info("Get list of director films, his size {}", films.size());
        return films;
    }

    public Collection<Film> getCommonFilms(int userId, int friendId) {
        checkUser(userId);
        checkUser(friendId);

        Collection<Film> commonFilms = filmStorage.getCommonFilms(userId, friendId);
        log.info("Got {}} common films", commonFilms.size());

        return commonFilms;
    }

    public Collection<Film> searchFilms(String query, List<String> by) {
        checkByQuery(by);
        return filmStorage.searchFilms(query, by);
    }

    private void checkFilmIdAndUserId(int filmId, int userId) {
        if (!filmStorage.contains(filmId)) {
            throw new FilmNotFoundException(String.format("Film is not found: %d", filmId));
        }
        if (!userStorage.contains(userId)) {
            throw new UserNotFoundException(String.format("User is not found: %d", userId));
        }
    }

    private void checkFilm(int filmId) {
        if (!filmStorage.contains(filmId)) {
            throw new FilmNotFoundException(String.format("Film is not found: %d", filmId));
        }
    }

    private void checkDirector(int directorId) {
        if (!directorStorage.contains(directorId)) {
            throw new DirectorNotFoundException(String.format("Director with ID %d not found", directorId));
        }
    }

    private void checkUser(int userId) {
        if (!userStorage.contains(userId)) {
            throw new UserNotFoundException(String.format("User is not found: %d", userId));
        }
    }

    private void checkByQuery(List<String> by) {
        if (by.isEmpty()) {
            throw new UnacceptableQueryException("Please enter the 'by' search parameter. It cannot be empty.Must be" +
                    " either 'director', or 'title'. Or you can search by both 'director' and 'title'.");
        } else if (by.size() > 2) {
            throw new UnacceptableQueryException("You can search by a max of two parameters: - director; - title.");
        }
    }

}
