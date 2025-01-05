package com.example.demo.Repo;

import com.example.demo.Model.FriendModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepo extends JpaRepository<FriendModel, Long> {
    FriendModel findByUserIdAndFriendId(Long userId, Long friendId);

    List<FriendModel> findAllByUserId(Long userId);
}