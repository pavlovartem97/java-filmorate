package ru.yandex.practicum.filmorate.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.enumerate.EventType;
import ru.yandex.practicum.filmorate.model.enumerate.OperationType;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewService {

    ReviewStorage reviewStorage;

    UserStorage userStorage;

    FilmStorage filmStorage;

    FeedStorage feedStorage;

    public void add(Review review) {
        checkFilm(review.getFilmId());
        checkUser(review.getUserId());
        reviewStorage.addReview(review);
        feedStorage.addFeed(review.getUserId(), review.getReviewId(), OperationType.ADD, EventType.REVIEW);
        log.debug("Review added: reviewId = {}", review.getReviewId());
    }

    public Review update(Review review) {
        checkFilm(review.getFilmId());
        checkUser(review.getUserId());
        checkReview(review.getReviewId());
        reviewStorage.updateReview(review);
        Review resultReview = reviewStorage.findReviewById(review.getReviewId()).orElseThrow();
        feedStorage.addFeed(resultReview.getUserId(), resultReview.getReviewId(), OperationType.UPDATE, EventType.REVIEW);
        log.debug("Review updated: reviewId = {} ", resultReview.getReviewId());
        return resultReview;
    }

    public void delete(int reviewId) {
        checkReview(reviewId);
        Review review = get(reviewId);
        reviewStorage.deleteReviewById(review.getReviewId());
        feedStorage.addFeed(review.getUserId(), review.getReviewId(), OperationType.REMOVE, EventType.REVIEW);
        log.debug("Review deleted: reviewId = {} ", reviewId);
    }

    public Review get(int reviewId) {
        Review review = reviewStorage.findReviewById(reviewId)
                .orElseThrow(() -> {
                    throw new ReviewNotFoundException(String.format("Review is not found %d", reviewId));
                });
        log.debug("Review got: reviewId = {}", reviewId);
        return review;
    }

    public Collection<Review> getTopReviews(Integer filmId, int count) {
        Collection<Review> topReviews = filmId == null
                ? reviewStorage.findTopReview(count)
                : reviewStorage.findTopReview(filmId, count);
        log.debug("Top review {} size got ", topReviews.size());
        return topReviews;
    }

    public void changeLikeState(int reviewId, int userId, boolean isLike, OperationType addLike) {
        checkReview(reviewId);
        checkUser(userId);
        reviewStorage.changeLikeState(reviewId, userId, isLike, addLike);
        if (addLike.equals(OperationType.REMOVE)) {
            Review review = get(reviewId);
            feedStorage.addFeed(review.getUserId(), review.getReviewId(), addLike, EventType.REVIEW);
        }
        log.debug("Like changed: reviewId = {}, userId = {}, isLike = {}, addLike = {}", reviewId, userId, isLike, addLike);
    }

    private void checkReview(int reviewId) {
        if (!reviewStorage.contains(reviewId)) {
            throw new ReviewNotFoundException(String.format("Review is not found %d", reviewId));
        }
    }

    private void checkUser(int userId) {
        if (!userStorage.contains(userId)) {
            throw new UserNotFoundException(String.format("User is not found %d", userId));
        }
    }

    private void checkFilm(int filmId) {
        if (!filmStorage.contains(filmId)) {
            throw new FilmNotFoundException(String.format("Film is not found %d", filmId));
        }
    }

}
