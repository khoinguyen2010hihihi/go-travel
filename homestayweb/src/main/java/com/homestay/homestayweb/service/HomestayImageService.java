package com.homestay.homestayweb.service;

import com.homestay.homestayweb.entity.Homestay;
import com.homestay.homestayweb.entity.HomestayImage;

public interface HomestayImageService {
    HomestayImage getPrimaryImage(Homestay homestay);
    void saveHomestayImage(HomestayImage homestayImage);
}