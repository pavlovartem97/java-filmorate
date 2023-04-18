package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Operation;

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
                .eventType(mapEvent(rs.getString("event_type")))
                .operation(mapOperation(rs.getString("operation")))
                .entityId(rs.getInt("entity_id"))
                .build();
    }

    private Operation mapOperation(String operation) {
        switch (operation) {
            case "REMOVE":
                return Operation.REMOVE;
            case "ADD":
                return Operation.ADD;
            case "UPDATE":
                return Operation.UPDATE;
            default:
                return null;
        }
    }

    private EventType mapEvent(String eventStr) {
        switch (eventStr) {
            case "LIKE":
                return EventType.LIKE;
            case "REVIEW":
                return EventType.REVIEW;
            case "FRIEND":
                return EventType.FRIEND;
            default:
                return null;
        }
    }
}
