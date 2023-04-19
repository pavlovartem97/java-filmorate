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
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.impl.FeedDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.ReviewDbStorage;

import java.util.Optional;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewDbStorageTests {

    private ReviewDbStorage reviewDbStorage;

    private final ReviewMapper reviewMapper;

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
        reviewDbStorage = new ReviewDbStorage(jdbcTemplate, reviewMapper, new FeedDbStorage(jdbcTemplate, feedMapper));
    }

    @Test
    public void getReviewByIdTest() {
        Review review = reviewDbStorage.findReviewById(1).orElseThrow();

        Assertions.assertEquals(1, review.getReviewId());
        Assertions.assertEquals("content1", review.getContent());
        Assertions.assertEquals(1, review.getFilmId());
        Assertions.assertEquals(1, review.getUserId());
        Assertions.assertEquals(true, review.getIsPositive());
        Assertions.assertEquals(0, review.getUseful());
    }

    @Test
    public void updateReviewByIdTest() {
        Review review = reviewDbStorage.findReviewById(1).orElseThrow();
        review.setUseful(100);
        review.setContent("New content");
        review.setFilmId(2);
        review.setUserId(2);
        review.setIsPositive(false);

        reviewDbStorage.updateReview(review);
        review = reviewDbStorage.findReviewById(1).orElseThrow();
        Assertions.assertEquals(1, review.getReviewId());
        Assertions.assertEquals("New content", review.getContent());
        Assertions.assertEquals(1, review.getFilmId());
        Assertions.assertEquals(1, review.getUserId());
        Assertions.assertEquals(false, review.getIsPositive());
        Assertions.assertEquals(0, review.getUseful());
    }

    @Test
    public void addReviewTest() {
        Review review = Review.builder()
                .content("new review")
                .userId(3)
                .filmId(2)
                .isPositive(true)
                .build();

        reviewDbStorage.addReview(review);
        Assertions.assertEquals(3, review.getReviewId());
        Assertions.assertEquals("new review", review.getContent());
        Assertions.assertEquals(2, review.getFilmId());
        Assertions.assertEquals(3, review.getUserId());
        Assertions.assertEquals(true, review.getIsPositive());
        Assertions.assertEquals(0, review.getUseful());
    }

    @Test
    public void deleteReviewTest() {
        reviewDbStorage.deleteReviewById(1);

        Optional<Review> reviewOptional = reviewDbStorage.findReviewById(1);
        Assertions.assertTrue(reviewOptional.isEmpty());
    }

    @Test
    public void findTopReviewTest() {
        reviewDbStorage.changeLikeState(2, 2, true, Operation.ADD);
        reviewDbStorage.changeLikeState(1, 2, false, Operation.ADD);

        List<Review> reviews = List.copyOf(reviewDbStorage.findTopReview(2));
        Assertions.assertEquals(2, reviews.size());
        Assertions.assertEquals(2, reviews.get(0).getReviewId());
        Assertions.assertEquals(1, reviews.get(1).getReviewId());
        Assertions.assertEquals(1, reviews.get(0).getUseful());
        Assertions.assertEquals(-1, reviews.get(1).getUseful());

        reviews = List.copyOf(reviewDbStorage.findTopReview(2, 1));
        Assertions.assertEquals(0, reviews.size());

        reviews = List.copyOf(reviewDbStorage.findTopReview(1, 1));
        Assertions.assertEquals(1, reviews.size());
        Assertions.assertEquals(2, reviews.get(0).getReviewId());
        Assertions.assertEquals(1, reviews.get(0).getUseful());
    }

    @Test
    public void changeTopReviewTest() {
        reviewDbStorage.changeLikeState(2, 2, true, Operation.ADD);
        reviewDbStorage.changeLikeState(1, 2, false, Operation.ADD);

        List<Review> reviews = List.copyOf(reviewDbStorage.findTopReview(2));
        Assertions.assertEquals(2, reviews.size());
        Assertions.assertEquals(2, reviews.get(0).getReviewId());
        Assertions.assertEquals(1, reviews.get(1).getReviewId());
        Assertions.assertEquals(1, reviews.get(0).getUseful());
        Assertions.assertEquals(-1, reviews.get(1).getUseful());

        reviewDbStorage.changeLikeState(2, 2, true, Operation.REMOVE);
        reviewDbStorage.changeLikeState(1, 2, true, Operation.ADD);

        reviews = List.copyOf(reviewDbStorage.findTopReview(2));
        Assertions.assertEquals(2, reviews.size());
        Assertions.assertEquals(1, reviews.get(0).getReviewId());
        Assertions.assertEquals(2, reviews.get(1).getReviewId());
        Assertions.assertEquals(1, reviews.get(0).getUseful());
        Assertions.assertEquals(0, reviews.get(1).getUseful());
    }
}
