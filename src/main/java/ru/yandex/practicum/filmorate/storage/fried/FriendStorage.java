package ru.yandex.practicum.filmorate.storage.fried;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    void addFriend(User user1, User user2);

    void removeFriend(User user1, User user2);

    List<User> getCommonFriends(User user1, User user2);

    List<User> getFriends(User user1);
}
