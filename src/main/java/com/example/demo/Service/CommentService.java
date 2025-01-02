package com.example.demo.Service;

import com.example.demo.Model.CommentModel;
import com.example.demo.Model.CommentResponse;
import com.example.demo.Repo.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    public List<CommentResponse> getAllComments() {
        return commentRepo.findAll().stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<CommentResponse> getCommentByPostId(Long postId) {
        return commentRepo.findByPostId(postId).stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    private CommentResponse convertToResponse(CommentModel comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setDate(comment.getDate());
        response.setUserId(comment.getUser().getId());
        response.setUsername(comment.getUser().getUsername());
        response.setPostId(comment.getPost().getId());
        response.setPostTitle(comment.getPost().getTitle());
        response.setPostUrl("/posts/" + comment.getPost().getId());
        return response;
    }
}