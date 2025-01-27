package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class PostModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String title;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserModel user;


    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column()
    private LocalDateTime modifiedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "post")
    @JsonManagedReference
    private List<CommentModel> comments;

    @Column(nullable = false, columnDefinition = "int default 1")
    private int upvotes;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int downvotes;

}