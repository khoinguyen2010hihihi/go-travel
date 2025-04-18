package com.homestay.homestayweb.service;

import com.homestay.homestayweb.dto.request.RoomRequest;
import com.homestay.homestayweb.dto.response.RoomResponse;

import java.util.List;

public interface RoomService {
    RoomResponse createRoom(Long homestayId, RoomRequest request);
    List<RoomResponse> getRoomsByHomestay(Long homestayId);
    RoomResponse updateRoom(Long roomId, RoomRequest request);
    void deleteRoom(Long roomId);
    RoomResponse getRoomById(Long roomId);

    List<RoomResponse> getAllRooms();
}