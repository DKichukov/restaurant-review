package com.project.restaurant_review.controller;

import com.project.restaurant_review.dto.ReviewCreateUpdateRequestDto;
import com.project.restaurant_review.dto.ReviewDto;
import com.project.restaurant_review.entity.User;
import com.project.restaurant_review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@PathVariable String restaurantId,
                                                  @Valid @RequestBody ReviewCreateUpdateRequestDto reviewDto,
                                                  @AuthenticationPrincipal Jwt jwt) {
        User user = jwtToUser(jwt);
        ReviewDto createdReviewDto = reviewService.createReview(user, restaurantId, reviewDto);

        return ResponseEntity.ok(createdReviewDto);
    }

    @GetMapping
    public Page<ReviewDto> listReviews(@PathVariable String restaurantId,
                                       @PageableDefault(size = 20, sort = "datePosted", direction = DESC) Pageable pageable) {
        return reviewService.lastReviews(restaurantId, pageable);
    }

    @GetMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable String restaurantId,
                                               @PathVariable String reviewId) {
        return reviewService.getReview(restaurantId, reviewId).map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    @PutMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable String restaurantId,
                                                  @PathVariable String reviewId,
                                                  @Valid @RequestBody ReviewCreateUpdateRequestDto reviewDto,
                                                  @AuthenticationPrincipal Jwt jwt) {
        User user = jwtToUser(jwt);
        ReviewDto updateReview = reviewService.updateReview(user, restaurantId, reviewId, reviewDto);

        return ResponseEntity.ok(updateReview);
    }

    private User jwtToUser(Jwt jwt) {
        return User.builder().id(jwt.getSubject()).username(jwt.getClaimAsString("preferred_username")).givenName(jwt.getClaimAsString("given_name")).familyName(jwt.getClaimAsString("family_name")).build();
    }

}
