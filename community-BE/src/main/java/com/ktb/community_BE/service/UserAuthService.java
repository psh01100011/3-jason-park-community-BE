package com.ktb.community_BE.service;

import com.ktb.community_BE.entity.User;
import com.ktb.community_BE.entity.UserAuth;
import com.ktb.community_BE.jwt.JwtProvider;
import com.ktb.community_BE.repository.UserAuthRepository;
import com.ktb.community_BE.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {

    /*
    Todo :
        비밀번호 해싱 구현 ->  UserAuthService(login 추가)
        세션 확인 위치 조정(getSessionId) 여기보다 다른 곳에 있는 게 더 좋을 듯 함.
     */
    private final JwtProvider jwtProvider;
    private final UserAuthRepository userAuthRepository;
    private final UserRepository userRepository;

    private static final int ACCESS_TOKEN_EXPIRATION = 60 * 60;
    private static final int REFRESH_TOKEN_EXPIRATION = 7* 24 * 3600;


    public Long login(String email, String password,HttpServletResponse response) {
        UserAuth user = userAuthRepository.findByEmail(email);

        if(user == null){
            return null;
        }

        // 새로운 토큰 발급 및 저장
        var tokenResponse = generateAndSaveTokens(user);

        // 쿠키 추가
        addTokenCookies(response, tokenResponse);

        return user.getId();
    }

    public void logoutUser(HttpServletResponse response){
        //쿠키 즉시 만료
        addTokenCookie(response,"accessToken", null, 0);
        addTokenCookie(response,"refreshToken", null, 0);
    }

    //세션 만료 확인 : 여기 있는 것보다 따로 분리하는 게 더 좋을 것 같긴 함 -> 일단 여기
    public Long getSessionId(HttpSession session){
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null){
            throw new IllegalStateException("로그인되지 않은 사용자입니다.");
        }
        return userId;
    }

    @Transactional
    public TokenResponse refreshTokens(String refreshToken, HttpServletResponse response){
        var parsedRefreshToken = jwtProvider.parse(refreshToken);

        //검증 후 만료 전이면 발급

        Date expiration = parsedRefreshToken.getBody().getExpiration();
        if(expiration.before(new Date())){
            //refresh로 토큰 재발급 요청 보내기
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        Long userId = Long.valueOf(parsedRefreshToken.getBody().getSubject());
        User user = userRepository.findById(userId).orElse(null);

        if(user == null){
            return null;
        }

        // refresh token은 유지하고 access token만 새로 발급
        String newAccessToken = jwtProvider.createAccessToken(user.getId());

        // access token 쿠키만 갱신
        addTokenCookie(response, "accessToken", newAccessToken, ACCESS_TOKEN_EXPIRATION);

        return new TokenResponse(newAccessToken,refreshToken);
    }

    // Access / Refresh 토큰을 새로 발급하고 DB에 저장
    private TokenResponse generateAndSaveTokens(UserAuth user){
        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());
        return new TokenResponse(accessToken,refreshToken);
    }


    // AcessToken, RefreshToken 쿠키를 한번에 추가
    private void addTokenCookies(HttpServletResponse response, TokenResponse tokenResponse){
        addTokenCookie(response,"accessToken", tokenResponse.accessToken(), ACCESS_TOKEN_EXPIRATION);
        addTokenCookie(response,"refreshToken", tokenResponse.refreshToken(), REFRESH_TOKEN_EXPIRATION);
    }

    // 공통 쿠키 저장 로직
    private void addTokenCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        System.out.println(name);
        if(name.equals("refreshToken")){
            cookie.setPath("/api/v1/auth/refresh");
        }
        else{
            cookie.setPath("/");
        }
        System.out.println(name + " : "+ cookie.getPath());
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }


    public record TokenResponse(String accessToken, String refreshToken) { }
}
