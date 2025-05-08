package com.homestay.homestayweb.service.implementation;

import com.homestay.homestayweb.entity.Room;
import com.homestay.homestayweb.entity.RoomImage;
import com.homestay.homestayweb.exception.ForbiddenException;
import com.homestay.homestayweb.repository.RoomImageRepository;
import com.homestay.homestayweb.repository.RoomRepository;
import com.homestay.homestayweb.security.UserDetailsImpl;
import com.homestay.homestayweb.service.RoomImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomImageServiceImpl implements RoomImageService {

    private final RoomImageRepository roomImageRepository;
    private final RoomRepository roomRepository;

    @Override
    public void saveRoomImage(RoomImage roomImage) {
        roomImageRepository.save(roomImage);
    }

    private void checkRoomOwnership(Room room) {
        UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long currentUserId = currentUser.getId();
        Long roomOwnerId = room.getHomestay().getHost().getId();

        if (!currentUserId.equals(roomOwnerId)) {
            throw new ForbiddenException("Bạn không có quyền thực hiện hành động này với phòng này.");
        }
    }

    public void uploadImageForRoom(Long roomId, String imageUrl) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Kiểm tra quyền sở hữu phòng
        checkRoomOwnership(room);

        RoomImage roomImage = new RoomImage();
        roomImage.setRoom(room);
        roomImage.setImageUrl(imageUrl);

        saveRoomImage(roomImage);
    }
}
