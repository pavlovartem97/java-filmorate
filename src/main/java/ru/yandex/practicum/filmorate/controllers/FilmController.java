package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAllFilms(){
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film){
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film){
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        }
        return film;
    }
}
