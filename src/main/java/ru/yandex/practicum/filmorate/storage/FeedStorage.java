package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.Collection;

public interface FeedStorage {

    int insertFeed(Feed feed);

    Collection<Feed> getFeed(int userId);
}
