package com.ktb.community_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostDto {
    private Long id;
    private Long userId;
    private String nickname;
    private String title;
    private String content;
    private String image;
    private int viewCount;
    private int commentCount;
    private int likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostDto(Long id){
        this.id = id;
    }

    public PostDto(Long id, Long userId, String nickname, String title, String content, String image, int viewCount, int commentCount, int likeCount, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.image = image;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public void setPostId(Long postId){
        this.id = postId;
    }
}
