package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table (name = "user_friends")
public class FriendModel {
    // Getters and setters
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    @JsonManagedReference
    private UserModel friend;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendStatus status;

    public FriendModel() {
    }

    public FriendModel(UserModel user, UserModel friend, FriendStatus status) {
        this.user = user;
        this.friend = friend;
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public void setFriend(UserModel friend) {
        this.friend = friend;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }

    public UserModel getUser() {
        return user;
    }

    public UserModel getFriend() {
        return friend;
    }

    public FriendStatus getStatus() {
        return status;
    }
}
