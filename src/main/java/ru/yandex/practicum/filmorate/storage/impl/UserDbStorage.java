package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

@Component
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    private final UserMapper userMapper;


    private final FeedDbStorage feedDbStorage;

    @Override
    public User addUser(User user) {
        int id = insertUser(user);
        user.setId(id);
        return user;
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE filmorate_user " +
                "SET email = ?,  name = ?, login = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getName(), user.getLogin(), user.getBirthday(), user.getId());
    }

    @Override
    public void deleteUser(int userId) {
        String sql = "DELETE FROM filmorate_user WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public Collection<User> getAll() {
        String sql = "SELECT * FROM filmorate_user";
        return jdbcTemplate.query(sql, userMapper);
    }

    @Override
    public Optional<User> findUserById(int id) {
        String sql = "SELECT * FROM filmorate_user WHERE user_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, userMapper, id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sql = "SELECT FROM friend WHERE friend_id = ? AND user_id = ?";
        int checkFriend = jdbcTemplate.query(sql, (rs, rowNum) -> 1, userId, friendId).size();

        if (checkFriend != 0) {
            sql = "INSERT INTO friend (friend_id, user_id, status) VALUES ( ?, ?, TRUE )";
            jdbcTemplate.update(sql, friendId, userId);

            sql = "UPDATE friend SET status = TRUE " +
                    "WHERE friend_id = ? AND user_id = ?";
            jdbcTemplate.update(sql, userId, friendId);

            Feed feed = Feed.builder()
                    .userId(friendId)
                    .timestamp(Instant.now().toEpochMilli())
                    .eventType(EventType.FRIEND)
                    .operation(Operation.ADD)
                    .entityId(userId)
                    .build();

            feedDbStorage.insertFeed(feed);
        } else {
            sql = "INSERT INTO friend (friend_id, user_id, status) VALUES ( ?, ?, FALSE )";
            jdbcTemplate.update(sql, friendId, userId);

            Feed feed = Feed.builder()
                    .userId(userId)
                    .timestamp(Instant.now().toEpochMilli())
                    .eventType(EventType.FRIEND)
                    .operation(Operation.ADD)
                    .entityId(friendId)
                    .build();

            feedDbStorage.insertFeed(feed);
        }
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sql = "DELETE FROM friend WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);

        Feed feed = Feed.builder()
                .userId(userId)
                .timestamp(Instant.now().toEpochMilli())
                .eventType(EventType.FRIEND)
                .operation(Operation.REMOVE)
                .entityId(friendId)
                .build();

        feedDbStorage.insertFeed(feed);

        sql = "UPDATE friend SET status = FALSE " +
                "WHERE friend_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, userId, friendId);

//        feed = Feed.builder()
//                .userId(friendId)
//                .timestamp(Instant.now().toEpochMilli())
//                .eventType(EventType.FRIEND)
//                .operation(Operation.UPDATE)
//                .entityId(userId)
//                .build();
//
//        feedDbStorage.insertFeed(feed);
    }

    @Override
    public Collection<User> getCommonFriends(int userId1, int userId2) {
        String sql =
                "SELECT * " +
                        "FROM filmorate_user " +
                        "WHERE user_id IN (SELECT friend_id " +
                        "        FROM friend " +
                        "        WHERE user_id = ? " +
                        "        AND NOT friend_id = ? " +
                        "        AND friend_id IN (SELECT friend_id " +
                        "                FROM friend " +
                        "                WHERE user_id = ?)) ";
        return jdbcTemplate.query(sql, userMapper, userId1, userId2, userId1);
    }

    @Override
    public Collection<Feed> getFeed(int userId) {
        return feedDbStorage.getFeed(userId);
    }

    @Override
    public Collection<User> findFriendsById(int userId) {
        String sql =
                "SELECT * " +
                        "FROM filmorate_user " +
                        "WHERE user_id IN (SELECT friend_id " +
                        "        FROM friend " +
                        "        WHERE user_id = ?)";
        return jdbcTemplate.query(sql, userMapper, userId);
    }

    @Override
    public boolean contains(int userId) {
        try {
            String sql = "select user_id from filmorate_user where user_id = ?";
            jdbcTemplate.queryForObject(sql, Long.class, userId);
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
        return true;
    }

    private int insertUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO filmorate_user (email, name, login, birthday) " +
                "VALUES ( ?, ?, ?, ? )";

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, user.getEmail());
                    preparedStatement.setString(2, user.getName());
                    preparedStatement.setString(3, user.getLogin());
                    preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
                    return preparedStatement;
                }, keyHolder);

        return keyHolder.getKey().intValue();
    }

}
