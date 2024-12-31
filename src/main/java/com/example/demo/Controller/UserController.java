package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

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

}
