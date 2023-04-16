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
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.impl.DirectorDbStorage;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorDbStorageTests {
    private DirectorDbStorage dbStorage;
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
        dbStorage = new DirectorDbStorage(jdbcTemplate, directorMapper);
    }

    @Test
    public void correctFindDirectorByID() {
        Director director = dbStorage.getDirectorByID(1).orElseThrow();
        Assertions.assertEquals(director.getId(), 1);
        Assertions.assertEquals(director.getName(), "First Director");
    }

    @Test
    public void correctGetAllDirectors() {
        List<Director> directors = List.copyOf(dbStorage.getAllDirectors());

        Assertions.assertEquals(directors.get(0).getId(), 1);
        Assertions.assertEquals(directors.get(0).getName(), "First Director");
        Assertions.assertEquals(directors.get(1).getId(), 2);
        Assertions.assertEquals(directors.get(1).getName(), "Second Director");
    }

    @Test
    public void correctAddDirector() {
        Director director = Director.builder().name("Third Director").build();
        director = dbStorage.addDirector(director);
        Assertions.assertEquals(director.getId(), 3);
    }

    @Test
    public void correctDirectorUpdating() {
        Director director = dbStorage.getDirectorByID(2).orElseThrow();
        Director update = Director.builder().id(director.getId()).name("Updated Director").build();
        dbStorage.updateDirector(update);

        Director newDirector = dbStorage.getDirectorByID(director.getId()).orElseThrow();
        Assertions.assertEquals(newDirector.getName(), "Updated Director");
    }

    @Test
    public void correctDirectorRemoving() {
        dbStorage.deleteDirector(1);
        List<Director> directors = List.copyOf(dbStorage.getAllDirectors());
        Assertions.assertEquals(directors.size(), 1);
        Assertions.assertEquals(directors.get(0).getId(), 2);
        Assertions.assertEquals(directors.get(0).getName(), "Second Director");
    }
}
