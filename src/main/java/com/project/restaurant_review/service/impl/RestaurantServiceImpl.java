package com.project.restaurant_review.service.impl;

import com.project.restaurant_review.dto.RestaurantCreateUpdateRequestDto;
import com.project.restaurant_review.dto.RestaurantDto;
import com.project.restaurant_review.entity.Address;
import com.project.restaurant_review.entity.GeoLocation;
import com.project.restaurant_review.entity.Photo;
import com.project.restaurant_review.entity.Restaurant;
import com.project.restaurant_review.entity.RestaurantCreateUpdateRequest;
import com.project.restaurant_review.mapper.RestaurantMapper;
import com.project.restaurant_review.repository.RestaurantRepository;
import com.project.restaurant_review.service.GeoLocationService;
import com.project.restaurant_review.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final GeoLocationService geoLocationService;

    private final RestaurantMapper restaurantMapper;

    @Override
    public RestaurantDto createRestaurant(RestaurantCreateUpdateRequestDto requestDto) {
        RestaurantCreateUpdateRequest request = restaurantMapper.toRestaurantCreateUpdateRequest(requestDto);
        Address address = request.getAddress();
        var uploadedDate = LocalDateTime.now();
        GeoLocation geoLocation = geoLocationService.geoLocate(address);
        GeoPoint geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());

        List<String> photoIds = request.getPhotoIds();
        List<Photo> photos = photoIds.stream().map(photoUrl -> Photo.builder()
                .url(photoUrl)
                .uploadDate(uploadedDate)
                .build()).toList();

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .cuisineType(request.getCuisine())
                .contactInformation(request.getContactInformation())
                .address(address)
                .geoLocation(geoPoint)
                .operatingHours(request.getOperatingHours())
                .averageRating(0f)
                .photos(photos)
                .build();

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toRestaurantDto(savedRestaurant);
    }

}
