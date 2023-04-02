package ru.yandex.practicum.filmorate.storage.favourite;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FavouriteStorage {

    void addFavourite(int filmId, int userId);

    void removeFavoutite(int filmId, int userId);

    List<Film> getTopFilm(int count);
}
