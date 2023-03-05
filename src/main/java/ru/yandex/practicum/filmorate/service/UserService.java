package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserValidator userValidator;

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage, UserValidator userValidator) {
        this.userStorage = userStorage;
        this.userValidator = userValidator;
    }

    public void addFriend(int id, int friendId) {
        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(friendId);
        user1.getFriendIds().add(user2.getId());
        user2.getFriendIds().add(user1.getId());
        log.info("Put friend " + friendId + " for user " + id);
    }

    public void removeFriend(int id, int friendId) {
        User user1 = userStorage.getUserById(id);
        User user2 = userStorage.getUserById(friendId);
        user1.getFriendIds().remove(user2.getId());
        user2.getFriendIds().remove(user1.getId());
        log.info("Delete friend " + friendId + " for user " + id);
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);
        Set<Integer> intersection = new HashSet<>(user1.getFriendIds());
        intersection.retainAll(user2.getFriendIds());
        List<User> commonFriends = intersection.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
        log.info("Get common friends for users " + userId1 + " " + userId2 + " : " + commonFriends);
        return commonFriends;
    }

    public List<User> getUserFriends(int userId) {
        List<User> friends = userStorage.getUserById(userId).getFriendIds().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
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
