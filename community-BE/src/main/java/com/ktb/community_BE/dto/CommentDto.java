package com.ktb.community_BE.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private Long postId;
    private Long userId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentDto(Long id,Long postId, Long userId, String nickname, String content, LocalDateTime createdAt, LocalDateTime updatedAt){
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public void setPostId(Long postId){
        this.postId = postId;
    }


    public void setId(Long id){
        this.id = id;
    }
}
