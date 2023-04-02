package ru.yandex.practicum.filmorate.storage.fried;

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
    public void addFriend(User user, User friend) {
        String sql = "SELECT FROM friend WHERE friend_id = ? AND user_id = ?";
        int checkFriend = jdbcTemplate.query(sql, (rs, rowNum) -> 1, user.getId(), friend.getId()).size();

        if (checkFriend != 0) {
            sql = "INSERT INTO friend (friend_id, user_id, status) VALUES ( ?, ?, TRUE )";
            jdbcTemplate.update(sql, friend.getId(), user.getId());

            sql = "UPDATE friend SET status = TRUE " +
                    "WHERE friend_id = ? AND user_id = ?";
            jdbcTemplate.update(sql, user.getId(), friend.getId());
        } else {
            sql = "INSERT INTO friend (friend_id, user_id, status) VALUES ( ?, ?, FALSE )";
            jdbcTemplate.update(sql, friend.getId(), user.getId());
        }
    }

    @Override
    public void removeFriend(User user, User friend) {
        String sql = "DELETE FROM friend WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, user.getId(), friend.getId());

        sql = "UPDATE friend SET status = FALSE " +
                "WHERE friend_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, user.getId(), friend.getId());
    }

    @Override
    public List<User> getCommonFriends(User user1, User user2) {
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
        return jdbcTemplate.query(sql, userMapper, user1.getId(), user2.getId(), user1.getId());
    }

    @Override
    public List<User> getFriends(User user) {
        String sql =
                "SELECT * " +
                "FROM filmorate_user " +
                "WHERE user_id IN (SELECT friend_id " +
                "        FROM friend " +
                "        WHERE user_id = ?)";
        return jdbcTemplate.query(sql, userMapper, user.getId());
    }
}
