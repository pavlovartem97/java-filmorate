package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

interface FilmStorage {

    void addFilm(Film film);

    void updateFilm(Film film);

    void deleteFilm(Film film);

    Collection<Film> getAllFilm();

    Film getFilmById(int id);
}
