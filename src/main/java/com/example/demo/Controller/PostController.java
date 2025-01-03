package com.example.demo.Controller;

import com.example.demo.Model.PostModel;
import com.example.demo.Model.UserModel;
import com.example.demo.Service.PostService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
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
        if (posts.isEmpty()) {
            model.addAttribute("errorMessage", "No posts found");
            return "/error/post-not-found";
        }
        return "/test/posts";
    }

    @GetMapping("/posts/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        PostModel post = postService.getPostById(id);
        if (post != null) {
            model.addAttribute("post", post);
            model.addAttribute("errorMessage", "Post not found with id: " + id);
            return "/test/post";
        } else {
            return "/error/post-not-found";
        }
    }
    @GetMapping("/users/{userId}/posts")
    public String getPostsByUserId(@PathVariable Long userId, Model model) {
        UserModel user = userService.getUserById(userId);
        if (user != null) {
            List<PostModel> posts = postService.getPostsByUser(user);
            model.addAttribute("posts", posts);
            return "/test/posts";
        } else {
            model.addAttribute("errorMessage", "Post not found " );
            return "/error/user-not-found";
        }
    }

    // src/main/java/com/example/demo/Controller/PostController.java

    @PostMapping("/posts/{id}/edit")
    public String editPost(@PathVariable Long id, @ModelAttribute PostModel updatedPost, Model model, Principal principal) {
        UserModel currentUser = (UserModel) userService.loadUserByUsername(principal.getName());
        PostModel editedPost = postService.editPost(id, updatedPost, currentUser);
        if (editedPost != null) {
            model.addAttribute("post", editedPost);
            return "redirect:/posts/" + id;
        } else {
            model.addAttribute("errorMessage", "You do not have permission to edit this post or post not found");
            return "/error/post-not-found";
        }
    }

    @GetMapping("/posts/{id}/edit")
    public String getUserEditPostPage(@PathVariable Long id, Model model, Principal principal) {
        PostModel post = postService.getPostById(id);
        UserModel currentUser = (UserModel) userService.loadUserByUsername(principal.getName());
        if (post != null && (post.getUser().getId().equals(currentUser.getId()) || currentUser.getRole().contains("ADMIN"))) {
            model.addAttribute("post", post);
            return "/post/edit-post";
        } else {
            model.addAttribute("errorMessage", "You do not have permission to edit this post or post not found");
            return "/error/post-not-found";
        }
    }















    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/posts/{id}/edit")
    public String getAdminEditPostPage(@PathVariable Long id, Model model) {
        PostModel post = postService.getPostById(id);
        if (post != null) {
            model.addAttribute("post", post);
            return "/admin/edit-post";
        } else {
            model.addAttribute("errorMessage", "Post not found with id: " + id);
            return "/error/post-not-found";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/posts/{id}/edit")
    public String adminEditPost(@PathVariable Long id, @ModelAttribute PostModel updatedPost, Model model) {
        PostModel editedPost = postService.editPost(id, updatedPost, null); // Admin can edit any post
        if (editedPost != null) {
            model.addAttribute("post", editedPost);
            return "redirect:/posts/" + id;
        } else {
            model.addAttribute("errorMessage", "Post not found or could not be edited");
            return "/error/post-not-found";
        }
    }
}