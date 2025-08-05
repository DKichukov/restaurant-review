package com.project.restaurant_review.service;

import com.project.restaurant_review.dto.ReviewCreateUpdateRequestDto;
import com.project.restaurant_review.dto.ReviewDto;
import com.project.restaurant_review.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewService {

    ReviewDto createReview(User author,
                           String restaurantId,
                           ReviewCreateUpdateRequestDto reviewRequest);

    Page<ReviewDto> lastReviews(String restaurantId,
                                Pageable pageable);

    Optional<ReviewDto> getReview(String restaurantId,
                                  String reviewId);

    ReviewDto updateReview(User author,
                           String restaurantId,
                           String reviewId,
                           ReviewCreateUpdateRequestDto reviewRequest);

    void deleteReview(String restaurantId,
                      String reviewId);

}
