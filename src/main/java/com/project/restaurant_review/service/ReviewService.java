package com.project.restaurant_review.service;

import com.project.restaurant_review.dto.ReviewCreateUpdateRequestDto;
import com.project.restaurant_review.dto.ReviewDto;
import com.project.restaurant_review.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    ReviewDto createReview(User author, String restaurantId,
                           ReviewCreateUpdateRequestDto reviewRequest);
    Page<ReviewDto> lastReviews(String restaurantId, Pageable pageable);

}
