package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FavouriteStorage {

    void addFavourite(int filmId, int userId);

    void removeFavoutite(int filmId, int userId);

    List<Film> getTopFilm(int count);
}
