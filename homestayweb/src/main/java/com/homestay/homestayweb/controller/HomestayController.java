package com.homestay.homestayweb.controller;

import com.homestay.homestayweb.dto.request.HomestayRequest;
import com.homestay.homestayweb.dto.response.HomestayResponse;
import com.homestay.homestayweb.service.HomestayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homestays")
@RequiredArgsConstructor
public class HomestayController {

    private final HomestayService homestayService;

    @PostMapping
    public ResponseEntity<HomestayResponse> create(@RequestBody HomestayRequest request) {
        return ResponseEntity.ok(homestayService.createHomestay(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomestayResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(homestayService.getHomestayById(id));
    }

    @GetMapping
    public ResponseEntity<List<HomestayResponse>> getAll() {
        return ResponseEntity.ok(homestayService.getAllHomestays());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HomestayResponse> update(@PathVariable Long id, @RequestBody HomestayRequest request) {
        return ResponseEntity.ok(homestayService.updateHomestay(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        homestayService.deleteHomestay(id);
        return ResponseEntity.noContent().build();
    }
}
