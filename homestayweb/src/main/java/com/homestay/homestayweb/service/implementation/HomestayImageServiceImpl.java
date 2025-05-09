package com.homestay.homestayweb.service.implementation;
import com.homestay.homestayweb.entity.Homestay;
import com.homestay.homestayweb.entity.HomestayImage;
import com.homestay.homestayweb.exception.ForbiddenException;
import com.homestay.homestayweb.repository.HomestayImageRepository;
import com.homestay.homestayweb.repository.HomestayRepository;
import com.homestay.homestayweb.security.UserDetailsImpl;
import com.homestay.homestayweb.service.HomestayImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomestayImageServiceImpl implements HomestayImageService {

    private final HomestayImageRepository homestayImageRepository;
    private final HomestayRepository homestayRepository;

    @Override
    public HomestayImage getPrimaryImage(Homestay homestay) {
        // Tìm ảnh chính của homestay
        return homestayImageRepository.findByHomestayAndIsPrimaryTrue(homestay);
    }

    @Override
    public void saveHomestayImage(HomestayImage homestayImage) {
        // Lưu ảnh vào DB
        homestayImageRepository.save(homestayImage);
    }

    private void checkHomestayOwnership(Homestay homestay) {
        UserDetailsImpl currentUser = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        Long currentUserId = currentUser.getId();
        Long homestayOwnerId = homestay.getHost().getId();

        if (!currentUserId.equals(homestayOwnerId)) {
            throw new ForbiddenException("Bạn không có quyền thực hiện hành động này với homestay này.");
        }
    }

    // Phương thức upload ảnh cho homestay, bao gồm kiểm tra quyền sở hữu homestay
    public void uploadImageForHomestay(Long homestayId, String imageUrl) {
        Homestay homestay = homestayRepository.findById(homestayId)
                .orElseThrow(() -> new RuntimeException("Homestay not found"));

        // Kiểm tra quyền sở hữu homestay
        checkHomestayOwnership(homestay);

        HomestayImage homestayImage = new HomestayImage();
        homestayImage.setHomestay(homestay);
        homestayImage.setImageUrl(imageUrl);

        saveHomestayImage(homestayImage);
    }
}
