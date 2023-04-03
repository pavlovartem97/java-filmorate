package ru.yandex.practicum.filmorate.storage.InMemoryImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FavouriteStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class InMemoryFavouriteStorage implements FavouriteStorage {

    private final FilmStorage filmStorage;

    private final Map<Integer, List<Integer>> storage = new HashMap<>();

    @Override
    public void addFavourite(int filmId, int userId) {
        if (!storage.containsKey(filmId)) {
            storage.put(filmId, new ArrayList<>());
        }
        storage.get(filmId).add(userId);
    }

    @Override
    public void removeFavoutite(int filmId, int userId) {
        storage.get(filmId).remove(userId);
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
