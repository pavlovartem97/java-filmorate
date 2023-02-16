package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    Map<Integer, Film> films = new HashMap<>();

    private static int uresId = 0;

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        validate(film);
        film.setId(++uresId);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) throws ValidationException {
        validate(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Не найдено Id фильма " + film.getId());
        }
        films.put(film.getId(), film);
        return film;
    }

    private void validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() == null) {
            throw new ValidationException("Описание фильма не может быть не задано");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == null) {
            throw new ValidationException("Длительность фильма не может быть не задана");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Длительность фильма не может быть не задана");
        }
    }
}
