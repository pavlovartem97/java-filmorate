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
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.storage.impl.FeedDbStorage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FeedDbStorageTest {

    private FeedStorage feedStorage;

    private final FeedMapper feedMapper;

    @BeforeEach
    void setUp() {
        EmbeddedDatabase embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addScript("schema.sql")
                .addScript("data.sql")
                .addScript("test-data.sql")
                .setType(EmbeddedDatabaseType.H2)
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        feedStorage = new FeedDbStorage(jdbcTemplate, feedMapper);
    }

    @Test
    void insertFeed() {
        Feed feed = Feed.builder()
                .userId(1)
                .timestamp(Instant.now().toEpochMilli())
                .eventType(EventType.REVIEW)
                .operation(Operation.ADD)
                .entityId(1)
                .build();

        int feedId = feedStorage.insertFeed(feed);

        Assertions.assertEquals(3, feedId);
    }

    @Test
    void getFeed() {
        Collection<Feed> feedListUserId1 = feedStorage.getFeed(1);
        Collection<Feed> feedListUserId2 = feedStorage.getFeed(2);

        Assertions.assertNotNull(feedListUserId1);
        Assertions.assertEquals(1, new ArrayList<>(feedListUserId1).get(0).getEventId());
        Assertions.assertEquals(1, feedListUserId1.size());

        Assertions.assertNotNull(feedListUserId2);
        Assertions.assertEquals(2, new ArrayList<>(feedListUserId2).get(0).getEventId());
        Assertions.assertEquals(1, feedListUserId2.size());
    }
}