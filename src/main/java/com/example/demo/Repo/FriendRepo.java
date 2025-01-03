package com.example.demo.Repo;

import com.example.demo.Model.FriendList;
import com.example.demo.Model.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepo extends JpaRepository<FriendList, Long> {
    List<FriendList> findByStatus(FriendStatus status);
}
