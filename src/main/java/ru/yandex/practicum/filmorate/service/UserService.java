package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId1, int userId2) {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);
        user1.getFriendsId().add(user2.getId());
        user2.getFriendsId().add(user1.getId());
    }

    public void removeFriend(int userId1, int userId2) {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);
        user1.getFriendsId().remove(user2.getId());
        user2.getFriendsId().remove(user1.getId());
    }

    public List<User> getCommonFriends(int userId1, int userId2) {
        User user1 = userStorage.getUserById(userId1);
        User user2 = userStorage.getUserById(userId2);
        Set<Integer> intersection = new HashSet<>(user1.getFriendsId());
        intersection.retainAll(user2.getFriendsId());
        return intersection.stream().map(id -> userStorage.getUserById(id)).collect(Collectors.toList());
    }

    public List<User> getUserFriends(int userId) {
        return userStorage.getUserById(userId).getFriendsId().stream()
                .map(id -> userStorage.getUserById(id))
                .collect(Collectors.toList());
    }
}
