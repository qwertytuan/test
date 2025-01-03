package com.example.demo.Controller;

import com.example.demo.Model.PostModel;
import com.example.demo.Model.UserModel;
import com.example.demo.Service.CommentService;
import com.example.demo.Service.PostService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private CommentService commentService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }
////////////////////////////////////
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
////////////////////////////////////////
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


    ////////////////////////////////////////////////
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


    //////////////////////////////////////////////
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


}