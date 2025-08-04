package com.project.restaurant_review.service;

import com.project.restaurant_review.dto.RestaurantCreateUpdateRequestDto;
import com.project.restaurant_review.dto.RestaurantDto;
import com.project.restaurant_review.dto.RestaurantSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RestaurantService {

    RestaurantDto createRestaurant(RestaurantCreateUpdateRequestDto request);
    Page<RestaurantSummaryDto> searchRestaurants(String query,
                                                 Float minRating,
                                                 Float latitude,
                                                 Float longitude,
                                                 Float radius,
                                                 Pageable pageable);

    Optional<RestaurantDto> getRestaurant(String id);
}

