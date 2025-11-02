package com.ktb.community_BE.controller;

import com.ktb.community_BE.dto.AuthDto;
import com.ktb.community_BE.dto.UserDto;
import com.ktb.community_BE.entity.User;
import com.ktb.community_BE.service.UserAuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    /*
    Todo :
     세션 사용시 세션을 저장하고 있을 데이터베이스가 필요한 거 아닌가? (일단은 단순하게 구현)
     -> 학습 후 추가하기
     */

    private final UserAuthService userAuthService;

    //로그인 후 세션 생성, 프론트 검증용 id 반환
    @PostMapping
    public ResponseEntity<Long> login(@RequestBody AuthDto request, HttpServletResponse response) {
        Long userId = userAuthService.login(request.getEmail(), request.getPassword(),response);

        if(userId == null){
            throw new IllegalStateException("비정상적인 접근입니다.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }

    //로그아웃 후 세션 삭제
    @DeleteMapping
    public ResponseEntity<String> logout(HttpServletResponse response) {
        userAuthService.logoutUser(response);
        return ResponseEntity.ok("로그아웃 완료");
    }

    @PostMapping("/refresh")
    @ResponseBody
    public Map<String, String> refresh(@CookieValue(value ="refreshToken", required = false) String refreshToken,
                                       HttpServletResponse response){
        if(refreshToken == null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Map.of("error", "Refresh token missing");
        }

        try{
            var tokenRes = userAuthService.refreshTokens(refreshToken,response);

            if(tokenRes == null){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return Map.of("error", "Refresh token invalid or expired");
            }

            return Map.of(
                    "accessToken", tokenRes.accessToken(),
                    "refreshToken", tokenRes.refreshToken()
            );

        } catch(ResponseStatusException exception){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return Map.of("error", "Refresh token invalid or expired");
        }
    }


}