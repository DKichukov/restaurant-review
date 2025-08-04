package com.project.restaurant_review.service.impl;

import com.project.restaurant_review.dto.ReviewCreateUpdateRequestDto;
import com.project.restaurant_review.dto.ReviewDto;
import com.project.restaurant_review.entity.Photo;
import com.project.restaurant_review.entity.Restaurant;
import com.project.restaurant_review.entity.Review;
import com.project.restaurant_review.entity.User;
import com.project.restaurant_review.exception.RestaurantNotFoundException;
import com.project.restaurant_review.exception.ReviewNotAllowedException;
import com.project.restaurant_review.mapper.ReviewMapper;
import com.project.restaurant_review.repository.RestaurantRepository;
import com.project.restaurant_review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final LocalDateTime now = LocalDateTime.now();

    private final RestaurantRepository restaurantRepository;

    private final ReviewMapper reviewMapper;

    @Override
    public ReviewDto createReview(User author,
                                  String restaurantId,
                                  ReviewCreateUpdateRequestDto reviewRequestDto) {

        var reviewRequest = reviewMapper.toReviewCreateUpdateRequest(reviewRequestDto);
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        boolean hasExistingReview = restaurant.getReviews().stream().
                anyMatch(r -> r.getWrittenBy().getId().equals(author.getId()));

        if (hasExistingReview) {
            throw new ReviewNotAllowedException("User has already reviewed this restaurant");
        }

        List<Photo> photos = reviewRequest.getPhotoIds().stream()
                .map(url -> Photo
                        .builder()
                        .url(url)
                        .uploadDate(now)
                        .build()).toList();

        String reviewId = UUID.randomUUID().toString();

        var reviewToCreate = Review.builder()
                .id(reviewId)
                .content(reviewRequest.getContent())
                .rating(reviewRequest.getRating())
                .photos(photos)
                .datePosted(now)
                .lastEdited(now)
                .writtenBy(author)
                .build();

        restaurant.getReviews().add(reviewToCreate);
        updateRestaurantAverageRating(restaurant);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        Review review = savedRestaurant.getReviews().stream()
                .filter(r -> reviewId.equals(r.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Error retrieving created review"));

        return reviewMapper.toReviewDto(review);
    }

    private Restaurant getRestaurantOrThrow(String restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new RestaurantNotFoundException("Restaurant with ID %s not found".formatted(restaurantId)));
    }

    private void updateRestaurantAverageRating(Restaurant restaurant) {
        List<Review> reviews = restaurant.getReviews();
        if (reviews.isEmpty()) {
            restaurant.setAverageRating(0.0f);
        } else {
            double averageRaing = reviews.stream().mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);
            restaurant.setAverageRating((float) averageRaing);
        }
    }

}
