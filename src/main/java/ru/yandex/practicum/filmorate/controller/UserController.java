package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;
import ru.yandex.practicum.filmorate.validator.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    private static int userId = 0;

    private final UserValidator userValidator;

    @Autowired
    public UserController(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @GetMapping
    public List<User> getAllFilms() {
        log.info("User list getting: " + users);
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userValidator.validate(user);
        user.setId(++userId);
        users.put(user.getId(), user);
        log.info("New user creating: " + user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        userValidator.validate(user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Can't find user with Id " + user.getId());
        }
        users.put(user.getId(), user);
        log.info("User updating: " + user);
        return user;
    }
}
