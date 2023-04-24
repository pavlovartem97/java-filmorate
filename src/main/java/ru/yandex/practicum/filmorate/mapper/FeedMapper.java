package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.enumerate.EventType;
import ru.yandex.practicum.filmorate.model.enumerate.OperationType;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FeedMapper implements RowMapper<Feed> {
    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Feed.builder()
                .eventId(rs.getInt("event_id"))
                .userId(rs.getInt("user_id"))
                .timestamp(rs.getLong("timestamp"))
                .eventType(EventType.getByType(rs.getString("event_type")))
                .operationType(OperationType.getByType(rs.getString("operation")))
                .entityId(rs.getInt("entity_id"))
                .build();
    }
}
