package com.homestay.homestayweb.service.implementation;

import com.homestay.homestayweb.dto.request.HomestayRequest;
import com.homestay.homestayweb.dto.response.HomestayResponse;
import com.homestay.homestayweb.entity.Homestay;
import com.homestay.homestayweb.exception.BadRequestException;
import com.homestay.homestayweb.exception.DuplicateResourceException;
import com.homestay.homestayweb.exception.ResourceNotFoundException;
import com.homestay.homestayweb.repository.HomestayRepository;
import com.homestay.homestayweb.service.HomestayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomestayServiceImpl implements HomestayService {

    private final HomestayRepository homestayRepository;

    @Override
    public HomestayResponse createHomestay(HomestayRequest request) {
        // Kiểm tra tên
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Tên homestay không được để trống.");
        }

        // Kiểm tra định dạng email
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, request.getContactInfo())) {
            throw new BadRequestException("Email không đúng định dạng.");
        }

        // Kiểm tra email trùng
        if (homestayRepository.existsByContactInfo(request.getContactInfo())) {
            throw new DuplicateResourceException("Email đã tồn tại.");
        }

        // Kiểm tra trùng địa chỉ
        if (homestayRepository.existsByStreetAndWardAndDistrict(
                request.getStreet(), request.getWard(), request.getDistrict())) {
            throw new DuplicateResourceException("Địa chỉ homestay đã tồn tại.");
        }

        // Tạo homestay
        Homestay homestay = Homestay.builder()
                .name(request.getName())
                .street(request.getStreet())
                .ward(request.getWard())
                .district(request.getDistrict())
                .description(request.getDescription())
                .surfRating(request.getSurfRating())
                .approveStatus(request.getApproveStatus())
                .approvedBy(request.getApprovedBy())
                .contactInfo(request.getContactInfo())
                .build();
        homestayRepository.save(homestay);
        return mapToResponse(homestay);
    }

    @Override
    public HomestayResponse getHomestayById(Long id) {
        Homestay homestay = homestayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homestay not found"));
        return mapToResponse(homestay);
    }

    @Override
    public List<HomestayResponse> getAllHomestays() {
        return homestayRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HomestayResponse updateHomestay(Long id, HomestayRequest request) {
        Homestay homestay = homestayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homestay not found"));

        homestay.setName(request.getName());
        homestay.setStreet(request.getStreet());
        homestay.setWard(request.getWard());
        homestay.setDistrict(request.getDistrict());
        homestay.setDescription(request.getDescription());
        homestay.setSurfRating(request.getSurfRating());
        homestay.setApproveStatus(request.getApproveStatus());
        homestay.setApprovedBy(request.getApprovedBy());
        homestay.setContactInfo(request.getContactInfo());

        homestayRepository.save(homestay);
        return mapToResponse(homestay);
    }

    @Override
    public void deleteHomestay(Long id) {
        if (!homestayRepository.existsById(id)) {
            throw new ResourceNotFoundException("Homestay not found");
        }
        homestayRepository.deleteById(id);
    }

    private HomestayResponse mapToResponse(Homestay homestay) {
        return HomestayResponse.builder()
                .id(homestay.getHomestayId())
                .name(homestay.getName())
                .street(homestay.getStreet())
                .ward(homestay.getWard())
                .district(homestay.getDistrict())
                .description(homestay.getDescription())
                .surfRating(homestay.getSurfRating())
                .approveStatus(homestay.getApproveStatus())
                .approvedBy(homestay.getApprovedBy())
                .contactInfo(homestay.getContactInfo())
                .createdAt(homestay.getCreatedAt())
                .build();
    }
}
