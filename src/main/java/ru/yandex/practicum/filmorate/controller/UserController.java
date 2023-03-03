package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private static int userId = 0;

    private final UserValidator userValidator;

    private final UserStorage userStorage;

    private final UserService userService;

    @Autowired
    public UserController(UserValidator userValidator, UserStorage userStorage, UserService userService) {
        this.userValidator = userValidator;
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllFilms() {
        List<User> users = new ArrayList<>(userStorage.getAllUsers());
        log.info("User list getting: " + users);
        return users;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        User user = userStorage.getUserById(id);
        log.info("Get user by id: " + user);
        return user;
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
        userStorage.updateUser(user);
        log.info("User updating: " + user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> friendId) {
        if (id.isEmpty() || friendId.isEmpty()) {
            throw new RuntimeException("Path variable should contain id and friendId");
        }
        userService.addFriend(id.get(), friendId.get());
        log.info("Put friend " + friendId.get() + " for user " + id.get());
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> friendId) {
        if (id.isEmpty() || friendId.isEmpty()) {
            throw new RuntimeException("Path variable should contain id and friendId");
        }
        userService.removeFriend(id.get(), friendId.get());
        log.info("Delete friend " + friendId.get() + " for user " + id.get());
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable Optional<Integer> id) {
        if (id.isEmpty()) {
            throw new RuntimeException("Path variable should contain id");
        }
        List<User> userFriends = userService.getUserFriends(id.get());
        log.info("Get friend for user " + id.get());
        return userFriends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Optional<Integer> id, @PathVariable Optional<Integer> otherId) {
        if (id.isEmpty() || otherId.isEmpty()) {
            throw new RuntimeException("Path variable should contain id and otherId");
        }
        List<User> commonFriends = userService.getCommonFriends(id.get(), otherId.get());
        log.info("Get common friends for users " + id + " " + otherId + " : " + commonFriends);
        return commonFriends;
    }
}
