package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ReviewNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
@AllArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;

    private final UserStorage userStorage;

    private final FilmStorage filmStorage;

    public void add(Review review) {
        checkFilm(review.getFilmId());
        checkUser(review.getUserId());
        reviewStorage.addReview(review);
        log.debug("Review added: reviewId = {}", review.getReviewId());
    }

    public Review update(Review review) {
        checkFilm(review.getFilmId());
        checkUser(review.getUserId());
        checkReview(review.getReviewId());
        reviewStorage.updateReview(review);
        Review resultReview = reviewStorage.findReviewById(review.getReviewId()).orElseThrow();
        log.debug("Review updated: reviewId = {} ", resultReview.getReviewId());
        return resultReview;
    }

    public void delete(int reviewId) {
        checkReview(reviewId);
        reviewStorage.deleteReviewById(reviewId);
        log.debug("Review deleted: reviewId = {} ", reviewId);
    }

    public Review get(int reviewId) {
        Review review = reviewStorage.findReviewById(reviewId)
                .orElseThrow(() -> {
                    throw new ReviewNotFoundException("Review is not found " + reviewId);
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

    public void changeLikeState(int reviewId, int userId, boolean isLike, Operation addLike) {
        checkReview(reviewId);
        checkUser(userId);
        reviewStorage.changeLikeState(reviewId, userId, isLike, addLike);
        log.debug("Like changed: reviewId = {}, userId = {}, isLike = {}, addLike = {}", reviewId, userId, isLike, addLike.name());
    }

    private void checkReview(int reviewId) {
        if (!reviewStorage.contains(reviewId)) {
            throw new ReviewNotFoundException("Review is not found " + reviewId);
        }
    }

    private void checkUser(int userId) {
        if (!userStorage.contains(userId)) {
            throw new UserNotFoundException("User is not found " + userId);
        }
    }

    private void checkFilm(int filmId) {
        if (!filmStorage.contains(filmId)) {
            throw new FilmNotFoundException("Film is not found " + filmId);
        }
    }
}
