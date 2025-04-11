package com.homestay.homestayweb.controller;

import com.homestay.homestayweb.dto.request.RoomRequest;
import com.homestay.homestayweb.dto.response.RoomResponse;
import com.homestay.homestayweb.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/homestay/{homestayId}")
    public ResponseEntity<RoomResponse> createRoom(@PathVariable Long homestayId,
                                                   @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.createRoom(homestayId, request));
    }

    @GetMapping("/homestay/{homestayId}")
    public ResponseEntity<List<RoomResponse>> getRoomsByHomestay(@PathVariable Long homestayId) {
        return ResponseEntity.ok(roomService.getRoomsByHomestay(homestayId));
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(roomId, request));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}

