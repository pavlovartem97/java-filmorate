package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User addUser(User user);

    void updateUser(User user);

    void deleteUser(User user);

    Collection<User> getAllUsers();

    User getUserById(int id);
}
