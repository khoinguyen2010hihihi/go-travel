package com.homestay.homestayweb.service;

import com.homestay.homestayweb.dto.request.BookingRequest;
import com.homestay.homestayweb.dto.response.BookingResponse;
import com.homestay.homestayweb.security.UserDetailsImpl;

import java.util.List;

public interface BookingService {
    List<BookingResponse> getAllBookings();
    BookingResponse getBookingById(Long id);
    BookingResponse createBooking(BookingRequest request, UserDetailsImpl currentUser);
    BookingResponse updateBooking(Long id, BookingRequest request);
    void deleteBooking(Long id);
    List<BookingResponse> getBookingsByUserId(Long userId);
    List<BookingResponse> getBookingsByRoomId(Long roomId);
}
