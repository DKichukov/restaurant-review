package com.project.restaurant_review.mapper;

import com.project.restaurant_review.dto.ReviewCreateUpdateRequest;
import com.project.restaurant_review.dto.ReviewCreateUpdateRequestDto;
import com.project.restaurant_review.dto.ReviewDto;
import com.project.restaurant_review.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    ReviewCreateUpdateRequest toReviewCreateUpdateRequest(ReviewCreateUpdateRequestDto reviewCreateUpdateRequestDto);

    ReviewDto toReviewDto(Review review);

}
