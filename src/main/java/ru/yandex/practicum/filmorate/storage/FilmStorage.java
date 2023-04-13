package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface FilmStorage {

    void addFilm(Film film);

    void updateFilm(Film film);

    void deleteFilm(int filmId);

    Collection<Film> findAll();

    Optional<Film> findFilmById(int id);

    void addFavourite(int filmId, int userId);

    void removeFavoutite(int filmId, int userId);

    Collection<Film> findTopFilms(Map<String, Object> filters);

    boolean contains(int filmId);

    Collection<Film> getRecommendations(int id);

    Collection<Film> getCommonFilms(int userId, int friendId);
}
