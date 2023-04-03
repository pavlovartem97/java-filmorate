package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserValidator userValidator;

    private final UserStorage userStorage;

    private final FriendStorage friendStorage;

    public void addFriend(int id, int friendId) {
        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(friendId);
        friendStorage.addFriend(user1.getId(), user2.getId());
        log.info("Put friend " + friendId + " for user " + id);
    }

    public void removeFriend(int id, int friendId) {
        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(friendId);
        friendStorage.removeFriend(user1.getId(), user2.getId());
        log.info("Delete friend " + friendId + " for user " + id);
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);
        List<User> commonFriends = friendStorage.getCommonFriends(user1.getId(), user2.getId());
        log.info("Get common friends for users " + userId1 + " " + userId2 + " : " + commonFriends);
        return commonFriends;
    }

    public List<User> getUserFriends(int userId) {
        User user = userStorage.getUserById(userId);
        List<User> friends = friendStorage.getFriends(user.getId());
        log.info("Get friend for user " + userId);
        return friends;
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>(userStorage.getAllUsers());
        log.info("User list getting: " + users);
        return users;
    }

    public User getUserById(int id) {
        User user = userStorage.getUserById(id);
        log.info("Get user by id: " + user);
        return user;
    }

    public void addUser(User user) {
        userValidator.validate(user);
        userStorage.addUser(user);
        log.info("New user creating: " + user);
    }

    public void updateUser(User user) {
        userValidator.validate(user);
        userStorage.updateUser(user);
        log.info("User updating: " + user);
    }

}
