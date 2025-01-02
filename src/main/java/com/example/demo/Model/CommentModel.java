package com.example.demo.Model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import java.util.Date;
@Entity
public class CommentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String content;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd:HH:mm:ss")
    private Date date;
    @ManyToOne
    private UserModel user;
    @ManyToOne
    private PostModel post;

    public CommentModel() {
    }

    public CommentModel(Long id, String content, Date date, UserModel user, PostModel post) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.user = user;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Date getDate() {
        return date;
    }

    public UserModel getUser() {
        return user;
    }

    public PostModel getPost() {
        return post;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public void setPost(PostModel post) {
        this.post = post;
    }
}
