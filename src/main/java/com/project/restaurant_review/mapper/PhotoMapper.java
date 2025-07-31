package com.project.restaurant_review.mapper;

import com.project.restaurant_review.dto.PhotoDto;
import com.project.restaurant_review.entity.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {

    PhotoDto toDto(Photo photo);



}
