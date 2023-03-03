package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFountException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new UserNotFountException("Film with Id " + user.getId() + " not found");
        }
    }

    @Override
    public void deleteUser(User user) {
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
        } else {
            throw new UserNotFountException("Film with Id " + user.getId() + " not found");
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(int userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        }
        throw new UserNotFountException("Film with Id " + userId + " not found");
    }
}
