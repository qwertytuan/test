package com.example.demo.Controller;

import com.example.demo.Model.PostModel;
import com.example.demo.Service.CommentService;
import com.example.demo.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/posts")
public class AdminPostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String getAdminPosts(Model model) {
        List<PostModel> posts = postService.getAllPost();
        model.addAttribute("posts", posts);
        if (posts.isEmpty()) {
            model.addAttribute("errorMessage", "No posts found");
            return "/error/post-not-found";
        }
        return "/admin/posts";
    }

    @GetMapping("/manage")
    public String getAdminManagePosts(Model model) {
        List<PostModel> posts = postService.getAllPost();
        model.addAttribute("posts", posts);
        if (posts.isEmpty()) {
            model.addAttribute("errorMessage", "No posts found");
            return "/error/post-not-found";
        }
        return "/admin/manage-posts";
    }

    @GetMapping("/{id}/edit")
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

    @PostMapping("/{id}/edit")
    public String editPost(@PathVariable Long id, PostModel updatedPost, Model model) {
        PostModel post = postService.editPostAdmin(id, updatedPost);
        if (post != null) {
            return "redirect:/admin/posts";
        } else {
            model.addAttribute("errorMessage", "Post not found with id: " + id);
            return "/error/post-not-found";
        }
    }

    @GetMapping("/{id}/confirm-delete")
    public String getAdminConfirmDeletePostPage(@PathVariable Long id, Model model) {
        PostModel post = postService.getPostById(id);
        if (post != null) {
            model.addAttribute("post", post);
            return "/admin/confirm-delete";
        } else {
            model.addAttribute("errorMessage", "Post not found with id: " + id);
            return "/error/post-not-found";
        }
    }
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id, Model model) {
        commentService.deleteCommentsByPostId(id); // Delete associated comments first
        postService.deletePost(id);
        return "redirect:/admin/posts";
    }
}
