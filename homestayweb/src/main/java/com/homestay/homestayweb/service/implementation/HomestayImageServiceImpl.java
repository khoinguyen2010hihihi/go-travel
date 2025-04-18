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
    public void uploadImages(Long homestayId, List<MultipartFile> images, Integer primaryIndex) {
        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("Image list cannot be empty");
        }

        if (images.size() > 5) {
            throw new IllegalArgumentException("Cannot upload more than 5 images");
        }

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path homestayPath = uploadPath.resolve(String.valueOf(homestayId));

            Files.createDirectories(homestayPath);

            for (int i = 0; i < images.size(); i++) {
                MultipartFile file = images.get(i);
                if (file.isEmpty()) continue;

                String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String uniqueFilename = UUID.randomUUID() + fileExtension;

                Path targetPath = homestayPath.resolve(uniqueFilename);

                if (!targetPath.normalize().startsWith(uploadPath)) {
                    throw new RuntimeException("Security error: Cannot save file outside upload directory");
                }

                file.transferTo(targetPath);

                boolean isPrimary = (primaryIndex != null && primaryIndex == i);

                imageRepository.save(HomestayImage.builder()
                        .homestayId(homestayId)
                        .imageUrl("/uploads/homestays/" + homestayId + "/" + uniqueFilename)
                        .isPrimary(isPrimary)
                        .build());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while uploading image: " + e.getMessage(), e);
        }
    }
}