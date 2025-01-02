package com.example.demo.Controller.ApiTest;

import com.example.demo.Model.CommentModel;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

        @Autowired
        private CommentService commentService;

        @GetMapping("/comments")
        public List<CommentModel> getAllComments() {
                return commentService.getAllComments();
        }

        @GetMapping("/comments/byPost")
        public List<CommentModel> getCommentsByPostId(@RequestParam Long postId) {
                return commentService.getCommentByPostId(postId);
        }

        @GetMapping("/post/comment/{id}")
        public String getPostByCommentId(@PathVariable Long id) {
                return commentService.getPostByCommentId(id);
        }

        @GetMapping("/comments/up/{id}")
        public void upvoteComment(@PathVariable Long id) {
                commentService.upvoteComment(id);
        }

        @GetMapping("/comments/down/{id}")
        public  void downvoteComment(@PathVariable Long id) {
                commentService.downvoteComment(id);
        }

        @GetMapping("/comments/delete/{id}")
        public void deleteComment(@PathVariable Long id, String text) {
                commentService.deleteComment(id, text);
        }

        @GetMapping("/comment/add")
        public void addComment(@PathVariable String text) {
                commentService.addComment(text);
        }

        @GetMapping("/comments/edit/{id}")
        public void editComment(@PathVariable Long id, String text) {
                commentService.editComment(id, text);
        }


}