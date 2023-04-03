package ru.yandex.practicum.filmorate.storage.DbImlp;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Component("filmDbStorage")
@Primary
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private final FilmExtractor filmExtractor;

    @Override
    public void addFilm(Film film) {
        int id = insertFilm(film);
        film.setId(id);

        updateGenres(List.copyOf(film.getGenres()), film.getId());
    }

    @Override
    public void updateFilm(Film film) {
        getFilmById(film.getId());
        String sql = "UPDATE film " +
                     "SET name = ?,  description = ?, release_date = ?, duration = ?, mpa_id = ?" +
                     "WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());

        updateGenres(List.copyOf(film.getGenres()), film.getId());
    }

    @Override
    public void deleteFilm(Film film) {
        getFilmById(film.getId());
        String sql = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    @Override
    public Collection<Film> getAllFilm() {
        String sql = "SELECT * FROM film f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre as gf ON f.film_id = gf.film_id " +
                "LEFT JOIN genre g ON gf.GENRE_ID = g.genre_id";
        return jdbcTemplate.query(sql, filmExtractor);
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT * FROM film f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre as gf ON f.film_id = gf.film_id " +
                "LEFT JOIN genre g ON gf.GENRE_ID = g.genre_id " +
                "WHERE f.FILM_ID = ?";
        return jdbcTemplate.query(sql, filmExtractor, id).stream().findAny()
                .orElseThrow(() -> {
                    throw new FilmNotFoundException("Film with Id " + id + " is not found");
                });
    }

    private int insertFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO film (name, description, release_date, duration, mpa_id) " +
                "VALUES ( ?, ?, ?, ?, ? )";

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, film.getName());
                    preparedStatement.setString(2, film.getDescription());
                    preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
                    preparedStatement.setInt(4, film.getDuration());
                    preparedStatement.setInt(5, film.getMpa().getId());
                    return preparedStatement;
                }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    private void updateGenres(List<Genre> genres, int filmId) {
        String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES ( ?, ? )";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, filmId);
                preparedStatement.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }
}
