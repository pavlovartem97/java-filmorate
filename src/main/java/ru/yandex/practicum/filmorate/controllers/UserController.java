package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;
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
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        validate(user);
        user.setId(++userId);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        validate(user);
        if (user.getId() == null) {
            throw new ValidationException("Не задан Id пользователя");
        }
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Не найдено пользователя с Id " + user.getId());
        }
        users.put(user.getId(), user);
        return user;
    }

    private void validate(User user) throws ValidationException {
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getEmail() == null || user.getLogin().isEmpty()) {
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта должна содержать @");
        }
        if (user.getBirthday() == null) {
            throw new ValidationException("Дата рождения должна быть задана");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
