package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enumerate.EventType;
import ru.yandex.practicum.filmorate.model.enumerate.OperationType;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserValidator userValidator;

    private final UserStorage userStorage;

    private final FilmStorage filmStorage;

    private final FeedStorage feedDbStorage;

    public void addFriend(int id, int friendId) {
        checkUsers(id, friendId);
        int checkFriend = userStorage.addFriend(id, friendId);
        if (checkFriend != 0) {
            feedDbStorage.addFeed(friendId, id, OperationType.ADD, EventType.FRIEND);
        } else {
            feedDbStorage.addFeed(id, friendId, OperationType.ADD, EventType.FRIEND);
        }
        log.info("Put friend " + friendId + " for user " + id);
    }

    public void removeFriend(int id, int friendId) {
        checkUsers(id, friendId);
        userStorage.removeFriend(id, friendId);
        feedDbStorage.addFeed(id, friendId, OperationType.REMOVE, EventType.FRIEND);
        log.info("Delete friend " + friendId + " for user " + id);
    }

    public Collection<User> getCommonFriends(int userId1, int userId2) {
        checkUsers(userId1, userId2);
        Collection<User> commonFriends = userStorage.getCommonFriends(userId1, userId2);
        log.info("Get common friends for users " + userId1 + " " + userId2 + " : " + commonFriends);
        return commonFriends;
    }

    public Collection<User> getUserFriends(int userId) {
        checkUser(userId);
        Collection<User> friends = userStorage.findFriendsById(userId);
        log.info("Get friend for user " + userId);
        return friends;
    }

    public Collection<User> getAllUsers() {
        Collection<User> users = userStorage.getAll();
        log.info("User list getting: " + users);
        return users;
    }

    public User getUserById(int id) {
        User user = userStorage.findUserById(id)
                .orElseThrow(() -> {
                    throw new UserNotFoundException("User is not found: " + id);
                });
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
        checkUser(user.getId());
        userStorage.updateUser(user);
        log.info("User updating: " + user);
    }

    public void deleteUser(int userId) {
        checkUser(userId);
        userStorage.deleteUser(userId);
        log.info("User deleted: " + userId);
    }

    public Collection<Film> getRecommendations(int id) {
        checkUser(id);
        Collection<Film> recommendationFilms = filmStorage.getRecommendations(id);
        log.info("Got " + recommendationFilms.size() + " recommended films");
        return recommendationFilms;
    }

    public Collection<Feed> getFeed(int id) {
        checkUser(id);
        Collection<Feed> feed = feedDbStorage.findFeedByUserId(id);
        log.info("Got" + feed.size() + " feeds objects");
        return feed;
    }

    private void checkUser(int userId) {
        if (!userStorage.contains(userId)) {
            throw new UserNotFoundException("User is not found: " + userId);
        }
    }

    private void checkUsers(int userId1, int userId2) {
        if (!userStorage.contains(userId1)) {
            throw new UserNotFoundException("User is not found: " + userId1);
        }
        if (!userStorage.contains(userId2)) {
            throw new UserNotFoundException("User is not found: " + userId2);
        }
    }

}
