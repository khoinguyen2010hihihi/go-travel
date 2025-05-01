package com.homestay.homestayweb.service.implementation;

import com.homestay.homestayweb.dto.request.RoomRequest;
import com.homestay.homestayweb.dto.response.RoomResponse;
import com.homestay.homestayweb.entity.Homestay;
import com.homestay.homestayweb.entity.Room;
import com.homestay.homestayweb.exception.ForbiddenException;
import com.homestay.homestayweb.repository.HomestayRepository;
import com.homestay.homestayweb.repository.RoomRepository;
import com.homestay.homestayweb.security.UserDetailsImpl;
import com.homestay.homestayweb.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HomestayRepository homestayRepository;

    @Override
    public RoomResponse createRoom(Long homestayId, RoomRequest request) {
        Homestay homestay = homestayRepository.findById(homestayId)
                .orElseThrow(() -> new RuntimeException("Homestay not found"));

        Room room = Room.builder()
                .homestay(homestay)
                .roomType(request.getRoomType())
                .price(request.getPrice())
                .availability(request.getAvailability())
                .features(request.getFeatures())
                .build();

        roomRepository.save(room);
        return mapToResponse(room);
    }

    @Override
    public List<RoomResponse> getRoomsByHomestay(Long homestayId) {
        return roomRepository.findByHomestay_HomestayId(homestayId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponse getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return mapToResponse(room);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findByRoomStatus("ACCEPTED")
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponse updateRoom(Long roomId, RoomRequest request) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        checkRoomOwnership(room);

        room.setRoomType(request.getRoomType());
        room.setPrice(request.getPrice());
        room.setAvailability(request.getAvailability());

        roomRepository.save(room);
        return mapToResponse(room);
    }

    @Override
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        checkRoomOwnership(room);

        roomRepository.delete(room);
    }

    private RoomResponse mapToResponse(Room room) {
        return RoomResponse.builder()
                .roomId(room.getRoomId())
                .homestayName(room.getHomestay().getName())
                .roomType(room.getRoomType())
                .price(room.getPrice())
                .availability(room.getAvailability())
                .features(room.getFeatures())
                .build();
    }

    private void checkRoomOwnership(Room room) {
        UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long currentUserId = currentUser.getId();
        Long roomOwnerId = room.getHomestay().getHost().getId();

        if (!currentUserId.equals(roomOwnerId)) {
            throw new ForbiddenException("Bạn không có quyền thực hiện hành động này");
        }
    }
}