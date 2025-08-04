package com.project.restaurant_review.controller;

import com.project.restaurant_review.dto.ReviewCreateUpdateRequestDto;
import com.project.restaurant_review.dto.ReviewDto;
import com.project.restaurant_review.entity.User;
import com.project.restaurant_review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
private final ReviewService  reviewService;

        @PostMapping
        public ResponseEntity<ReviewDto> createReview(@PathVariable String restaurantId,
                                                      @Valid @RequestBody ReviewCreateUpdateRequestDto reviewDto,
                                                      @AuthenticationPrincipal
                                                      Jwt jwt) {
            User user = jwtToUser(jwt);
            ReviewDto createdReviewDto = reviewService.createReview(user, restaurantId, reviewDto);

            return ResponseEntity.ok(createdReviewDto);
        }

    private User jwtToUser(Jwt jwt) {
        return User
                .builder()
                .id(jwt.getSubject())
                .username(jwt.getClaimAsString("preferred_username"))
                .givenName(jwt.getClaimAsString("given_name"))
                .familyName(jwt.getClaimAsString("family_name"))
                .build();
    }

}
