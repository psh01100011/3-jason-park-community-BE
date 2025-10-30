package com.ktb.community_BE.controller;


import com.ktb.community_BE.dto.PostDto;
import com.ktb.community_BE.dto.UserDto;
import com.ktb.community_BE.service.PostService;
import com.ktb.community_BE.service.UserAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    /*
    Todo :
        0. postController 하다가 알았는데 반환값 ResponseEntity 포멧으로 반환하는 식으로 구현
        -> ********모든 컨트롤러 수정필요**********
        1. 권한 검사 구현 위치 -> 어디가 합리적일까? 고민해보고 조사하기
        2. 게시물 목록 조회 -> repository, service, controller 전체에서 어떻게 구현할지 애매한 듯함
        -> 공부 필요

     */


    private final PostService postService;
    private final UserAuthService userAuthService;

    // 게시물 등록
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postRequest, HttpSession session) {
        Long userId = userAuthService.getSessionId(session);
        postRequest.setUserId(userId);
        PostDto response = postService.createPost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 게시물 조회 : id
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostDetail(@PathVariable Long postId) {
        PostDto response = postService.getPostDetail(postId);
        return ResponseEntity.ok(response);
    }

    // 게시물 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @RequestBody PostDto postRequest,HttpSession session){
        Long userId = userAuthService.getSessionId(session);
        //권한 검사 구현 필요

        postRequest.setUserId(userId);
        postRequest.setPostId(postId);
        PostDto response = postService.updatePost(postRequest);
        return ResponseEntity.ok(response);
    }

    // 게시물 소프트 삭제 : 게시물 상태 변경(삭제)
    @PatchMapping("/{postId}/status")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, HttpSession session){
        Long userId = userAuthService.getSessionId(session);
        //권한 검사는 service에 구현? or controller에 구현? -> 조사하기


        postService.updatePostStatus(postId);
        return ResponseEntity.noContent().build();
    }

    // 게시물 삭제 :
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePostPermently(@PathVariable Long postId, HttpSession session){
        Long userId = userAuthService.getSessionId(session);
        //권한 검사는 service에 구현? or controller에 구현? -> 조사하기


        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }

    //게시물 목록 조회 : 무한 스크롤
    @GetMapping("/list")
    public ResponseEntity<List<PostDto>> getPostList(@RequestParam(value = "lastPostId", required = false) Long lastPostId, @RequestParam(value = "limit", defaultValue = "10") int limit) {
        List<PostDto> response = postService.getPostList(lastPostId, limit);
        return ResponseEntity.ok(response);
    }
}
