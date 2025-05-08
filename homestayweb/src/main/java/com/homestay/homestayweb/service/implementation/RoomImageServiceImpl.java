package com.homestay.homestayweb.service.implementation;

import com.homestay.homestayweb.entity.RoomImage;
import com.homestay.homestayweb.repository.RoomImageRepository;
import com.homestay.homestayweb.service.RoomImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomImageServiceImpl implements RoomImageService {

    private final RoomImageRepository roomImageRepository;

    @Override
    public void saveRoomImage(RoomImage roomImage) {
        roomImageRepository.save(roomImage);
    }
}
