package com.homestay.homestayweb.controller;

import com.homestay.homestayweb.dto.request.HomestayRequest;
import com.homestay.homestayweb.dto.response.HomestayImageResponse;
import com.homestay.homestayweb.dto.response.HomestayResponse;
import com.homestay.homestayweb.entity.Homestay;
import com.homestay.homestayweb.entity.HomestayImage;
import com.homestay.homestayweb.service.CloudinaryService;
import com.homestay.homestayweb.service.HomestayImageService;
import com.homestay.homestayweb.service.HomestayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/homestays")
@RequiredArgsConstructor
public class HomestayController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private HomestayImageService homestayImageService;

    @Autowired
    private HomestayService homestayService;

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
    public ResponseEntity<List<HomestayResponse>> getAll() {
        return ResponseEntity.ok(homestayService.getAllHomestays());
    }

    @GetMapping("/pending")
//    @PreAuthorize("hasAuthority('ADMIN_ACCESS')")
    public ResponseEntity<List<HomestayResponse>> getAllP() {
        return ResponseEntity.ok(homestayService.getAllPendingHomestays());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomestayResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(homestayService.getHomestayById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<HomestayResponse>> getMyHomestays() {
        return ResponseEntity.ok(homestayService.getMyHomestays());
    }

    @GetMapping("/host/{host_id}")
    public ResponseEntity<List<HomestayResponse>> getByHost(@PathVariable Long host_id) {
        return ResponseEntity.ok(homestayService.getHomestayByHost(host_id));
    }

    @PutMapping("/admin/pending/{id}")
    @PreAuthorize("hasAuthority('ADMIN_ACCESS')")
    public ResponseEntity<HomestayResponse> pending(@PathVariable Long id) {
        return ResponseEntity.ok(homestayService.pendingHomestay(id));
    }

    @PutMapping("/admin/reject/{id}")
    @PreAuthorize("hasAuthority('ADMIN_ACCESS')")
    public ResponseEntity<HomestayResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(homestayService.rejectHomestay(id));
    }

    @GetMapping("/slide/{district}")
    public ResponseEntity<List<HomestayResponse>> getAllByDistrict(@PathVariable String district, String status) {
        return ResponseEntity.ok(homestayService.getAllByDistrict(district,status));
    }

    @GetMapping("/{id}/images/primary")
    public ResponseEntity<Map<String, String>> getPrimaryImage(@PathVariable Long id) {
        Homestay homestay = homestayService.findEntityById(id);
        HomestayImage primaryImage = homestayImageService.getPrimaryImage(homestay);

        if (primaryImage == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Map<String, String> response = new HashMap<>();
        response.put("primaryImageUrl", primaryImage.getImageUrl());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{homestayId}/images")
    @PreAuthorize("hasAuthority('CREATE_HOMESTAY')")
    public ResponseEntity<?> uploadImagesToHomestay(
            @PathVariable Long homestayId,
            @RequestParam("file") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return ResponseEntity.badRequest().body("Không có file nào được gửi lên.");
        }

        List<String> uploadedUrls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                String imageUrl = cloudinaryService.uploadFile(file);
                homestayImageService.uploadImageForHomestay(homestayId, imageUrl);
                uploadedUrls.add(imageUrl);
            }
            return ResponseEntity.ok(uploadedUrls);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload thất bại: " + e.getMessage());
        }
    }

    @GetMapping("/{homestayId}/images")
    public ResponseEntity<List<HomestayImageResponse>> getHomestayImagesByHomestayId(@PathVariable Long homestayId) {
        List<HomestayImageResponse> homestayImages = homestayImageService.getHomestayImageByHomestayId(homestayId);
        return ResponseEntity.ok(homestayImages);
    }

    @GetMapping("/search")
    public List<HomestayResponse> searchHomestays(
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Double priceFrom,
            @RequestParam(required = false) Double priceTo,
            @RequestParam(required = false) String features,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestParam(required = false) Double surfRating,
            @RequestParam(required = false) String location) {

        return homestayService.searchHomestays(
                roomType, priceFrom, priceTo, features,
                checkInDate, checkOutDate, surfRating, location);
    }

}