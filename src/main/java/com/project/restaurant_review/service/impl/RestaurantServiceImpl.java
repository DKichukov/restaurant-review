package com.project.restaurant_review.service.impl;

import com.project.restaurant_review.dto.RestaurantCreateUpdateRequestDto;
import com.project.restaurant_review.dto.RestaurantDto;
import com.project.restaurant_review.dto.RestaurantSummaryDto;
import com.project.restaurant_review.entity.Address;
import com.project.restaurant_review.entity.GeoLocation;
import com.project.restaurant_review.entity.Photo;
import com.project.restaurant_review.entity.Restaurant;
import com.project.restaurant_review.dto.RestaurantCreateUpdateRequest;
import com.project.restaurant_review.exception.RestaurantNotFoundException;
import com.project.restaurant_review.mapper.RestaurantMapper;
import com.project.restaurant_review.repository.RestaurantRepository;
import com.project.restaurant_review.service.GeoLocationService;
import com.project.restaurant_review.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final LocalDateTime uploadedDate = LocalDateTime.now();

    private final RestaurantRepository restaurantRepository;

    private final GeoLocationService geoLocationService;

    private final RestaurantMapper restaurantMapper;

    @Override
    public RestaurantDto createRestaurant(RestaurantCreateUpdateRequestDto requestDto) {
        RestaurantCreateUpdateRequest request = restaurantMapper.toRestaurantCreateUpdateRequest(requestDto);
        Address address = request.getAddress();
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

    @Override
    public Page<RestaurantSummaryDto> searchRestaurants(String query,
                                                        Float minRating,
                                                        Float latitude,
                                                        Float longitude,
                                                        Float radius,
                                                        Pageable pageable) {
        if (minRating != null && (query == null || query.isEmpty())) {

            Page<Restaurant> restaurantPage = restaurantRepository.findByAverageRatingGreaterThanEqual(minRating, pageable);
            return restaurantPage.map(restaurantMapper::toSummaryDto);
        }
        Float searchMinRating = minRating == null ? 0f : minRating;

        if (query != null && !query.trim().isEmpty()) {
            Page<Restaurant> minRatingRestaurantPage = restaurantRepository.findByQueryAndMinRating(query, searchMinRating,
                    pageable);
            return minRatingRestaurantPage.map(restaurantMapper::toSummaryDto);
        }

        if (latitude != null && longitude != null && radius != null) {
            Page<Restaurant> byLocationNearPage = restaurantRepository.findByLocationNear(latitude, longitude, radius,
                    pageable);
            return byLocationNearPage.map(restaurantMapper::toSummaryDto);
        }

        Page<Restaurant> foundRestaurants = restaurantRepository.findAll(pageable);

        return foundRestaurants.map(restaurantMapper::toSummaryDto);
    }

    @Override
    public Optional<RestaurantDto> getRestaurantDto(String id) {
        return restaurantRepository.findById(id)
                .map(restaurantMapper::toRestaurantDto);
    }

    @Override
    public RestaurantDto updateRestaurant(String id,
                                          RestaurantCreateUpdateRequestDto restaurantCreateUpdateRequestDto) {

        var restaurantCreateUpdateRequest = restaurantMapper.
                toRestaurantCreateUpdateRequest(restaurantCreateUpdateRequestDto);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(
                () -> new RestaurantNotFoundException("Restaurant with ID does not exist" + id));

        GeoLocation geoLocation = geoLocationService.geoLocate(restaurantCreateUpdateRequest.getAddress());
        GeoPoint geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());

        List<String> photoIds = restaurantCreateUpdateRequest.getPhotoIds();
        List<Photo> photos = photoIds.stream().map(photoUrl -> Photo.builder()
                .url(photoUrl)
                .uploadDate(uploadedDate)
                .build()).toList();

        restaurant.setName(restaurantCreateUpdateRequest.getName());
        restaurant.setCuisineType(restaurantCreateUpdateRequest.getCuisine());
        restaurant.setContactInformation(restaurantCreateUpdateRequest.getContactInformation());
        restaurant.setAddress(restaurantCreateUpdateRequest.getAddress());
        restaurant.setGeoLocation(geoPoint);
        restaurant.setOperatingHours(restaurantCreateUpdateRequest.getOperatingHours());
        restaurant.setPhotos(photos);

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toRestaurantDto(savedRestaurant);
    }

    @Override
    public void deleteRestaurant(String id) {
        restaurantRepository.deleteById(id);
    }


}
