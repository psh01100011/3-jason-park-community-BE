package com.ktb.community_BE.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false, length = 500)
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    protected Comment() {};

    public Comment(Post post, User user, String content){
        this.post = post;
        this.user = user;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public void changeContent(String content){
        this.content = content;
    }

    public void setUpdatedAt(LocalDateTime updatedAt){this.updatedAt = updatedAt;}
    public void setDeletedAt(LocalDateTime deletedAt){this.deletedAt = deletedAt;}
}
