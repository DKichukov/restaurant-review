package com.project.restaurant_review.service;

import com.project.restaurant_review.dto.RestaurantCreateUpdateRequestDto;
import com.project.restaurant_review.dto.RestaurantDto;

public interface RestaurantService {

    RestaurantDto createRestaurant(RestaurantCreateUpdateRequestDto request);

}
