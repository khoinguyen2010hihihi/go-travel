package com.homestay.homestayweb.controller;

import com.homestay.homestayweb.dto.request.HomestayRequest;
import com.homestay.homestayweb.dto.response.HomestayResponse;
import com.homestay.homestayweb.service.HomestayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homestays")
@RequiredArgsConstructor
public class HomestayController {

    private final HomestayService homestayService;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_HOMESTAY')")
    public ResponseEntity<HomestayResponse> create(@RequestBody HomestayRequest request) {
        return ResponseEntity.ok(homestayService.createHomestay(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_HOMESTAY')")
    public ResponseEntity<HomestayResponse> update(@PathVariable Long id, @RequestBody HomestayRequest request) {
        return ResponseEntity.ok(homestayService.updateHomestay(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_HOMESTAY')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        homestayService.deleteHomestay(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_HOMESTAY')")
    public ResponseEntity<List<HomestayResponse>> getAll() {
        return ResponseEntity.ok(homestayService.getAllHomestays());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_HOMESTAY')")
    public ResponseEntity<HomestayResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(homestayService.getHomestayById(id));
    }
}
