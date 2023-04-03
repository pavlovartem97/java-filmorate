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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbImlp.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.DbImlp.UserMapper;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendDbStorageTests {

    private FriendDbStorage friendDbStorage;

    private final UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        EmbeddedDatabase embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .addScript("test-data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        friendDbStorage = new FriendDbStorage(jdbcTemplate, userMapper);
    }

    @Test
    public void getFriendTest() {
        List<User> users = friendDbStorage.getFriends(1);

        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(users.get(0).getId(), 2);
    }

    @Test
    public void addFriendTest() {
        friendDbStorage.addFriend(1, 3);
        List<User> users = friendDbStorage.getFriends(1);

        Assertions.assertEquals(users.size(), 2);
        Assertions.assertEquals(users.get(0).getId(), 2);
        Assertions.assertEquals(users.get(1).getId(), 3);
    }

    @Test
    public void removeFriendTest() {
        friendDbStorage.removeFriend(1, 2);
        List<User> users = friendDbStorage.getFriends(1);

        Assertions.assertEquals(users.size(), 0);
    }

    @Test
    public void getCommonFriendsTest() {
        List<User> users = friendDbStorage.getCommonFriends(1, 3);

        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(users.get(0).getId(), 2);
    }
}
