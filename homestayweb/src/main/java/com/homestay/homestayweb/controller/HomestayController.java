package com.homestay.homestayweb.controller;

import com.homestay.homestayweb.dto.request.HomestayRequest;
import com.homestay.homestayweb.dto.response.HomestayResponse;
import com.homestay.homestayweb.entity.HomestayImage;
import com.homestay.homestayweb.repository.HomestayImageRepository;
import com.homestay.homestayweb.security.UserDetailsImpl;
import com.homestay.homestayweb.service.CloudinaryService;
import com.homestay.homestayweb.service.HomestayService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/homestays")
@RequiredArgsConstructor
public class HomestayController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private HomestayImageRepository homestayImageRepository;

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
                System.out.println("123");
        return ResponseEntity.ok(homestayService.getAllHomestays());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomestayResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(homestayService.getHomestayById(id));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('VIEW_HOMESTAY')")
    public ResponseEntity<List<HomestayResponse>> getMyHomestays() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<HomestayResponse> homestays = homestayService.getHomestaysByHostId(userDetails.getId());
        return ResponseEntity.ok(homestays);
    }

    @PutMapping("/admin/pending/{id}")
    @PreAuthorize("hasAuthority('ADMIN_ACCESS')")
    public ResponseEntity<HomestayResponse> pending(@PathVariable Long id) {
        return ResponseEntity.ok(homestayService.pendingHomestay(id));
    }

    @GetMapping("/slide/{district}")
    public ResponseEntity<List<HomestayResponse>> getAllByDistrict(@PathVariable String district) {
        return ResponseEntity.ok(homestayService.getAllByDistrict(district));
    }

    @PostMapping("/{id}/images")
    @PreAuthorize("hasAuthority('CREATE_HOMESTAY')")
    public ResponseEntity<String> uploadImageToHomestay(
            @PathVariable("id") Long homestayId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "isPrimary", defaultValue = "false") boolean isPrimary
    ) {
        try {
            // Upload ảnh lên Cloudinary
            String imageUrl = cloudinaryService.uploadFile(file);

            // Gán Homestay cho ảnh
            HomestayImage image = new HomestayImage();
            image.setImageUrl(imageUrl);
            image.setIsPrimary(isPrimary);
            image.setHomestay(homestayService.findEntityById(homestayId));

            // Lưu vào DB
            homestayImageRepository.save(image);

            return ResponseEntity.ok(imageUrl);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload thất bại: " + e.getMessage());
        }
    }
}