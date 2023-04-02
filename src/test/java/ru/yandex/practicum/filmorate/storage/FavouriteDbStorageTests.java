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
import ru.yandex.practicum.filmorate.storage.favourite.FavouriteDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmExtractor;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FavouriteDbStorageTests {

    private FavouriteDbStorage favouriteDbStorage;

    private final FilmExtractor filmExtractor;

    @BeforeEach
    public void setUp() {
        EmbeddedDatabase embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .addScript("test-data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        favouriteDbStorage = new FavouriteDbStorage(jdbcTemplate, filmExtractor);
    }

    @Test
    public void getTopFilmsTest() {
        List<Film> films = favouriteDbStorage.getTopFilm(2);

        Assertions.assertEquals(films.size(), 2);
        Assertions.assertEquals(films.get(0).getId(), 1);
        Assertions.assertEquals(films.get(1).getId(), 3);
    }

    @Test
    public void addFavouriteTest() {
        favouriteDbStorage.addFavourite(2, 1);
        favouriteDbStorage.addFavourite(2, 2);
        favouriteDbStorage.addFavourite(2, 3);

        List<Film> films = favouriteDbStorage.getTopFilm(1);
        Assertions.assertEquals(films.size(), 1);
        Assertions.assertEquals(films.get(0).getId(), 2);
    }

    @Test
    public void removeFavouriteTest() {
        favouriteDbStorage.removeFavoutite(1, 2);
        favouriteDbStorage.removeFavoutite(1, 3);

        List<Film> films = favouriteDbStorage.getTopFilm(1);
        Assertions.assertEquals(films.size(), 1);
        Assertions.assertEquals(films.get(0).getId(), 3);
    }
}
