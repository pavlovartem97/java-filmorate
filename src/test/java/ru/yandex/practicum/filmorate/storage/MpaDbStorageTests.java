package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.DbImlp.MpaDbStorage;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTests {
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void testFindMpaById() {
        Mpa mpa = mpaDbStorage.getMpaById(1);

        Assertions.assertEquals(mpa.getId(), 1);
        Assertions.assertEquals(mpa.getName(), "G");
    }

    @Test
    public void testGetAllMpa() {
        List<Mpa> mpaList = mpaDbStorage.getAllMpa();

        Assertions.assertEquals(mpaList.size(), 5);
        Assertions.assertEquals(mpaList.get(0).getId(), 1);
        Assertions.assertEquals(mpaList.get(1).getId(), 2);
        Assertions.assertEquals(mpaList.get(2).getId(), 3);
        Assertions.assertEquals(mpaList.get(3).getId(), 4);
        Assertions.assertEquals(mpaList.get(4).getId(), 5);

        Assertions.assertEquals(mpaList.get(0).getName(), "G");
        Assertions.assertEquals(mpaList.get(1).getName(), "PG");
        Assertions.assertEquals(mpaList.get(2).getName(), "PG-13");
        Assertions.assertEquals(mpaList.get(3).getName(), "R");
        Assertions.assertEquals(mpaList.get(4).getName(), "NC-17");
    }
}
