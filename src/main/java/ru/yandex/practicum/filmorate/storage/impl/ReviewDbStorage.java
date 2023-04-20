package ru.yandex.practicum.filmorate.storage.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.ReviewMapper;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.enumerate.OperationType;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    private final ReviewMapper reviewMapper;

    @Override
    public void addReview(Review review) {
        int id = insertReview(review);
        review.setUseful(0);
        review.setReviewId(id);
    }

    @Override
    public void updateReview(Review review) {
        String sql = "UPDATE review " +
                "SET content = ?,  is_positive = ? " +
                "WHERE review_id = ?";
        jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getReviewId());
    }

    @Override
    public void deleteReviewById(int reviewId) {
        String sql = "DELETE FROM review " +
                "WHERE review_id = ?";
        jdbcTemplate.update(sql, reviewId);
    }

    @Override
    public Optional<Review> findReviewById(int reviewId) {
        try {
            String sql = "SELECT * FROM review " +
                    "WHERE review_id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, reviewMapper, reviewId));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Review> findTopReview(int filmId, int count) {
        String sql = "SELECT * FROM review " +
                "WHERE film_id = ? " +
                "ORDER BY useful DESC, review_id " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, reviewMapper, filmId, count);
    }

    @Override
    public Collection<Review> findTopReview(int count) {
        String sql = "SELECT * FROM review " +
                "ORDER BY useful DESC, review_id " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, reviewMapper, count);
    }

    @Override
    public void changeLikeState(int reviewId, int userId, boolean isLike, OperationType addLike) {
        if (addLike.equals(OperationType.REMOVE)) {
            deleteLike(reviewId, userId, isLike);
        } else {
            mergeLike(reviewId, userId, isLike);
        }
        updateUseful(reviewId);
    }

    @Override
    public boolean contains(int reviewId) {
        try {
            String sql = "SELECT review_id FROM review WHERE review_id = ?";
            jdbcTemplate.queryForObject(sql, Integer.class, reviewId);
        } catch (EmptyResultDataAccessException ex) {
            return false;
        }
        return true;
    }

    private int insertReview(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO review (content, user_id, film_id, is_positive) " +
                "VALUES ( ?, ?, ?, ? )";

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, review.getContent());
                    preparedStatement.setInt(2, review.getUserId());
                    preparedStatement.setInt(3, review.getFilmId());
                    preparedStatement.setBoolean(4, review.getIsPositive());
                    return preparedStatement;
                }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    private void mergeLike(int reviewId, int userId, boolean isLike) {
        String sql = "MERGE INTO review_like (review_id, user_id, is_like) " +
                "VALUES ( ?, ?, ? ) ";
        jdbcTemplate.update(sql, reviewId, userId, isLike);
    }

    private void deleteLike(int reviewId, int userId, boolean isLike) {
        String sql = "DELETE FROM review_like " +
                "WHERE review_id = ? AND user_id = ? AND is_like = ?";
        jdbcTemplate.update(sql, reviewId, userId, isLike);
    }

    private int getUseful(int reviewId) {
        String sql = "SELECT is_like, COUNT(is_like) val " +
                "FROM review_like " +
                "WHERE review_id = ? " +
                "GROUP BY is_like";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, reviewId);

        int sum = 0;
        while (userRows.next()) {
            boolean isLike = userRows.getBoolean("is_like");
            int count = userRows.getInt("val");
            sum += isLike ? count : -count;
        }

        return sum;
    }

    private void updateUseful(int reviewId) {
        int useful = getUseful(reviewId);
        String sql = "UPDATE review " +
                "SET useful = ? " +
                "WHERE review_id = ? ";
        jdbcTemplate.update(sql, useful, reviewId);
    }
}
