package com.homestay.homestayweb.repository;

import com.homestay.homestayweb.dto.response.BookingResponse;
import com.homestay.homestayweb.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_Id(Long userId);
    List<Booking> findByRoom_RoomId(Long roomId);
    @Query("SELECT b FROM Booking b WHERE b.room.homestay.host.id = :hostId AND b.bookingStatus = 'PENDING'")
    List<Booking> findPendingByHostId(@Param("hostId") Long hostId);

}