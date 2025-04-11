package com.homestay.homestayweb.repository;

import com.homestay.homestayweb.entity.Homestay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HomestayRepository extends JpaRepository<Homestay, Long> {
    boolean existsByContactInfo(String contactInfo);

    boolean existsByStreetAndWardAndDistrict(String street, String ward, String district);
}
