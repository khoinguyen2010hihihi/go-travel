//package com.homestay.homestayweb.service.implementation;
//
//import com.homestay.homestayweb.entity.HomestayImage;
//import com.homestay.homestayweb.repository.HomestayImageRepository;
//import com.homestay.homestayweb.service.HomestayImageService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class HomestayImageServiceImpl implements HomestayImageService {
//    private final HomestayImageRepository imageRepository;
//
//    @Value("${upload.path:uploads/homestays}")
//    private String uploadDir;
//
//    @Override
//    public void uploadImages(Long homestayId, List<MultipartFile> images) {
//        if (images.size() < 1 || images.size() > 5) {
//            throw new IllegalArgumentException("You must upload between 1 and 5 images.");
//        }
//        Path homestayPath = Paths.get(uploadDir, String.valueOf(homestayId));
//        try {
//            Files.createDirectories(homestayPath);
//            for (MultipartFile file: images) {
//                String filename = file.getOriginalFilename();
//                if (filename == null || filename.isEmpty()) continue;
//                Path filePath = homestayPath.resolve(filename);
//                file.transferTo(filePath.toFile());
//                imageRepository.save(HomestayImage.builder()
//                        .imagePath("/uploads/homestays/" + homestayId + "/" + filename)
//                        .homestayId(homestayId)
//                        .build());
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to upload files", e);
//        }
//    }
//}
