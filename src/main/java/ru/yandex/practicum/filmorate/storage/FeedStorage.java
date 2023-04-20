package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.enumerate.EventType;
import ru.yandex.practicum.filmorate.model.enumerate.OperationType;

import java.util.Collection;

public interface FeedStorage {


    Collection<Feed> findFeedByUserId(int userId);

    int addFeed(int userId, int filmId, OperationType operationType, EventType eventType);
}
