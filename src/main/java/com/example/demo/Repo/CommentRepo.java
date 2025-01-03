package com.example.demo.Repo;

import com.example.demo.Model.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends JpaRepository<CommentModel, Long> {

    List<CommentModel> findByPostId(Long postId);

    void deleteByPostId(Long id);
}
