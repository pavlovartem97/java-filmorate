package ru.yandex.practicum.filmorate.storage.favourite;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmExtractor;

import java.util.List;

@Component
@Primary
@AllArgsConstructor
public class FavouriteDbStorage implements FavouriteStorage {

    private final JdbcTemplate jdbcTemplate;

    private final FilmExtractor filmExtractor;

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
    public List<Film> getTopFilm(int count) {
        String sql =
                "SELECT * " +
                "FROM ( " +
                "    SELECT f.film_id, f.name, f.description, f.release_date, f.duration, f.mpa_id, COUNT(fv.user_id) rating " +
                "    FROM film f " +
                "    LEFT JOIN favourite fv ON f.film_id = fv.film_id " +
                "    GROUP BY f.film_id " +
                "    ORDER BY rating desc, f.film_id " +
                "    LIMIT ?) fl " +
                "JOIN mpa m ON fl.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre gf ON fl.film_id = gf.film_id " +
                "LEFT JOIN genre g ON gf.genre_id = g.genre_id";
        return jdbcTemplate.query(sql, filmExtractor, count);
    }
}
