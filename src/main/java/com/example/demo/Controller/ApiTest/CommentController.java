package com.example.demo.Controller.ApiTest;

import com.example.demo.Model.CommentModel;
import com.example.demo.Model.CommentResponse;
import com.example.demo.Model.PostModel;
import com.example.demo.Model.UserModel;
import com.example.demo.Service.CommentService;
import com.example.demo.Service.PostService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentController {

        @Autowired
        private CommentService commentService;

        @Autowired
        private UserService userService;

        @Autowired
        private PostService postService;


    @GetMapping("/comments")
        public List<CommentResponse> getAllComments() {
                return commentService.getAllComments();
        }

        @GetMapping("/comments/byPost")
        public List<CommentResponse> getCommentsByPostId(@RequestParam Long postId) {
                return commentService.getCommentByPostId(postId);
        }

        @PostMapping("/comments/{id}/upvote")
        public CommentResponse upvoteComment(@PathVariable Long id) {
                return commentService.convertToResponse(commentService.upvoteComment(id));
        }

        @PostMapping("/comments/{id}/downvote")
        public CommentResponse downvoteComment(@PathVariable Long id) {
                return commentService.convertToResponse(commentService.downvoteComment(id));
        }

        @PostMapping(value = "/posts/{postId}/comment", consumes = "application/json")
        public ResponseEntity<?> addComment(@PathVariable Long postId,
                                            @RequestBody Map<String, Object> payload,
                                            Principal principal) {

            // Retrieve the current user
                String username = principal.getName();
                UserModel currentUser = userService.getUserByUsername(username);

                // Extract content and post ID from the payload
                String content = (String) payload.get("content");
                Map<String, Object> postMap = (Map<String, Object>) payload.get("post");
                Long extractedPostId = Long.valueOf((String) postMap.get("id"));

                // Fetch the post by postId
                PostModel post = postService.getPostById(extractedPostId);
                if (post == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
                }

                // Create and set the comment
                CommentModel comment = new CommentModel();
                comment.setUser(currentUser);
                comment.setPost(post);
                comment.setContent(content);
                comment.setUpvotes(1);
                comment.setDownvotes(0);

                // Save the comment
                CommentModel savedComment = commentService.saveComment(comment);

                // Convert to response
                CommentResponse response = commentService.convertToResponse(savedComment);

                return ResponseEntity.ok(response);
        }

}