package com.example.demo.Controller;

import com.example.demo.Model.PostModel;
import com.example.demo.Model.UserModel;
import com.example.demo.Service.PostService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PostController {

    @Autowired
    private final PostService postService;

    @Autowired
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/posts")
    public String getAllPosts(Model model) {
        List<PostModel> posts = postService.getAllPost();
        model.addAttribute("posts", posts);
        return "/post/posts";
    }

    @GetMapping("/posts/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        PostModel post = postService.getPostById(id);
        if (post != null) {
            model.addAttribute("post", post);
            return "post/post";
        } else {
            return "post-not-found";
        }
    }
    @GetMapping("/users/{userId}/posts")
    public String getPostsByUserId(@PathVariable Long userId, Model model) {
        UserModel user = userService.getUserById(userId);
        if (user != null) {
            List<PostModel> posts = postService.getPostsByUser(user);
            model.addAttribute("posts", posts);
            return "/post/posts";
        } else {
            return "user-not-found";
        }
    }

}