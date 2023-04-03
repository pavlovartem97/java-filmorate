package ru.yandex.practicum.filmorate.storage.DbImlp;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;

@Component("userDbStorage")
@Primary
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public User addUser(User user) {
        int id = insertUser(user);
        user.setId(id);
        return user;
    }

    @Override
    public void updateUser(User user) {
        getUserById(user.getId());
        String sql = "UPDATE filmorate_user " +
                "SET email = ?,  name = ?, login = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getName(), user.getLogin(), user.getBirthday(), user.getId());
    }

    @Override
    public void deleteUser(User user) {
        getUserById(user.getId());
        String sql = "DELETE FROM filmorate_user WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getId());
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * FROM filmorate_user";
        return jdbcTemplate.query(sql, userMapper);
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM filmorate_user WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userMapper, id);
        } catch (DataAccessException ex) {
            throw new UserNotFoundException("User with Id " + id + " not found");
        }
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
