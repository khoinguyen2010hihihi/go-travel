package com.homestay.homestayweb.service.impl;

import com.homestay.homestayweb.entity.Homestay;
import com.homestay.homestayweb.entity.HomestayImage;
import com.homestay.homestayweb.repository.HomestayImageRepository;
import com.homestay.homestayweb.service.HomestayImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomestayImageServiceImpl implements HomestayImageService {

    private final HomestayImageRepository homestayImageRepository;

    @Override
    public HomestayImage getPrimaryImage(Homestay homestay) {
        // Tìm ảnh chính của homestay
        return homestayImageRepository.findByHomestayAndIsPrimaryTrue(homestay);
    }

    @Override
    public void saveHomestayImage(HomestayImage homestayImage) {
        // Lưu ảnh vào DB
        homestayImageRepository.save(homestayImage);
    }
}
