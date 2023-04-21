package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;
    private final DirectorMapper directorMapper;

    @Override
    public Collection<Director> getAllDirectors() {
        String sql = "SELECT * FROM director";
        return jdbcTemplate.query(sql, directorMapper);
    }

    @Override
    public Director addDirector(Director director) {
        int id = insertDirector(director);
        director.setId(id);
        return director;
    }

    @Override
    public void updateDirector(Director director) {
        String sql = "UPDATE director SET director_name = ? WHERE director_id = ?";
        jdbcTemplate.update(sql, director.getName(), director.getId());
    }

    @Override
    public void deleteDirector(int directorID) {
        String sql = "DELETE FROM director WHERE director_id = ?";
        jdbcTemplate.update(sql, directorID);
    }

    @Override
    public Optional<Director> getDirectorByID(int directorID) {
        String sql = "SELECT * FROM director WHERE director_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, directorMapper, directorID));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public boolean contains(int directorID) {
        try {
            String sql = "SELECT director_id FROM director WHERE director_id = ?";
            jdbcTemplate.queryForObject(sql, Integer.class, directorID);
        } catch (EmptyResultDataAccessException exception) {
            return false;
        }
        return true;
    }

    private int insertDirector(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO director (director_name) VALUES (?)";

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, director.getName());
                    return preparedStatement;
                }, keyHolder);
        return keyHolder.getKey().intValue();
    }
}
