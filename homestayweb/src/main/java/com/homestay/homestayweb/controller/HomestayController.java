package com.homestay.homestayweb.controller;

import com.homestay.homestayweb.dto.request.HomestayCreationRequest;
import com.homestay.homestayweb.dto.request.HomestayUpdateRequest;
import com.homestay.homestayweb.entity.Homestay;
import com.homestay.homestayweb.service.HomestayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/homestay")
public class HomestayController {
    @Autowired
    private HomestayService homestayService;

    // API tạo Homestay
    @PostMapping("/create")
    public ResponseEntity<Homestay> createHomestay(@RequestBody HomestayCreationRequest request) {
        return ResponseEntity.ok(homestayService.createHomestay(request));
    }

    // API lấy danh sách Homestay
    @GetMapping("/list")
    public ResponseEntity<List<Homestay>> getAllHomestays() {
        return ResponseEntity.ok(homestayService.getAllHomestays());
    }

    // API lấy Homestay theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Homestay> getHomestayById(@PathVariable Long id) {
        Optional<Homestay> homestay = homestayService.getHomestayById(id);
        return homestay.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // API xóa Homestay theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHomestay(@PathVariable Long id) {
        boolean deleted = homestayService.deleteHomestay(id);
        if (deleted) {
            return ResponseEntity.ok("Homestay deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // API sửa Homestay theo ID
    @PutMapping("/{id}")
    public ResponseEntity<Homestay> updateHomestay(
            @PathVariable Long id,
            @RequestBody HomestayUpdateRequest request) {
        try {
            Homestay updatedHomestay = homestayService.updateHomestay(id, request);
            return ResponseEntity.ok(updatedHomestay);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
