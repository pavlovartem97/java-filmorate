package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private final GenreMapper genreMapper;

    private final FilmMapper filmMapper;

    @Override
    public void addFilm(Film film) {
        int id = insertFilm(film);
        film.setId(id);
        insertGenres(List.copyOf(film.getGenres()), film.getId());
    }

    @Override
    public void updateFilm(Film film) {
        String sql = "UPDATE film " +
                "SET name = ?,  description = ?, release_date = ?, duration = ?, mpa_id = ?" +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());

        insertGenres(List.copyOf(film.getGenres()), film.getId());
    }

    @Override
    public void deleteFilm(int filmId) {
        String sql = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "SELECT * FROM film f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id ";
        Collection<Film> films = jdbcTemplate.query(sql, filmMapper);

        for (Film film : films) {
            Collection<Genre> genres = getGenresByFilmId(film.getId());
            film.getGenres().addAll(genres);
        }

        return films;
    }

    @Override
    public Optional<Film> findFilmById(int id) {
        String sql = "SELECT * FROM film f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "WHERE f.FILM_ID = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sql, filmMapper, id);
            film.getGenres().addAll(getGenresByFilmId(film.getId()));
            return Optional.of(film);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public void addFavourite(int filmId, int userId) {
        String sql = "INSERT INTO favourite (film_id, user_id) VALUES ( ?, ? )";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeFavoutite(int filmId, int userId) {
        String sql = "DELETE FROM favourite WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public Collection<Film> findTopFilms(int count) {
        String sql = "SELECT * " +
                "FROM ( " +
                "    SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, COUNT(fv.user_id) rating " +
                "    FROM film f " +
                "    LEFT JOIN favourite fv ON f.film_id = fv.film_id " +
                "    GROUP BY f.film_id " +
                "    ORDER BY rating desc, f.film_id " +
                "    LIMIT ?) fl " +
                "JOIN mpa m ON fl.mpa_id = m.mpa_id ";
        Collection<Film> films = jdbcTemplate.query(sql, filmMapper, count);

        for (Film film : films) {
            Collection<Genre> genres = getGenresByFilmId(film.getId());
            film.getGenres().addAll(genres);
        }

        return films;
    }

    @Override
    public boolean contains(int filmId) {
        try {
            String sql = "SELECT film_id FROM film WHERE film_id = ?";
            jdbcTemplate.queryForObject(sql, Long.class, filmId);
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
        return true;
    }

    @Override
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        String sql = "SELECT * " +
                "FROM ( " +
                "    SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, COUNT(fv.user_id) rating " +
                "    FROM film f " +
                "       LEFT JOIN favourite fv ON f.film_id = fv.film_id " +
                "    WHERE f.film_id IN (SELECT favourite.film_id " +
                "                          FROM favourite " +
                "                          WHERE favourite.user_id = ? " +
                "                            AND favourite.film_id IN (SELECT favourite.film_id " +
                "                                                      FROM favourite " +
                "                                                      WHERE favourite.user_id = ?)) " +
                "    GROUP BY f.film_id " +
                "    ORDER BY rating desc, f.film_id " +
                ") fl " +
                "JOIN mpa m ON fl.mpa_id = m.mpa_id ";

        Collection<Film> films = jdbcTemplate.query(sql, filmMapper, userId, friendId);

        for (Film film : films) {
            Collection<Genre> genres = getGenresByFilmId(film.getId());
            film.getGenres().addAll(genres);
        }

        return films;
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

    private void insertGenres(List<Genre> genres, int filmId) {
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

    private Collection<Genre> getGenresByFilmId(int filmId) {
        String sql = "SELECT * FROM film_genre fg " +
                "JOIN genre g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, genreMapper, filmId);
    }
}
