package com.project.restaurant_review.service;

import com.project.restaurant_review.entity.Photo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PhotoService {

    Photo upladPhoto(MultipartFile file);

    Optional<Resource> getPhotoAsResource(String fileName);

}
