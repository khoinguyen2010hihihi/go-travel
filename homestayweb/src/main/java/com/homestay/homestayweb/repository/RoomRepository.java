package com.homestay.homestayweb.repository;

import com.homestay.homestayweb.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHomestay_HomestayId(Long homestayId);
}
