package com.project.restaurant_review.service;

import com.project.restaurant_review.entity.RestaurantCreateUpdateRequest;
import com.project.restaurant_review.entity.Restaurant;

public interface RestaurantService {

    Restaurant createRestaurant(RestaurantCreateUpdateRequest request);

}
