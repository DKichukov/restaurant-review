package com.project.restaurant_review.service.impl;

import com.project.restaurant_review.dto.RestaurantDto;
import com.project.restaurant_review.dto.ReviewCreateUpdateRequestDto;
import com.project.restaurant_review.dto.ReviewDto;
import com.project.restaurant_review.entity.Photo;
import com.project.restaurant_review.entity.Restaurant;
import com.project.restaurant_review.entity.Review;
import com.project.restaurant_review.entity.User;
import com.project.restaurant_review.exception.RestaurantNotFoundException;
import com.project.restaurant_review.exception.ReviewNotAllowedException;
import com.project.restaurant_review.mapper.RestaurantMapper;
import com.project.restaurant_review.mapper.ReviewMapper;
import com.project.restaurant_review.repository.RestaurantRepository;
import com.project.restaurant_review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final LocalDateTime now = LocalDateTime.now();

    private final RestaurantRepository restaurantRepository;

    private final ReviewMapper reviewMapper;

    private final RestaurantMapper restaurantMapper;

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

    @Override
    public Page<ReviewDto> lastReviews(String restaurantId,
                                       Pageable pageable) {

        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        RestaurantDto restaurantDto = restaurantMapper.toRestaurantDto(restaurant);
        List<ReviewDto> reviews = restaurantDto.getReviews();

        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            var isAccending = order.getDirection().isAscending();

            var comparator = switch (property) {
                case "datePosted" -> Comparator.comparing(ReviewDto::getDatePosted);
                case "rating" -> Comparator.comparing(ReviewDto::getRating);
                default -> Comparator.comparing(ReviewDto::getDatePosted);
            };

            reviews.sort(isAccending ? comparator : comparator.reversed());
        } else {
            reviews.sort(Comparator.comparing(ReviewDto::getDatePosted).reversed());
        }
        var start = (int) pageable.getOffset();

        if (start >= reviews.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, reviews.size());
        }

        var end = Math.min(start + pageable.getPageSize(), reviews.size());
        return new PageImpl<>(reviews.subList(start, end), pageable, reviews.size());

    }

    @Override
    public Optional<ReviewDto> getReview(String restaurantId,
                                         String reviewId) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);

        return getReviewDtoFromRestaurant(reviewId, restaurant);

    }

    private Optional<ReviewDto> getReviewDtoFromRestaurant(String reviewId,
                                                           Restaurant restaurant) {
        return restaurant.getReviews().stream()
                .filter(r -> reviewId.equals(r.getId()))
                .findFirst()
                .map(reviewMapper::toReviewDto);
    }

    @Override
    public ReviewDto updateReview(User author,
                                  String restaurantId,
                                  String reviewId,
                                  ReviewCreateUpdateRequestDto reviewRequest) {
        Restaurant restaurant = getRestaurantOrThrow(restaurantId);
        String authorId = author.getId();
        ReviewDto reviewDtoFromRestaurant = getReviewDtoFromRestaurant(reviewId, restaurant)
                .orElseThrow(() -> new ReviewNotAllowedException("Review does not exist"));
        var existingReview = reviewMapper.toReview(reviewDtoFromRestaurant);

        if (!existingReview.getWrittenBy().getId().equals(authorId)) {
            throw new ReviewNotAllowedException("Cannot update another user's review");
        }
        if (now.isAfter(existingReview.getDatePosted().plusHours(48))) {
            throw new ReviewNotAllowedException("Review can no longer be edited");
        }

        existingReview.setContent(reviewRequest.getContent());
        existingReview.setRating(reviewRequest.getRating());
        existingReview.setLastEdited(now);
        existingReview.setPhotos(reviewRequest.getPhotoIds().stream()
                .map(photoId -> Photo
                        .builder()
                        .url(photoId)
                        .uploadDate(now)
                        .build()).toList());

        List<Review> updatedReviews = restaurant.getReviews().stream()
                .filter(r -> !reviewId.equals(r.getId()))
                .collect(Collectors.toList());

        updatedReviews.add(existingReview);

        updateRestaurantAverageRating(restaurant);

        restaurant.setReviews(updatedReviews);

        restaurantRepository.save(restaurant);

        return reviewMapper.toReviewDto(existingReview);
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
