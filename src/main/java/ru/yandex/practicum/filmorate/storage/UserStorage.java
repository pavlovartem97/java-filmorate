package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

interface UserStorage {

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(User user);

    Collection<User> getAllUsers();

    User getUserById(int Id);
}
