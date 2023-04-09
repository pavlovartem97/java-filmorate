package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    User addUser(User user);

    void updateUser(User user);

    void deleteUser(User user);

    Collection<User> getAll();

    Optional<User> findUserById(int id);

    void addFriend(int userId, int friendId);

    void removeFriend(int userId, int friendId);

    Collection<User> getCommonFriends(int userId1, int userId2);

    Collection<User> findFriendsById(int userId);

    boolean contains(int userId);
}
