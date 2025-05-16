package com.homestay.homestayweb.controller;

import com.homestay.homestayweb.dto.request.BookingRequest;
import com.homestay.homestayweb.dto.response.BookingResponse;
import com.homestay.homestayweb.security.UserDetailsImpl;
import com.homestay.homestayweb.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAll() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/pending/customer/{id}")
    public ResponseEntity<List<BookingResponse>> getPendingBookingByCustomer(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getPendingBookingsByUserId(id));
    }

    @GetMapping("/accepted/customer/{id}")
    public ResponseEntity<List<BookingResponse>> getAcceptedBookingByCustomer(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getAcceptedBookingsByUserId(id));
    }

    @GetMapping("/rejected/customer/{id}")
    public ResponseEntity<List<BookingResponse>> getRejectedBookingByCustomer(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.getRejectedBookingsByUserId(id));
    }

    @PostMapping
    public ResponseEntity<BookingResponse> create(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        BookingResponse resp = bookingService.createBooking(request, currentUser);
        return ResponseEntity.status(201).body(resp);
    }

    @PutMapping("host/pending/{id}")
    public ResponseEntity<BookingResponse> pending(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.pendingBooking(id));
    }

    @PutMapping("host/reject/{id}")
    public ResponseEntity<BookingResponse> reject(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.rejectBooking(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pending/{hostId}")
    public ResponseEntity<List<BookingResponse>> getPendingBookingsByHost(@PathVariable Long hostId) {
        List<BookingResponse> bookings = bookingService.getBookingsForHost(hostId);
        return ResponseEntity.ok(bookings);
    }

}