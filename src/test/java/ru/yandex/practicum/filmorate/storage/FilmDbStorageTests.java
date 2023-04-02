package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmExtractor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {
    private EmbeddedDatabase embeddedDatabase;

    private JdbcTemplate jdbcTemplate;

    private FilmDbStorage filmDbStorage;

    private final FilmExtractor filmExtractor;

    @BeforeEach
    public void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .addScript("test-data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        filmDbStorage = new FilmDbStorage(jdbcTemplate, filmExtractor);
    }

    @Test
    public void getFilmByIdTest() {
        Film film = filmDbStorage.getFilmById(1);

        Assertions.assertEquals(film.getId(), 1);
        Assertions.assertEquals(film.getName(), "name1");
        Assertions.assertEquals(film.getDescription(), "description1");
        Assertions.assertEquals(film.getDuration(), 100);
        Assertions.assertEquals(film.getReleaseDate(), LocalDate.parse("2000-10-31"));
        Assertions.assertEquals(film.getMpa().getId(), 1);
        Assertions.assertEquals(film.getMpa().getName(), "G");
        Assertions.assertEquals(film.getGenres().size(), 2);

        List<Genre> genres = List.copyOf(film.getGenres());
        Assertions.assertEquals(genres.get(0).getId(), 1);
        Assertions.assertEquals(genres.get(0).getName(), "Комедия");
        Assertions.assertEquals(genres.get(1).getId(), 2);
        Assertions.assertEquals(genres.get(1).getName(), "Драма");
    }

    @Test
    public void getAllFilmsTest() {
        List<Film> films = List.copyOf(filmDbStorage.getAllFilm());

        Assertions.assertEquals(films.size(), 3);
        Assertions.assertEquals(films.get(0).getId(), 1);
        Assertions.assertEquals(films.get(0).getName(), "name1");
        Assertions.assertEquals(films.get(0).getDescription(), "description1");
        Assertions.assertEquals(films.get(0).getDuration(), 100);
        Assertions.assertEquals(films.get(0).getReleaseDate(), LocalDate.parse("2000-10-31"));
        Assertions.assertEquals(films.get(0).getMpa().getId(), 1);
        Assertions.assertEquals(films.get(0).getMpa().getName(), "G");
        Assertions.assertEquals(films.get(0).getGenres().size(), 2);

        List<Genre> genres = List.copyOf(films.get(0).getGenres());
        Assertions.assertEquals(genres.get(0).getId(), 1);
        Assertions.assertEquals(genres.get(0).getName(), "Комедия");
        Assertions.assertEquals(genres.get(1).getId(), 2);
        Assertions.assertEquals(genres.get(1).getName(), "Драма");

        Assertions.assertEquals(films.get(1).getId(), 2);
        Assertions.assertEquals(films.get(1).getName(), "name2");
        Assertions.assertEquals(films.get(1).getDescription(), "description2");
        Assertions.assertEquals(films.get(1).getDuration(), 50);
        Assertions.assertEquals(films.get(1).getMpa().getId(), 3);
        Assertions.assertEquals(films.get(1).getMpa().getName(), "PG-13");
        Assertions.assertEquals(films.get(1).getGenres().size(), 0);

        Assertions.assertEquals(films.get(2).getId(), 3);
        Assertions.assertEquals(films.get(2).getName(), "name3");
        Assertions.assertEquals(films.get(2).getDescription(), "description3");
        Assertions.assertEquals(films.get(2).getDuration(), 200);
        Assertions.assertEquals(films.get(2).getMpa().getId(), 4);
        Assertions.assertEquals(films.get(2).getMpa().getName(), "R");
        Assertions.assertEquals(films.get(2).getGenres().size(), 0);
    }

    @Test
    public void deleteFilmsTest() {
        Film film = filmDbStorage.getFilmById(1);
        filmDbStorage.deleteFilm(film);

        List<Film> films = List.copyOf(filmDbStorage.getAllFilm());
        Assertions.assertEquals(films.size(), 2);
        Assertions.assertEquals(films.get(0).getId(), 2);
        Assertions.assertEquals(films.get(1).getId(), 3);
    }

    @Test
    public void updateFilmsTest() {
        Film film = filmDbStorage.getFilmById(1);

        film.setName("new film");
        Set<Genre> genres = film.getGenres();
        genres.add(new Genre(6, null));
        film.setMpa(new Mpa(5, null));

        filmDbStorage.updateFilm(film);
        Film newFilm = filmDbStorage.getFilmById(1);
        Assertions.assertEquals(film.getName(), newFilm.getName());
        Assertions.assertEquals(newFilm.getMpa().getId(), 5);
        Assertions.assertEquals(newFilm.getMpa().getName(), "NC-17");
        Assertions.assertEquals(newFilm.getGenres().size(), 3);
    }

    @Test
    public void addFilm() {
        Film film = Film.builder()
                .name("film4")
                .duration(100)
                .mpa(new Mpa(5, null))
                .description("description4")
                .releaseDate(LocalDate.parse("2022-10-01"))
                .build();

        filmDbStorage.addFilm(film);

        Film newFilm = filmDbStorage.getFilmById(film.getId());
        Assertions.assertEquals(newFilm.getId(), 4);
        Assertions.assertEquals(newFilm.getName(), "film4");
        Assertions.assertEquals(newFilm.getDuration(), 100);
        Assertions.assertEquals(newFilm.getReleaseDate(), LocalDate.parse("2022-10-01"));
        Assertions.assertEquals(newFilm.getDescription(), "description4");
        Assertions.assertEquals(newFilm.getMpa().getId(), 5);
        Assertions.assertEquals(newFilm.getMpa().getName(), "NC-17");
        Assertions.assertEquals(newFilm.getGenres().size(), 0);
    }
}
