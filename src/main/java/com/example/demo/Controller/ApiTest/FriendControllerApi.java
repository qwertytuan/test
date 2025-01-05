package com.example.demo.Controller.ApiTest;

import com.example.demo.Model.FriendResponse;
import com.example.demo.Service.FriendServices;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendControllerApi {

    @Autowired
    private FriendServices friendServices;

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserFriends(@PathVariable Long userId) {
        List<FriendResponse> friends = friendServices.convertToResponseList(friendServices.findFriendsByUserId(userId));
        if (friends.isEmpty()) {
            return ResponseEntity.status(404).body("No friends found for the user");
        }
        return ResponseEntity.ok(friends);
    }
}