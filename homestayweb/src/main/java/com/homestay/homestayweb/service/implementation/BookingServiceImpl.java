package com.homestay.homestayweb.service.implementation;

import com.homestay.homestayweb.dto.request.BookingRequest;
import com.homestay.homestayweb.dto.response.BookingResponse;
import com.homestay.homestayweb.entity.Booking;
import com.homestay.homestayweb.entity.Room;
import com.homestay.homestayweb.entity.User;
import com.homestay.homestayweb.exception.ResourceNotFoundException;
import com.homestay.homestayweb.repository.BookingRepository;
import com.homestay.homestayweb.repository.RoomRepository;
import com.homestay.homestayweb.repository.UserRepository;
import com.homestay.homestayweb.security.UserDetailsImpl;
import com.homestay.homestayweb.service.BookingService;
import com.homestay.homestayweb.utils.BookingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
        return mapToResponse(booking);
    }

    @Override
    public BookingResponse createBooking(BookingRequest request, UserDetailsImpl currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + currentUser.getId()));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));

        Booking booking = Booking.builder()
                .user(user)
                .room(room)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .totalPrice(BookingUtil.calculatePrice(room, request.getCheckInDate(), request.getCheckOutDate()))
                .bookingStatus("PENDING")
                .createdAt(LocalDate.now())
                .build();

        Booking saved = bookingRepository.save(booking);
        return mapToResponse(saved);
    }

    @Override
    public BookingResponse updateBooking(Long id, BookingRequest request) {
        Booking existing = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        existing.setCheckInDate(request.getCheckInDate());
        existing.setCheckOutDate(request.getCheckOutDate());
        existing.setTotalPrice(BookingUtil.calculatePrice(existing.getRoom(), request.getCheckInDate(), request.getCheckOutDate()));
        if (request.getBookingStatus() != null) {
            existing.setBookingStatus(request.getBookingStatus());
        }

        Booking updated = bookingRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    @Override
    public List<BookingResponse> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUser_Id(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoom_RoomId(roomId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .totalPrice(booking.getTotalPrice())
                .userId(booking.getUser().getId())
                .roomId(booking.getRoom().getRoomId())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
