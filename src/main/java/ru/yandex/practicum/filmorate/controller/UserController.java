package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private static int userId = 0;

    private final UserValidator userValidator;

    private final InMemoryUserStorage userStorage;

    @Autowired
    public UserController(UserValidator userValidator, InMemoryUserStorage inMemoryUserStorage) {
        this.userValidator = userValidator;
        this.userStorage = inMemoryUserStorage;
    }

    @GetMapping
    public List<User> getAllFilms() {
        log.info("User list getting: " + userStorage.getAllUsers());
        return new ArrayList<>(userStorage.getAllUsers());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userValidator.validate(user);
        user.setId(++userId);
        userStorage.addUser(user);
        log.info("New user creating: " + user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        userValidator.validate(user);
        userStorage.addUser(user);
        log.info("User updating: " + user);
        return user;
    }
}
