package ru.yandex.practicum.filmorate.storage.fried;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryFriendStorage implements FriendStorage {

    private final Map<User, List<User>> friendStorage = new HashMap<>();

    @Override
    public void addFriend(User user1, User user2) {
        friendStorage.get(user1).add(user2);
        friendStorage.get(user2).add(user1);
    }

    @Override
    public void removeFriend(User user1, User user2) {
        friendStorage.get(user1).remove(user2);
        friendStorage.get(user2).remove(user1);
    }

    @Override
    public List<User> getCommonFriends(User user1, User user2) {
        Set<User> intersection = new HashSet<>(friendStorage.get(user1));
        intersection.retainAll(friendStorage.get(user1));
        return new ArrayList<>(intersection);
    }

    @Override
    public List<User> getFriends(User user1) {
        return friendStorage.get(user1);
    }
}
