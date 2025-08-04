package com.project.restaurant_review.controller;

import com.project.restaurant_review.dto.RestaurantCreateUpdateRequestDto;
import com.project.restaurant_review.dto.RestaurantDto;
import com.project.restaurant_review.dto.RestaurantSummaryDto;
import com.project.restaurant_review.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(
            @Valid @RequestBody RestaurantCreateUpdateRequestDto requestDto) {

        RestaurantDto restaurantDto = restaurantService.createRestaurant(requestDto);

        return ResponseEntity.ok(restaurantDto);

    }

    @GetMapping
    public Page<RestaurantSummaryDto> searchRestaurants(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Float latitude,
            @RequestParam(required = false) Float longitude,
            @RequestParam(required = false) Float radius,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
                                                       ) {
        return restaurantService.searchRestaurants(query, minRating, latitude
                , longitude, radius, PageRequest.of(page - 1, size));
    }

    @GetMapping(path = "/{restaurantId}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable("restaurantId") String restaurantId) {
        return restaurantService.getRestaurantDto(restaurantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PutMapping(path = "/{restaurantId}")
    public ResponseEntity<RestaurantDto> updateRestaurant(@PathVariable("restaurantId") String restaurantId,
                                                          @Valid @RequestBody RestaurantCreateUpdateRequestDto requestDto) {
        RestaurantDto updatedRestaurantDto = restaurantService.updateRestaurant(restaurantId, requestDto);

        return ResponseEntity.ok(updatedRestaurantDto);
    }
}
