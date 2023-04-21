package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FeedMapper;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.enumerate.EventType;
import ru.yandex.practicum.filmorate.model.enumerate.OperationType;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Instant;
import java.util.Collection;

@Component
@AllArgsConstructor
public class FeedDbStorage implements FeedStorage {

    private final JdbcTemplate jdbcTemplate;

    private final FeedMapper feedMapper;


    private int insertFeed(Feed feed) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO feed (user_id, timestamp, event_type, operation, entity_id)" +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, feed.getUserId());
            preparedStatement.setLong(2, feed.getTimestamp());
            preparedStatement.setString(3, feed.getEventType().getType());
            preparedStatement.setString(4, feed.getOperationType().getType());
            preparedStatement.setInt(5, feed.getEntityId());
            return preparedStatement;
        }, keyHolder);
        feed.setEventId(keyHolder.getKey().intValue());
        return keyHolder.getKey().intValue();
    }

    @Override
    public Collection<Feed> findFeedByUserId(int userId) {
        String sql = "SELECT * FROM feed WHERE user_id = ?";
        return jdbcTemplate.query(sql, feedMapper, userId);
    }

    @Override
    public int addFeed(int userId, int entityId, OperationType operationType, EventType eventType) {
        Feed feed = Feed.builder()
                .userId(userId)
                .timestamp(Instant.now().toEpochMilli())
                .eventType(eventType)
                .operationType(operationType)
                .entityId(entityId)
                .build();
        int eventId = insertFeed(feed);
        feed.setEventId(eventId);
        return eventId;
    }
}
