package com.project.restaurant_review.service.impl;

import com.project.restaurant_review.dto.PhotoDto;
import com.project.restaurant_review.entity.Photo;
import com.project.restaurant_review.mapper.PhotoMapper;
import com.project.restaurant_review.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final FileSystemStorageService storageService;

    private final PhotoMapper photoMapper;

    @Override
    public PhotoDto uploadPhoto(MultipartFile file) {

        var photoId = UUID.randomUUID().toString();
        String url = storageService.store(file, photoId);
        LocalDateTime uploadDate = LocalDateTime.now();

        Photo photo = Photo.builder()
                .url(url)
                .uploadDate(uploadDate)
                .build();

        return photoMapper.toDto(photo);
    }

    @Override
    public Optional<Resource> getPhotoAsResource(String fileName) {
        return storageService.loadAsResource(fileName);
    }

}
