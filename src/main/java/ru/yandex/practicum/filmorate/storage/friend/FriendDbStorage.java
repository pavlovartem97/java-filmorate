package ru.yandex.practicum.filmorate.storage.friend;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserMapper;

import java.util.List;

@Component
@Primary
@AllArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;

    private final UserMapper userMapper;

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
        } else {
            sql = "INSERT INTO friend (friend_id, user_id, status) VALUES ( ?, ?, FALSE )";
            jdbcTemplate.update(sql, friendId, userId);
        }
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sql = "DELETE FROM friend WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);

        sql = "UPDATE friend SET status = FALSE " +
                "WHERE friend_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getCommonFriends(int userId1, int userId2) {
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
    public List<User> getFriends(int userId) {
        String sql =
                "SELECT * " +
                "FROM filmorate_user " +
                "WHERE user_id IN (SELECT friend_id " +
                "        FROM friend " +
                "        WHERE user_id = ?)";
        return jdbcTemplate.query(sql, userMapper, userId);
    }
}
