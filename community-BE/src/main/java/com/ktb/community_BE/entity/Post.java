package com.ktb.community_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId")
    private Long id;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    /*
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes = new ArrayList<>();
     */

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false)
    private int viewCount;
    @Column(nullable = false)
    private int commentCount;
    @Column(nullable = false)
    private int likeCount;

    @Enumerated(EnumType.STRING)
    private PostStatus postStatus = PostStatus.ACTIVE;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @OneToOne(mappedBy = "post")
    private PostContent content;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    public Post(User user, String title) {
        this.user = user;
        this.title = title;
        this.createdAt = LocalDateTime.now();
    }

    protected Post() {};

    public void changeTitle(String title){
        this.title = title;
    }

    public void setUpdatedAt(LocalDateTime updatedAt){
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt){ this.deletedAt = deletedAt;}

    public void setPostStatusDeleted(){
        this.postStatus = PostStatus.DELETED;
    }


    public void addViewCount(){ this.viewCount++;}
    public void addCommentCount(){ this.commentCount++;}
    public void addLikeCount(){ this.likeCount++;}

    public void minusCommentCount(){ this.commentCount--;}
    public void minusLikeCount(){ this.likeCount--;}
}
