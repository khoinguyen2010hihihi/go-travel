package com.homestay.homestayweb.repository;

import com.homestay.homestayweb.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByBooking_BookingId(Long bookingId);
}
