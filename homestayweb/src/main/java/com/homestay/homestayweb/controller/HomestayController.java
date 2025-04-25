package com.homestay.homestayweb.controller;

import com.homestay.homestayweb.dto.request.HomestayRequest;
import com.homestay.homestayweb.dto.response.HomestayResponse;
import com.homestay.homestayweb.security.UserDetailsImpl;
import com.homestay.homestayweb.service.HomestayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<HomestayResponse> homestays = homestayService.getHomestaysByHostId(userDetails.getId());
        return ResponseEntity.ok(homestays);
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

    @GetMapping("/slide/{district}")
    public ResponseEntity<List<HomestayResponse>> getAllByDistrict(@PathVariable String district, String status) {
        return ResponseEntity.ok(homestayService.getAllByDistrict(district,status));
    }
}