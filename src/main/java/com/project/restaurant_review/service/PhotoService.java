package com.project.restaurant_review.service;

import com.project.restaurant_review.dto.PhotoDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PhotoService {

    PhotoDto uploadPhoto(MultipartFile file);

    Optional<Resource> getPhotoAsResource(String id);

}
