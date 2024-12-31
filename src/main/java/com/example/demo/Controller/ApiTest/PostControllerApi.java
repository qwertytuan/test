package com.example.demo.Controller.ApiTest;

import com.example.demo.Error.PostNotFoundException;
import com.example.demo.Model.PostResponse;
import com.example.demo.Service.PostService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PostControllerApi {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostControllerApi(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/posts")
    public List<PostResponse> getAllPosts() {
        return postService.getAllPost().stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getUser().getUsername(),
                        post.getCategory(),
                        post.getCreatedDate(),
                        post.getModifiedDate()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/posts/{id}")
    public PostResponse getPostById(@PathVariable Long id) {
        var post = postService.getPostById(id);
        if (post == null) {
            throw new PostNotFoundException("Post not found with id: " + id);
        }
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getUsername(),
                post.getCategory(),
                post.getCreatedDate(),
                post.getModifiedDate()
        );
    }

    @GetMapping("/users/{userId}/posts")
    public List<PostResponse> getPostsByUserId(@PathVariable Long userId) {
        var user = userService.getUserById(userId);
        if (user != null) {
            return postService.getPostsByUser(user).stream()
                    .map(post -> new PostResponse(
                            post.getId(),
                            post.getTitle(),
                            post.getContent(),
                            post.getUser().getUsername(),
                            post.getCategory(),
                            post.getCreatedDate(),
                            post.getModifiedDate()
                    ))
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }
}