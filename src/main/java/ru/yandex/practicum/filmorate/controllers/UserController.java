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
        log.info("Возвращен список пользователей: " + users.toString());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        validate(user);
        user.setId(++userId);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь: " + user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) throws ValidationException {
        validate(user);
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

    private void validate(User user) throws ValidationException {
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            log.error("Логин не может быть пустым");
            throw new ValidationException("Логин не может быть пустым");
        }
        if (user.getLogin().contains(" ")) {
            log.error("Логин не может содержать пробелы");
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getEmail() == null) {
            log.error("Электронная почта не может быть пустой");
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Электронная почта должна содержать @");
            throw new ValidationException("Электронная почта должна содержать @");
        }
        if (user.getBirthday() == null) {
            log.error("Дата рождения должна быть задана");
            throw new ValidationException("Дата рождения должна быть задана");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.info("Пусоое имя было заменено на логин: " + user.getLogin());
        }
        log.info("Валидация успешно пройдена");
    }
}
