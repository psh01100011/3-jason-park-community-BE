package com.ktb.community_BE.controller;

import com.ktb.community_BE.dto.CommentDto;
import com.ktb.community_BE.service.CommentService;
import com.ktb.community_BE.service.PostService;
import com.ktb.community_BE.service.UserAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    /*
    Todo :
        0. postController 하다가 알았는데 반환값 ResponseEntity 포멧으로 반환하는 식으로 구현
        -> ********모든 컨트롤러 수정필요**********
        1. 권한 검사 구현 위치 -> 어디가 합리적일까? 고민해보고 조사하기
        2. 게시물 목록 조회 -> repository, service, controller 전체에서 어떻게 구현할지 애매한 듯함
        -> 공부 필요

     */


    private final UserAuthService userAuthService;
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId,@RequestBody CommentDto commentRequest, HttpSession session){
        Long userId = userAuthService.getSessionId(session);
        commentRequest.setPostId(postId);
        commentRequest.setUserId(userId);
        CommentDto response= commentService.createComment(commentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentDto commentRequest,HttpSession session){
        Long userId = userAuthService.getSessionId(session);
        commentRequest.setPostId(postId);
        commentRequest.setUserId(userId);
        commentRequest.setId(commentId);
        //권한 검사는 service에 구현? or controller에 구현? -> 조사하기


        CommentDto response= commentService.updateComment(commentRequest);
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, HttpSession session){
        Long userId = userAuthService.getSessionId(session);
        //권한 검사는 service에 구현? or controller에 구현? -> 조사하기

        commentService.deleteComment(commentId,postId);
        return ResponseEntity.noContent().build();
    }

    // 댓글 리스트 조회
    @GetMapping("/list")
    public List<CommentDto> getPostList(@PathVariable Long postId) {
        return commentService.getCommentList(postId);
    }

}
