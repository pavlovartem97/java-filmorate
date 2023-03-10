package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private static int userId;

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        user.setId(++userId);
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Film with Id " + user.getId() + " not found");
        }
        users.put(user.getId(), user);
    }

    @Override
    public void deleteUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException("Film with Id " + user.getId() + " not found");
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
            throw new UserNotFoundException("Film with Id " + userId + " not found");
        }
        return users.get(userId);
    }
}
