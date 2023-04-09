package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.databaseImlp.GenreDbStorage;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTests {

    private final GenreDbStorage genreDbStorage;

    private final List<String> genreList = List.of("Комедия", "Драма", "Мультфильм", "Триллер", "Документальный", "Боевик");

    @Test
    public void testFindGenreById() {
        Genre genre = genreDbStorage.getGenreById(1);

        Assertions.assertEquals(genre.getId(), 1);
        Assertions.assertEquals(genre.getName(), genreList.get(0));
    }

    @Test
    public void testGetAllGenre() {
        List<Genre> mpaList = genreDbStorage.getAllGenres();

        Assertions.assertEquals(mpaList.size(), 6);
        Assertions.assertEquals(mpaList.get(0).getId(), 1);
        Assertions.assertEquals(mpaList.get(1).getId(), 2);
        Assertions.assertEquals(mpaList.get(2).getId(), 3);
        Assertions.assertEquals(mpaList.get(3).getId(), 4);
        Assertions.assertEquals(mpaList.get(4).getId(), 5);
        Assertions.assertEquals(mpaList.get(5).getId(), 6);

        Assertions.assertEquals(mpaList.get(0).getName(), genreList.get(0));
        Assertions.assertEquals(mpaList.get(1).getName(), genreList.get(1));
        Assertions.assertEquals(mpaList.get(2).getName(), genreList.get(2));
        Assertions.assertEquals(mpaList.get(3).getName(), genreList.get(3));
        Assertions.assertEquals(mpaList.get(4).getName(), genreList.get(4));
        Assertions.assertEquals(mpaList.get(5).getName(), genreList.get(5));
    }
}
