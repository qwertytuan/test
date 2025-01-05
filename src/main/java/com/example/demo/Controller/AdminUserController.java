package com.example.demo.Controller;

import com.example.demo.Model.UserModel;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/manage")
    public String getManageUsersPage(Model model) {
        List<UserModel> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "/admin/manage-users";
    }

    @GetMapping("/{id}/edit")
    public String getEditUserPage(@PathVariable Long id, Model model) {
        UserModel user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "/admin/edit-user";
        } else {
            model.addAttribute("errorMessage", "User not found with id: " + id);
            return "/error/user-not-found";
        }
    }

    @PostMapping("/{id}/edit")
    public String editUser(@PathVariable Long id, @ModelAttribute UserModel updatedUser, Model model) {
        UserModel user = userService.updateUser(id, updatedUser);
        if (user != null) {
            return "redirect:/admin/users/manage";
        } else {
            model.addAttribute("errorMessage", "User not found with id: " + id);
            return "/error/user-not-found";
        }
    }
}