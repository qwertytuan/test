package com.example.demo.Controller;

import com.example.demo.Model.UserModel;
import com.example.demo.Repo.UserRepo;
import com.example.demo.Service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class RegistrationController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping(value = "/req/signup", consumes = "multipart/form-data")
    public ResponseEntity<?> createUser(@RequestParam("username") String username,
                                             @RequestParam("email") String email,
                                             @RequestParam("password") String password,
                                             @RequestParam("passwordcon") String passwordcon,
                                             @RequestParam(value = "avatar", required = false) MultipartFile avatar) throws IOException {
        if (userRepo.findByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username is already taken");
        }
        if (password.length() < 8) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Password too short");
        }
        if (!password.equals(passwordcon)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Password not match");
        }

        UserModel user = new UserModel();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        if (avatar != null && !avatar.isEmpty()) {
            String fileName = fileUploadService.uploadFile(avatar);
            user.setAvatarUrl("/uploads/" + fileName);
        }

        return ResponseEntity.ok(userRepo.save(user));
    }
}