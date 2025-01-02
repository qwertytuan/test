package com.example.demo.Service;

import com.example.demo.Model.CommentModel;
import com.example.demo.Repo.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepo commentRepo;

    public List<CommentModel> getAllComments() {
        return commentRepo.findAll();
    }

    public List<CommentModel> getCommentByPostId(Long id) {

        return commentRepo.findByPostId(id);
    }

    public String getPostByCommentId(Long id) {
        return null;
    }


    public void upvoteComment(Long id) {

    }

    public void downvoteComment(Long id) {
    }

    public void deleteComment(Long id, String text) {
    }

    public void reportComment(Long id, String text) {
    }

    public void addComment(String text) {
    }

    public void editComment(Long id,String text) {

    }

}
