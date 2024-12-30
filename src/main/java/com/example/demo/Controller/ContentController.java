package com.example.demo.Controller;

import com.example.demo.Model.UserModel;
import com.example.demo.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/error")
    public String error() {return "/error/error";}

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/req/login")
    public String login() {
        return "login";
    }

    @GetMapping("/req/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/index")
    public String index(@AuthenticationPrincipal User loggedInUser, Model model) {
        Long userId = loggedInUser != null ? userRepo.findByUsername(loggedInUser.getUsername()).get().getId() : null;
        String username = loggedInUser != null ? loggedInUser.getUsername() : null;
        String avatarUrl = loggedInUser != null ? userRepo.findByUsername(loggedInUser.getUsername()).get().getAvatarUrl() : null;
        model.addAttribute("avatarUrl",avatarUrl);
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        // Add an attribute to pass to the view to check login status
        model.addAttribute("isLoggedIn", loggedInUser != null);
        return "index"; // Render the 'index.html' file
    }

}
