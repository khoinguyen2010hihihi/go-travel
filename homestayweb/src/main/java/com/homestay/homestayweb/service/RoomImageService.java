package com.homestay.homestayweb.service;

import com.homestay.homestayweb.entity.RoomImage;

public interface RoomImageService {
    void saveRoomImage(RoomImage roomImage);
    void uploadImageForRoom(Long roomId, String imageUrl);
}
