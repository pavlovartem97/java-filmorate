package ru.yandex.practicum.filmorate.storage.databaseImlp;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class FilmExtractor implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs)
            throws SQLException, DataAccessException {
        Map<Integer, Film> films = new LinkedHashMap<>();
        while (rs.next()) {
            int filmId = rs.getInt("film_id");
            if (!films.containsKey(filmId)) {
                Film film = getFilm(rs);
                films.put(film.getId(), film);
            }
            if (rs.getInt("genre_id") != 0) {
                films.get(filmId).getGenres().add(
                        new Genre(rs.getInt("genre_id"), rs.getString("genre_name")));
            }
        }
        return new ArrayList<>(films.values());
    }

    private Film getFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")))
                .build();
    }
}
