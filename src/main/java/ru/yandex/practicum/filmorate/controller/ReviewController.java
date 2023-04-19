package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        reviewService.add(review);
        return review;
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        reviewService.delete(id);
    }

    @GetMapping("/{id}")
    public Review get(@PathVariable("id") int id) {
        return reviewService.get(id);
    }

    @GetMapping
    public Collection<Review> getTopReviews(@RequestParam(value = "filmId", required = false) Integer filmId,
                                            @RequestParam(value = "count", required = false, defaultValue = "10") int count) {
        return reviewService.getTopReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int reviewId, @PathVariable("userId") int userId) {
        reviewService.changeLikeState(reviewId, userId, true, true);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislike(@PathVariable("id") int reviewId, @PathVariable("userId") int userId) {
        reviewService.changeLikeState(reviewId, userId, false, true);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") int reviewId, @PathVariable("userId") int userId) {
        reviewService.changeLikeState(reviewId, userId, true, false);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable("id") int reviewId, @PathVariable("userId") int userId) {
        reviewService.changeLikeState(reviewId, userId, false, false);
    }
}
