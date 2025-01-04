package com.example.demo.Controller.ApiTest;

import com.example.demo.Model.CommentResponse;
import com.example.demo.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

        @Autowired
        private CommentService commentService;

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
}