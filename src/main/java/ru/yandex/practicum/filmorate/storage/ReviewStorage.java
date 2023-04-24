package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.enumerate.OperationType;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.Optional;

public interface ReviewStorage {

    void addReview(Review review);

    void updateReview(Review review);

    void deleteReviewById(int reviewId);

    Optional<Review> findReviewById(int reviewId);

    Collection<Review> findTopReview(int filmId, int count);

    Collection<Review> findTopReview(int count);

    void changeLikeState(int reviewId, int userId, boolean isLike, OperationType addLike);

    boolean contains(int reviewId);
}
