package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    @Value("${file-upload-path}")
    private String uploadDir;

    public String uploadFile(MultipartFile file) throws IOException {
        validateFile(file);
        String fileName = file.getOriginalFilename();
        Path uploadPath = Paths.get(uploadDir, fileName);
        Files.createDirectories(uploadPath.getParent());
        file.transferTo(uploadPath.toFile());
        return fileName;
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new IOException("Invalid file type. Only JPEG and PNG are allowed.");
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB limit
            throw new IOException("File size exceeds the limit of 5MB");
        }
    }
}