package ru.yandex.practicum.filmorate.storage.databaseImlp;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, (rs, row) -> makeGenre(rs));
    }

    @Override
    public Genre getGenreById(int id) {
        String sql = "SELECT * FROM genre WHERE genre_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, row) -> makeGenre(rs), id);
        } catch (DataAccessException ex) {
            throw new GenreNotFoundException("Genre with Id " + id + " is not found");
        }
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
        return genre;
    }
}
