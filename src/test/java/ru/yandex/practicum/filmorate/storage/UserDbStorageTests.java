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
import ru.yandex.practicum.filmorate.mapper.FeedMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FeedDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.mapper.UserMapper;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTests {

    private UserDbStorage userDbStorage;

    private final UserMapper userMapper;

    private final FeedMapper feedMapper;


    @BeforeEach
    public void setUp() {
        EmbeddedDatabase embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .addScript("test-data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        userDbStorage = new UserDbStorage(jdbcTemplate, userMapper, new FeedDbStorage(jdbcTemplate, feedMapper));
    }

    @Test
    public void findByIdTest() {
        User user = userDbStorage.findUserById(1).orElseThrow();

        Assertions.assertEquals(user.getId(), 1);
        Assertions.assertEquals(user.getName(), "name1");
        Assertions.assertEquals(user.getLogin(), "login1");
        Assertions.assertEquals(user.getBirthday(), LocalDate.parse("2000-10-31"));
    }

    @Test
    public void findAllUsersTest() {
        List<User> users = List.copyOf(userDbStorage.getAll());

        Assertions.assertEquals(users.get(0).getId(), 1);
        Assertions.assertEquals(users.get(0).getName(), "name1");
        Assertions.assertEquals(users.get(0).getLogin(), "login1");
        Assertions.assertEquals(users.get(0).getEmail(), "email1");
        Assertions.assertEquals(users.get(0).getBirthday(), LocalDate.parse("2000-10-31"));

        Assertions.assertEquals(users.get(1).getId(), 2);
        Assertions.assertEquals(users.get(1).getName(), "name2");
        Assertions.assertEquals(users.get(1).getLogin(), "login2");
        Assertions.assertEquals(users.get(1).getEmail(), "email2");
        Assertions.assertEquals(users.get(1).getBirthday(), LocalDate.parse("2010-10-31"));
    }

    @Test
    public void addUserTest() {
        User user = User.builder()
                .email("email4")
                .birthday(LocalDate.parse("2005-10-31"))
                .name("name4")
                .login("login")
                .build();

        user = userDbStorage.addUser(user);
        Assertions.assertEquals(user.getId(), 4);
    }

    @Test
    public void updateUserTest() {
        User user = userDbStorage.findUserById(1).orElseThrow();
        User updatedUser = User.builder()
                .id(user.getId())
                .name("new name")
                .login("new login")
                .birthday(user.getBirthday())
                .email(user.getEmail())
                .build();
        userDbStorage.updateUser(updatedUser);

        User newUser = userDbStorage.findUserById(1).orElseThrow();
        Assertions.assertEquals(newUser.getName(), "new name");
        Assertions.assertEquals(newUser.getLogin(), "new login");
        Assertions.assertEquals(newUser.getBirthday(), user.getBirthday());
        Assertions.assertEquals(newUser.getEmail(), user.getEmail());
    }

    @Test
    public void deleteUser() {
        User user = userDbStorage.findUserById(2).orElseThrow();
        userDbStorage.deleteUser(user.getId());

        List<User> users = List.copyOf(userDbStorage.getAll());
        Assertions.assertEquals(users.size(), 2);
        Assertions.assertEquals(users.get(0).getId(), 1);
    }

    @Test
    public void getFriendTest() {
        List<User> users = List.copyOf(userDbStorage.findFriendsById(1));

        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(users.get(0).getId(), 2);
    }

    @Test
    public void addFriendTest() {
        userDbStorage.addFriend(1, 3);
        List<User> users = List.copyOf(userDbStorage.findFriendsById(1));

        Assertions.assertEquals(users.size(), 2);
        Assertions.assertEquals(users.get(0).getId(), 2);
        Assertions.assertEquals(users.get(1).getId(), 3);
    }

    @Test
    public void removeFriendTest() {
        userDbStorage.removeFriend(1, 2);
        List<User> users = List.copyOf(userDbStorage.findFriendsById(1));

        Assertions.assertEquals(users.size(), 0);
    }

    @Test
    public void getCommonFriendsTest() {
        List<User> users = List.copyOf(userDbStorage.getCommonFriends(1, 3));

        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(users.get(0).getId(), 2);
    }
}
