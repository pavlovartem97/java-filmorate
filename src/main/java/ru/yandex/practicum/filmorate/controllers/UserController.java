package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    Map<Integer, User> films = new HashMap<>();

    @GetMapping
    public List<User> getAllFilms(){
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public User create(@RequestBody User user){
        films.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user){
        if (films.containsKey(user.getId())) {
            films.put(user.getId(), user);
        }
        return user;
    }
}
