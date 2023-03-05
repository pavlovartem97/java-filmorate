package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static int filmId;

    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getAllFilm() {
        return films.values();
    }

    @Override
    public void addFilm(Film film) {
        film.setId(++filmId);
        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Film with Id " + film.getId() + " not found");
        }
        films.put(film.getId(), film);
    }

    @Override
    public void deleteFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Film with Id " + film.getId() + " not found");
        }
        films.remove(film.getId());
    }

    @Override
    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Film with Id " + id + " not found");
        }
        return films.get(id);
    }
}
