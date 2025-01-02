package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.Service.CommentService;

import java.util.List;

@Controller
public class CommentController {
        private final CommentService commentService;

        @Autowired
        public CommentController(CommentService commentService) {
                this.commentService = commentService;
        }

        @GetMapping("/comments")
        public List<String> getAllComments() {
                return commentService.getAllComments();
        }

        @GetMapping("/comments/{id}")
        public String getCommentById(@PathVariable Long id) {
                return commentService.getCommentById(id);
        }
        @GetMapping
        public String getPostByCommentId(@PathVariable Long id) {
                return commentService.getPostByCommentId(id);
        }
        @GetMapping
        public void upvoteComment(@PathVariable Long id) {
                commentService.upvoteComment(id);
        }
        @GetMapping
        public  void downvoteComment(@PathVariable Long id) {
                commentService.downvoteComment(id);
        }
        @GetMapping
        public void deleteComment(@PathVariable Long id, String text) {
                commentService.deleteComment(id, text);
        }
        @GetMapping
        public void addComment(@PathVariable Long id, String text) {
                commentService.addComment(id, text);
        }
        @GetMapping
        public void editComment(@PathVariable Long id, String text) {
                commentService.updateComment(id, text);
        }


}