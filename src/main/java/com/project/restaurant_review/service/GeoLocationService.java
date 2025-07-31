package com.project.restaurant_review.service;

import com.project.restaurant_review.entity.Address;
import com.project.restaurant_review.entity.GeoLocation;

public interface GeoLocationService {

    GeoLocation geoLocate(Address address);

}
