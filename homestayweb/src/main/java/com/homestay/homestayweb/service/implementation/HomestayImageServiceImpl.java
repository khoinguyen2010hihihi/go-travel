package com.homestay.homestayweb.service.implementation;

import com.homestay.homestayweb.entity.HomestayImage;
import com.homestay.homestayweb.repository.HomestayImageRepository;
import com.homestay.homestayweb.service.HomestayImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HomestayImageServiceImpl implements HomestayImageService {
    private final HomestayImageRepository imageRepository;

    @Value("${upload.path}")
    private String uploadDir;

    @Override
    public void uploadImages(Long homestayId, List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("Danh sách hình ảnh không được trống");
        }

        if (images.size() > 5) {
            throw new IllegalArgumentException("Không thể tải lên quá 5 hình ảnh");
        }

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path homestayPath = uploadPath.resolve(String.valueOf(homestayId));

            Files.createDirectories(homestayPath);

            for (MultipartFile file : images) {
                if (file.isEmpty()) continue;

                // Tạo tên file ngẫu nhiên
                String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueFilename = UUID.randomUUID() + fileExtension;

                Path targetPath = homestayPath.resolve(uniqueFilename);

                // Bảo mật: Kiểm tra path traversal
                if (!targetPath.normalize().startsWith(uploadPath)) {
                    throw new RuntimeException("Lỗi bảo mật: Không thể lưu file bên ngoài thư mục upload");
                }

                file.transferTo(targetPath);

                imageRepository.save(HomestayImage.builder()
                        .homestayId(homestayId)
                        .imageUrl("/uploads/homestays/" + homestayId + "/" + uniqueFilename)
                        .isPrimary(false)
                        .build());
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi tải lên hình ảnh: " + e.getMessage(), e);
        }
    }
}