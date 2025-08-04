package com.project.restaurant_review.service;

import com.project.restaurant_review.dto.ReviewCreateUpdateRequestDto;
import com.project.restaurant_review.dto.ReviewDto;
import com.project.restaurant_review.entity.User;

public interface ReviewService {

    ReviewDto createReview(User author, String restaurantId,
                           ReviewCreateUpdateRequestDto reviewRequest);

}
