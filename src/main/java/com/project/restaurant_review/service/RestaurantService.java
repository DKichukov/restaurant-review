package com.project.restaurant_review.service;

import com.project.restaurant_review.dto.RestaurantCreateUpdateRequestDto;
import com.project.restaurant_review.dto.RestaurantDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestaurantService {

    RestaurantDto createRestaurant(RestaurantCreateUpdateRequestDto request);
    Page<RestaurantDto> searchRestaurants(String query,
                                          Float minRating,
                                          Float latitude,
                                          Float longitude,
                                          Float radius,
                                          Pageable pageable);

}
