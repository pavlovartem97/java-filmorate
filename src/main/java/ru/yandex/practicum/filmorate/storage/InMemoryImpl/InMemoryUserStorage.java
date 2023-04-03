package ru.yandex.practicum.filmorate.storage.InMemoryImpl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private static int userId;

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        user.setId(++userId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("User with Id " + user.getId() + " is not found");
        }
        users.put(user.getId(), user);
    }

    @Override
    public void deleteUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("User with Id " + user.getId() + " is not found");
        }
        users.remove(user.getId());
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(int userId) {
        if (!users.containsKey(userId)) {
            throw new UserNotFoundException("User with Id " + userId + " is not found");
        }
        return users.get(userId);
    }
}
