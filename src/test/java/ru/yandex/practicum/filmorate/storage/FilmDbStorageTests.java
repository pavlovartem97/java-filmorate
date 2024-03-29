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
import ru.yandex.practicum.filmorate.mapper.DirectorMapper;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {

    private FilmDbStorage filmDbStorage;

    private final FilmMapper filmMapper;

    private final GenreMapper genreMapper;

    private final DirectorMapper directorMapper;

    @BeforeEach
    public void setUp() {
        EmbeddedDatabase embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .addScript("test-data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        filmDbStorage = new FilmDbStorage(jdbcTemplate, genreMapper, filmMapper, directorMapper);
    }

    @Test
    public void getFilmByIdTest() {
        Film film = filmDbStorage.findFilmById(1).orElseThrow();

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
        List<Film> films = List.copyOf(filmDbStorage.findAll());

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
        Film film = filmDbStorage.findFilmById(1).orElseThrow();
        filmDbStorage.deleteFilm(film.getId());

        List<Film> films = List.copyOf(filmDbStorage.findAll());
        Assertions.assertEquals(films.size(), 2);
        Assertions.assertEquals(films.get(0).getId(), 2);
        Assertions.assertEquals(films.get(1).getId(), 3);
    }

    @Test
    public void updateFilmsTest() {
        Film film = filmDbStorage.findFilmById(1).orElseThrow();

        film.setName("new film");
        film.setMpa(new Mpa(5, null));

        Set<Genre> genres = film.getGenres();
        genres.add(new Genre(6, null));

        filmDbStorage.updateFilm(film);
        Film newFilm = filmDbStorage.findFilmById(1).orElseThrow();
        Assertions.assertEquals(newFilm.getName(), "new film");
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

        Film newFilm = filmDbStorage.findFilmById(film.getId()).orElseThrow();
        Assertions.assertEquals(newFilm.getId(), 4);
        Assertions.assertEquals(newFilm.getName(), "film4");
        Assertions.assertEquals(newFilm.getDuration(), 100);
        Assertions.assertEquals(newFilm.getReleaseDate(), LocalDate.parse("2022-10-01"));
        Assertions.assertEquals(newFilm.getDescription(), "description4");
        Assertions.assertEquals(newFilm.getMpa().getId(), 5);
        Assertions.assertEquals(newFilm.getMpa().getName(), "NC-17");
        Assertions.assertEquals(newFilm.getGenres().size(), 0);
    }

    @Test
    public void getTopFilmsTest() {
        List<Film> films = List.copyOf(filmDbStorage.findTopFilms(Map.of("count", 2)));

        Assertions.assertEquals(films.size(), 2);
        Assertions.assertEquals(films.get(0).getId(), 1);
        Assertions.assertEquals(films.get(1).getId(), 3);
    }

    @Test
    public void addFavouriteTest() {
        filmDbStorage.addFavourite(2, 1);
        filmDbStorage.addFavourite(2, 2);
        filmDbStorage.addFavourite(2, 3);

        List<Film> films = List.copyOf(filmDbStorage.findTopFilms(Map.of("count", 1)));
        Assertions.assertEquals(films.size(), 1);
        Assertions.assertEquals(films.get(0).getId(), 2);
    }

    @Test
    public void removeFavouriteTest() {
        filmDbStorage.removeFavoutite(1, 2);
        filmDbStorage.removeFavoutite(1, 3);

        List<Film> films = List.copyOf(filmDbStorage.findTopFilms(Map.of("count", 1)));
        Assertions.assertEquals(films.size(), 1);
        Assertions.assertEquals(films.get(0).getId(), 3);
    }

    @Test
    void addFilteredFavouriteTest() {
        Film film = Film.builder()
                .name("film name")
                .description("film_description")
                .releaseDate(LocalDate.parse("2000-01-01"))
                .duration(60)
                .mpa(new Mpa(5, null))
                .build();

        filmDbStorage.addFilm(film);
        Set<Genre> genres = film.getGenres();
        genres.add(new Genre(1, null));
        filmDbStorage.updateFilm(film);

        filmDbStorage.addFavourite(4, 1);
        filmDbStorage.addFavourite(4, 2);
        filmDbStorage.addFavourite(4, 3);

        List<Film> films = List.copyOf(filmDbStorage.findTopFilms(
                Map.of("count", 5,
                        "genreId", 1,
                        "year", 2000)
        ));

        Assertions.assertEquals(2, films.size());
        Assertions.assertEquals(4, films.get(0).getId());
        Assertions.assertEquals(1, films.get(1).getId());
    }

    @Test
    public void correctEmptyFilmsOfDirector() {
        List<Film> films = List.copyOf(filmDbStorage.getFilmsOfDirector(1, "order"));
        Assertions.assertEquals(films.size(), 0);
    }

    @Test
    public void correctFilmsOfDirectorDefaultOrder() {
        List<Film> films = List.copyOf(filmDbStorage.getFilmsOfDirector(2, "order"));
        Assertions.assertEquals(films.get(0).getId(), 1);
        Assertions.assertEquals(films.get(1).getId(), 2);
        Assertions.assertEquals(films.get(2).getId(), 3);
    }

    @Test
    public void correctFilmsOfDirectorLikesOrder() {
        List<Film> films = List.copyOf(filmDbStorage.getFilmsOfDirector(2, "likes"));
        Assertions.assertEquals(films.get(0).getId(), 1);
        Assertions.assertEquals(films.get(1).getId(), 3);
        Assertions.assertEquals(films.get(2).getId(), 2);
    }

    @Test
    public void correctFilmsOfDirectorYearOrder() {
        List<Film> films = List.copyOf(filmDbStorage.getFilmsOfDirector(2, "year"));
        Assertions.assertEquals(films.get(0).getId(), 1);
        Assertions.assertEquals(films.get(1).getId(), 2);
        Assertions.assertEquals(films.get(2).getId(), 3);
    }

    @Test
    void getRecommendationsTest() {
        filmDbStorage.addFavourite(1, 1);
        filmDbStorage.addFavourite(2, 2);
        filmDbStorage.addFavourite(2, 1);

        List<Film> films = List.copyOf(filmDbStorage.getRecommendations(2));
        Assertions.assertEquals(1, films.size());
        Assertions.assertEquals(3, films.get(0).getId());
    }

    @Test
    void getCommonFilmsTest() {
        filmDbStorage.addFavourite(2, 1);
        filmDbStorage.addFavourite(2, 2);
        filmDbStorage.addFavourite(2, 3);

        List<Film> films = List.copyOf(filmDbStorage.getCommonFilms(2, 3));
        Assertions.assertEquals(2, films.size());
        Assertions.assertEquals(2, films.get(0).getId());
        Assertions.assertEquals(1, films.get(1).getId());
    }

    @Test
    void searchFilmsTest() {
        List<Film> films = List.copyOf(filmDbStorage.searchFilms("Peace Man", Collections.singletonList("title")));
        Assertions.assertEquals(0, films.size());
        Film film = Film.builder()
                .name("film to find")
                .duration(100)
                .mpa(new Mpa(2, null))
                .description("description4")
                .releaseDate(LocalDate.parse("2011-10-01"))
                .build();
        filmDbStorage.addFilm(film);
        List<Film> filmsAgain = List.copyOf(filmDbStorage.searchFilms("find",
                Collections.singletonList("title")));
        Assertions.assertEquals(1, filmsAgain.size());

    }
}
