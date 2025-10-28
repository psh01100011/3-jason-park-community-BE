package com.ktb.community_BE.controller;

import com.ktb.community_BE.service.PostLikeService;
import com.ktb.community_BE.service.UserAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/{postId}/like")
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;
    private final UserAuthService userAuthService;

    // 좋아요 추가
    @PostMapping
    public ResponseEntity<String> likePost(@PathVariable Long postId, HttpSession session) {
        Long userId = userAuthService.getSessionId(session);
        postLikeService.likePost(postId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body("liked");
    }

    // 좋아요 취소
    @DeleteMapping
    public ResponseEntity<String> unlikePost(@PathVariable Long postId, HttpSession session) {
        Long userId = userAuthService.getSessionId(session);
        postLikeService.unlikePost(postId, userId);
        return ResponseEntity.ok("unliked");
    }
}