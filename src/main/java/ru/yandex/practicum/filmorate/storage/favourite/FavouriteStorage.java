package ru.yandex.practicum.filmorate.storage.favourite;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FavouriteStorage {

    void addFavourite(Film film, User user);

    void removeFavoutite(Film film, User user);

    List<Film> getTopFilm(int count);
}
