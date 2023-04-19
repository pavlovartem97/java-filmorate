package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FeedMapper;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

@Component
@AllArgsConstructor
public class FeedDbStorage implements FeedStorage {

    private final JdbcTemplate jdbcTemplate;

    private final FeedMapper feedMapper;


    public int insertFeed(Feed feed) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO feed (user_id, timestamp, event_type, operation, entity_id)" +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, feed.getUserId());
            preparedStatement.setLong(2, feed.getTimestamp());
            preparedStatement.setString(3, feed.getEventType().name());
            preparedStatement.setString(4, feed.getOperation().name());
            preparedStatement.setInt(5, feed.getEntityId());
            return preparedStatement;
        }, keyHolder);
        feed.setEventId(keyHolder.getKey().intValue());
        return keyHolder.getKey().intValue();
    }

    public Collection<Feed> getFeed(int userId) {
        String sql = "SELECT * FROM feed WHERE user_id = ?";
        return jdbcTemplate.query(sql, feedMapper, userId);
    }
}
