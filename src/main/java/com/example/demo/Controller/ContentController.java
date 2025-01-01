package com.example.demo.Controller;

import com.example.demo.Model.UserModel;
import com.example.demo.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class ContentController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String index() {
        return "redirect:/index";
    }

    @GetMapping("/req/login")
    public String login(@RequestParam(value = "session", required = false) String session, Model model) {
        if ("expired".equals(session)) {
            model.addAttribute("sessionExpired", true);
            return "login";
        }
        return "login";
    }

    @GetMapping("/req/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/error")
    public String error() {return "/error/error";}

    @GetMapping("/index")
    public String index(@AuthenticationPrincipal User loggedInUser, Model model) {
        Long userId = loggedInUser != null ? userRepo.findByUsername(loggedInUser.getUsername()).get().getId() : null;
        String username = loggedInUser != null ? loggedInUser.getUsername() : null;
        String avatarUrl = loggedInUser != null ? userRepo.findByUsername(loggedInUser.getUsername()).get().getAvatarUrl() : null;
        model.addAttribute("avatarUrl", Objects.requireNonNullElse(avatarUrl, "/uploads/xampp.png"));

        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        // Add an attribute to pass to the view to check login status
        model.addAttribute("isLoggedIn", loggedInUser != null);
        return "index"; // Render the 'index.html' file
    }

}
