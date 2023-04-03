package ru.yandex.practicum.filmorate.storage.InMemoryImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.DbImlp.UserDbStorage;

import java.util.*;

@Component
@AllArgsConstructor
public class InMemoryFriendStorage implements FriendStorage {

    private final Map<User, List<User>> friendStorage = new HashMap<>();

    private final UserDbStorage userDbStorage;

    @Override
    public void addFriend(int userId1, int userId2) {
        User user1 = userDbStorage.getUserById(userId1);
        User user2 = userDbStorage.getUserById(userId1);
        friendStorage.get(user1).add(user2);
        friendStorage.get(user2).add(user1);
    }

    @Override
    public void removeFriend(int userId1, int userId2) {
        User user1 = userDbStorage.getUserById(userId1);
        User user2 = userDbStorage.getUserById(userId1);
        friendStorage.get(user1).remove(user2);
        friendStorage.get(user2).remove(user1);
    }

    @Override
    public List<User> getCommonFriends(int userId1, int userId2) {
        User user1 = userDbStorage.getUserById(userId1);
        User user2 = userDbStorage.getUserById(userId1);
        Set<User> intersection = new HashSet<>(friendStorage.get(user1));
        intersection.retainAll(friendStorage.get(user2));
        return new ArrayList<>(intersection);
    }

    @Override
    public List<User> getFriends(int userId1) {
        User user1 = userDbStorage.getUserById(userId1);
        return friendStorage.get(user1);
    }
}
