package com.project.restaurant_review.repository;

import com.project.restaurant_review.entity.Restaurant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RestaurantRepository extends ElasticsearchRepository<Restaurant, String> {

//TODO: Add custom query methods

}
