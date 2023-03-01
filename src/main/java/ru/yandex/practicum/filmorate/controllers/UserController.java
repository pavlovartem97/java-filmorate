package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;
import ru.yandex.practicum.filmorate.validators.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    Map<Integer, User> users = new HashMap<>();

    private static int userId = 0;

    @GetMapping
    public List<User> getAllFilms() {
        log.info("Возвращен список пользователей: " + users.toString());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        UserValidator.validate(user);
        user.setId(++userId);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: " + user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        UserValidator.validate(user);
        if (user.getId() == null) {
            log.error("Не задан Id пользователя");
            throw new ValidationException("Не задан Id пользователя");
        }
        if (!users.containsKey(user.getId())) {
            log.error("Не найдено пользователя с Id " + user.getId());
            throw new ValidationException("Не найдено пользователя с Id " + user.getId());
        }
        users.put(user.getId(), user);
        log.info("Обновлены данные пользователя: " + user);
        return user;
    }
}
