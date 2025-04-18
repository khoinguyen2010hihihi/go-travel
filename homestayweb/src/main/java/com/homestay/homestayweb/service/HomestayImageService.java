package com.homestay.homestayweb.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HomestayImageService {
    void uploadImages(Long homestayId, List<MultipartFile> images);
}