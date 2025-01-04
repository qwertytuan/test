package com.example.demo.Controller;

import com.example.demo.Model.CommentModel;
import com.example.demo.Model.CommentResponse;
import com.example.demo.Model.PostModel;
import com.example.demo.Model.UserModel;
import com.example.demo.Repo.UserRepo;
import com.example.demo.Service.CommentService;
import com.example.demo.Service.PostService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
public class PostController {

    private static final String DEFAULT_AVATAR_URL = "/uploads/xampp.png";
    @Autowired
    private final PostService postService;

    @Autowired
    private final UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepo userRepo;

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
    public String getPostById(@PathVariable Long id, Model model, @AuthenticationPrincipal User loggedInUser) {
        PostModel post = postService.getPostById(id);
        Long userId = loggedInUser != null ? userRepo.findByUsername(loggedInUser.getUsername()).get().getId() : null;
        String username = loggedInUser != null ? loggedInUser.getUsername() : null;
        String avatarUrl = loggedInUser != null ? userRepo.findByUsername(loggedInUser.getUsername()).get().getAvatarUrl() : null;
        model.addAttribute("avatarUrl", Objects.requireNonNullElse(avatarUrl, DEFAULT_AVATAR_URL));
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);
        // Add an attribute to pass to the view to check login status
        model.addAttribute("isLoggedIn", loggedInUser != null);
        if (post != null) {
            model.addAttribute("post", post);
            model.addAttribute("errorMessage", "Post not found with id: " + id);
            return "/post/posts";
        } else {
            return "/error/post-not-found";
        }
    }

    @GetMapping("/users/posts")
    public String getUserPosts(Principal principal, Model model) {
        String username = principal.getName();
        UserModel currentUser =  userService.getUserByUsername(username);
        List<PostModel> posts = postService.getPostsByUser(currentUser);
        model.addAttribute("posts", posts);
        if (posts.isEmpty()) {
            model.addAttribute("errorMessage", "No posts found");
            return "/error/post-not-found";
        }
        return "/post/posts";
    }

    @GetMapping("/users/{id}/posts")
    public String getUserPosts(@PathVariable Long id, Model model) {
        UserModel user = userService.getUserById(id);
        List<PostModel> posts = postService.getPostsByUser(user);
        model.addAttribute("posts", posts);
        if (posts.isEmpty()) {
            model.addAttribute("errorMessage", "No posts found");
            return "/error/post-not-found";
        }
        return "/post/posts";
    }

    @GetMapping("/users/posts/manage")
    public String getUserEditPostPage( Principal principal, Model model) {
        String username = principal.getName();
        UserModel currentUser = userService.getUserByUsername(username);
        List<PostModel> posts = postService.getPostsByUser(currentUser);
        if (posts.isEmpty()) {
            model.addAttribute("errorMessage", "No posts found");
            return "/error/post-not-found";
        }
        model.addAttribute("posts", posts);
        return "/post/manage-posts";
    }



    /// /////////////////////////////////////////////
    @GetMapping("/users/posts/{id}/edit")
    public String getUserEditPostPage(@PathVariable Long id, Model model, Principal principal) {
        PostModel post = postService.getPostById(id);
        String username = principal.getName();
        UserModel currentUser = userService.getUserByUsername(username);
        if (post != null && (post.getUser().getId().equals(currentUser.getId()) || currentUser.getRole().contains("ADMIN"))) {
            model.addAttribute("post", post);
            return "/post/edit-post";
        } else {
            model.addAttribute("errorMessage", "You do not have permission to edit this post or post not found");
            return "/error/post-not-found";
        }
    }

    @PostMapping("/users/posts/{id}/edit")
    public String editPost(@PathVariable Long id, PostModel updatedPost, Principal principal, Model model) {
        String username = principal.getName();
        UserModel currentUser = userService.getUserByUsername(username);
        PostModel post = postService.editPost(id, updatedPost, currentUser);
        if (post != null) {
            return "redirect:/users/posts";
        } else {
            model.addAttribute("errorMessage", "You do not have permission to edit this post or post not found");
            return "/error/post-not-found";
        }
    }

    /////////////////////////////////////////////////
    @PostMapping("/users/posts/{id}/delete")
    public String deletePost(@PathVariable Long id, Principal principal, Model model) {
        String username = principal.getName();
        UserModel currentUser = userService.getUserByUsername(username);
        PostModel post = postService.getPostById(id);
        if (post != null && (post.getUser().getId().equals(currentUser.getId()) || currentUser.getRole().contains("ADMIN"))) {
            commentService.deleteCommentsByPostId(id); // Delete associated comments first
            postService.deletePost(id);
            return "redirect:/users/posts";
        } else {
            model.addAttribute("errorMessage", "You do not have permission to delete this post or post not found");
            return "/error/post-not-found";
        }
    }


    @GetMapping("/users/posts/create")
    public String getCreatePostPage(Model model) {
        model.addAttribute("post", new PostModel());
        return "/post/create-post";
    }

    @PostMapping("/users/posts/create")
    public String createPost(PostModel post, Principal principal, Model model) {
        String username = principal.getName();
        UserModel currentUser = userService.getUserByUsername(username);
        post.setUser(currentUser);
        postService.createPost(post);
        return "redirect:/users/posts";
    }


    @GetMapping("/users/posts/{id}/confirm-delete")
    public String getConfirmDeletePostPage(@PathVariable Long id, Model model, Principal principal) {
        PostModel post = postService.getPostById(id);
        String username = principal.getName();
        UserModel currentUser = userService.getUserByUsername(username);
        if (post != null && (post.getUser().getId().equals(currentUser.getId()) || currentUser.getRole().contains("ADMIN"))) {
            model.addAttribute("post", post);
            return "/post/confirm-delete";
        } else {
            model.addAttribute("errorMessage", "You do not have permission to delete this post or post not found");
            return "/error/post-not-found";
        }
    }

/////////////////////////////////////////////////
    @PostMapping("/posts/{id}/upvote")
    public String upvotePost(@PathVariable Long id, Model model) {
        PostModel post = postService.upvotePost(id);
        if (post != null) {
            return "redirect:/posts/" + id;
        } else {
            model.addAttribute("errorMessage", "Post not found with id: " + id);
            return "/error/post-not-found";
        }
    }

    @PostMapping("/posts/{id}/downvote")
    public String downvotePost(@PathVariable Long id, Model model) {
        PostModel post = postService.downvotePost(id);
        if (post != null) {
            return "redirect:/posts/" + id;
        } else {
            model.addAttribute("errorMessage", "Post not found with id: " + id);
            return "/error/post-not-found";
        }
    }

}