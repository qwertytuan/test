package com.example.demo.Controller.ApiTest;

import com.example.demo.Model.UserModel;
import com.example.demo.Repo.UserRepo;
import com.example.demo.Service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class UserAvatarControllerApi {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/{userId}/avatar")
    public UserModel uploadAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file) throws IOException {
        UserModel user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        String username = user.getUsername();
        String fileName = fileUploadService.uploadFile(file, username);
        user.setAvatarUrl("/static/uploads/" + fileName);
        return userRepo.save(user);
    }
    @GetMapping("/api/avatar/{userId}")
    public String getAvatar(@PathVariable Long userId) {
        UserModel user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getAvatarUrl();
    }
}