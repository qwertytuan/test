package com.example.demo.Controller;

import com.example.demo.Repo.UserRepo;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/users")
    public String ShowUsers() {
        return "/user/showUserInfo";
    }

    @GetMapping("/users/edit")
    public String EditUsers() {
        return "/user/editUserInfo";
    }

    @GetMapping("/users/create")
    public String CreateUsers() {
        return "/user/createUser";
    }

    @GetMapping("/users/friends")
    public String ShowFriends() {
        return "/user/showFriends";
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
}
