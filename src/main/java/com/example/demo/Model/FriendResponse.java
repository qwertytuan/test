package com.example.demo.Model;

public class FriendResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long friendId;
    private String friendName;
    private FriendStatus status;

    public FriendResponse() {
    }

    public FriendResponse(Long id, Long userId, String userName, Long friendId, String friendName, FriendStatus status) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.friendId = friendId;
        this.friendName = friendName;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Long getFriendId() {
        return friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }
}