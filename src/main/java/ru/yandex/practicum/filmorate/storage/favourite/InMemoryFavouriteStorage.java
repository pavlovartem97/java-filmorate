package ru.yandex.practicum.filmorate.storage.favourite;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InMemoryFavouriteStorage implements FavouriteStorage {

    private final FilmStorage filmStorage;

    private final Map<Film, List<User>> storage = new HashMap<>();

    @Override
    public void addFavourite(Film film, User user) {
        if (!storage.containsKey(film)) {
            storage.put(film, new ArrayList<>());
        }
        storage.get(film).add(user);
    }

    @Override
    public void removeFavoutite(Film film, User user) {
        storage.get(film).remove(user);
    }

    @Override
    public List<Film> getTopFilm(int count) {
        List<Film> topFilms = filmStorage.getAllFilm().stream()
                .sorted(Comparator.comparing(f -> storage.get(f).size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
        return topFilms;
    }
}
