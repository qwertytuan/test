package com.example.demo.Controller;

import com.example.demo.Model.UserModel;
import com.example.demo.Repo.UserRepo;
import com.example.demo.Security.SecurityConfig;
import com.example.demo.Service.FileUploadService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Objects;

@Controller
public class UserController {
    private final UserService userService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadService fileUploadService;

    @Autowired
    public UserController(UserService userService,
                          UserRepo userRepo,
                          PasswordEncoder passwordEncoder,
                          FileUploadService fileUploadService) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.fileUploadService = fileUploadService;
    }

    @GetMapping("/profile")
    public String ShowProfile(@AuthenticationPrincipal User loggedInUser, Model model) {
        Long userId = loggedInUser != null? userRepo.findByUsername(loggedInUser.getUsername()).get().getId() :  null;
        String username = loggedInUser != null? loggedInUser.getUsername() : null;
        String avatarUrl = loggedInUser != null? userRepo.findByUsername(loggedInUser.getUsername()).get().getAvatarUrl() : null;
        String email = loggedInUser != null? userRepo.findByUsername(loggedInUser.getUsername()).get().getEmail()  : null;
        String password = loggedInUser != null? loggedInUser.getPassword() : null;
        model.addAttribute("password", password);
        model.addAttribute("avatarUrl", avatarUrl);
        model.addAttribute("email", email);
        model.addAttribute("userName", username);
        model.addAttribute("userId", userId);
        return "/user/profile";
    }

    @PostMapping("/profile/update")
    public String UpdateProfile(@AuthenticationPrincipal User loggedInUser,
                                String username,
                                String password,
                                String email,
                                MultipartFile avatar,
                                RedirectAttributes redirectAttributes) {
        if (loggedInUser == null) {
            return "redirect:/index";
        }

        try {
            // Fetch user from the database
            UserModel user = userRepo.findByUsername(loggedInUser.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Update email if provided
            if (email != null && !email.isBlank()) {
                user.setEmail(email);
            }

            // Handle avatar upload
            if (avatar != null && !avatar.isEmpty()) {
                try {
                    String fileName = fileUploadService.uploadFile(avatar, username);
                    user.setAvatarUrl("/uploads/" + fileName);
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("error", "Failed to upload avatar: " + e.getMessage());
                    return "redirect:/profile";
                }
            }

            // Update password if provided
            if (password != null && !password.isBlank()) {
                String encryptedPassword = passwordEncoder.encode(password);
                user.setPassword(encryptedPassword);
            }

            // Save all changes
            userRepo.save(user);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
            return "redirect:/profile";
        }

        return "redirect:/index";
    }
}
