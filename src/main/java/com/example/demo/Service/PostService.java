package com.example.demo.Service;

import com.example.demo.Model.PostModel;
import com.example.demo.Model.UserModel;
import com.example.demo.Repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepo postRepo;

    public PostService(PostRepo postRepo) {
        this.postRepo = postRepo;
    }

    public List<PostModel> getAllPost() {
        return postRepo.findAll();
    }

    public String getUserNameByUserId(Long id) {
        PostModel post = postRepo.findById(id).orElse(null);
        if (post != null) {
            return post.getUser().getUsername();
        }
        return null;
    }

    public List<PostModel> getPostsByUser(UserModel user) {
        return postRepo.findByUser(user);
    }

    public PostModel getPostById(Long id) {
        return postRepo.findById(id).orElse(null);
    }

    public PostModel savePost(PostModel post) {
        return postRepo.save(post);
    }

    public void deletePost(Long id) {
        postRepo.deleteById(id);
    }



    public PostModel editPost(Long postId, PostModel updatedPost, UserModel currentUser) {
        PostModel existingPost = postRepo.findById(postId).orElse(null);
        if (existingPost != null) {
            if (existingPost.getUser().getId().equals(currentUser.getId()) || currentUser.getRole().contains("ADMIN")) {
                existingPost.setTitle(updatedPost.getTitle());
                existingPost.setImageUrl(updatedPost.getImageUrl());
                existingPost.setContent(updatedPost.getContent());
                existingPost.setCategory(updatedPost.getCategory());
                existingPost.setModifiedDate(LocalDateTime.now());
                return postRepo.save(existingPost);
            }
        }
        return null;
    }
}