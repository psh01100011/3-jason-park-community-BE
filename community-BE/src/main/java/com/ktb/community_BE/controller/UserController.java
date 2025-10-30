package com.ktb.community_BE.controller;

import com.ktb.community_BE.dto.AuthDto;
import com.ktb.community_BE.dto.UserDto;
import com.ktb.community_BE.entity.User;
import com.ktb.community_BE.entity.UserStatus;
import com.ktb.community_BE.service.UserAuthService;
import com.ktb.community_BE.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    /*
    Todo :
        0. postController 하다가 알았는데 반환값 ResponseEntity 포멧으로 반환하는 식으로 구현
        -> ********모든 컨트롤러 수정필요**********
        1. 완료
        2. 1번과 UserService 추가 구현 후 할 일
        - 내 글 조회, 내 댓글 조회, 내 좋아요 조회
     */


    private final UserService userService;
    private final UserAuthService userAuthService;
    // 회원 가입
    @PostMapping
    public ResponseEntity<UserDto> registUser(@RequestBody UserDto userRequest) {
        UserDto response = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //내 정보 가져오기 : 세션Id 사용
    @GetMapping("/me")
    public ResponseEntity<Object> getUserInfo(@RequestAttribute("userId") Long userId) {
        UserDto response = userService.getMyInfo(userId);
        return ResponseEntity.ok(response);
    }

    // 내 프로필 수정(닉네임, 프로필 사진)
    @PatchMapping("/me")
    public ResponseEntity<Void> updateProfile(@RequestBody UserDto userRequest, @RequestAttribute("userId") Long userId){
        userService.updateInfo(userId,userRequest);
        return ResponseEntity.noContent().build();
    }
    // 내 비밀번호 수정
    @PatchMapping("/me/auth")
    public ResponseEntity<Void> updatePassword(@RequestBody UserDto userRequest,@RequestAttribute("userId") Long userId){
        userService.updateAuth(userId,userRequest);
        return ResponseEntity.noContent().build();
    }

    // 회원 탈퇴 : 내 정보의 상태만 변경 (활성화 -> 탈퇴)
    @PatchMapping("/me/status")
    public ResponseEntity<Void> withdrawUser(@RequestAttribute("userId") Long userId){
        userService.withdrawUser(userId);
        return ResponseEntity.noContent().build();
    }

    //유저 이메일 중복 체크 : 중복시 false
    @PostMapping("/email")
    public ResponseEntity<Boolean> checkEmail(@RequestBody UserDto userRequest) {
        String email = userRequest.getEmail();
        log.info(email);
        if(userService.checkEmail(email)){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    //유저 이메일 중복 체크 중복시 false
    @PostMapping("/nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestBody UserDto userRequest) {
        // 중복이라면
        String nickname = userRequest.getNickname();
        if(userService.checkNickname(nickname)){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }




}
