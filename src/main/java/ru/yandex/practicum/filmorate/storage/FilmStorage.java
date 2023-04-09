package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    void addFilm(Film film);

    void updateFilm(Film film);

    void deleteFilm(Film film);

    Collection<Film> findAll();

    Optional<Film> findFilmById(int id);

    void addFavourite(int filmId, int userId);

    void removeFavoutite(int filmId, int userId);

    Collection<Film> findTopFilms(int count);

    boolean contains(int filmId);
}
