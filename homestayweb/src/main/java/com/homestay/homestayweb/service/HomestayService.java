package com.homestay.homestayweb.service;

import com.homestay.homestayweb.dto.request.HomestayCreationRequest;
import com.homestay.homestayweb.dto.request.HomestayUpdateRequest;
import com.homestay.homestayweb.entity.Homestay;
import com.homestay.homestayweb.repository.HomestayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HomestayService {
    @Autowired
    private HomestayRepository homestayRepository;

    // Tạo mới Homestay
    public Homestay createHomestay(HomestayCreationRequest request) {
        Homestay homestay = new Homestay();
        homestay.setName(request.getName());
        homestay.setStreet(request.getStreet());
        homestay.setWard(request.getWard());
        homestay.setDistrict(request.getDistrict());
        homestay.setDescription(request.getDescription());
        homestay.setSurfRating(request.getSurfRating());
        homestay.setApproveStatus("PENDING");
        homestay.setApprovedBy(null);
        homestay.setContactInfo(request.getContactInfo());

        //Lưu vào database
        return homestayRepository.save(homestay);
    }

    // Lấy tất cả Homestay
    public List<Homestay> getAllHomestays() {
        return homestayRepository.findAll();
    }

    // Lấy Homestay theo ID
    public Optional<Homestay> getHomestayById(Long id) {
        return homestayRepository.findById(id);
    }

    // Xóa Homestay theo ID
    public boolean deleteHomestay(Long id) {
        if (homestayRepository.existsById(id)) {
            homestayRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Homestay updateHomestay(Long id, HomestayUpdateRequest request) {
        return homestayRepository.findById(id)
                .map(existingHomestay -> {
                    existingHomestay.setName(request.getName());
                    existingHomestay.setStreet(request.getStreet());
                    existingHomestay.setWard(request.getWard());
                    existingHomestay.setDistrict(request.getDistrict());
                    existingHomestay.setDescription(request.getDescription());
                    existingHomestay.setSurfRating(request.getSurfRating());
                    existingHomestay.setContactInfo(request.getContactInfo());

                    return homestayRepository.save(existingHomestay);
                })
                .orElseThrow(() -> new RuntimeException("Homestay not found with id: " + id));
    }
}
