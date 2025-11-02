package com.ktb.community_BE.filter;

import com.ktb.community_BE.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    //필터 제외 경로 목록
    private static final String[] EXCLUDED_PATHS_AFTER_METHODS_GET = {
            "/api/v1/posts/list"
    };
    private static final String[] EXCLUDED_PATHS_AFTER_METHODS_POST = {
            "/api/v1/auth", "/api/v1/users", "/api/v1/users/email", "/api/v1/users/nickname"
    };


    //필터는 그냥 다 거치게? -> 회원가입?
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String method = request.getMethod();
        String path = request.getRequestURI();
        System.out.println("jwt : " +method+" : "+ path);
        if(method.equals("OPTIONS")) return true;
        if(method.equals("GET")){
            if(Arrays.stream(EXCLUDED_PATHS_AFTER_METHODS_GET).anyMatch(path::startsWith)){
                return true;
            }
        }
        else if(method.equals("POST")){
            System.out.println(path +" ?");
            if(Arrays.stream(EXCLUDED_PATHS_AFTER_METHODS_POST).anyMatch(path::startsWith)){
                return true;
            }
        }
        System.out.println(path +" 검증함");
        return false;

    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws IOException, ServletException {


        //토큰 까보기
        Optional<String> token = extractToken(request);
        System.out.println( "jwt 토큰: " + token);

        //토큰 확인
        if(token.isEmpty()){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        //토큰 검증
        if(!validateAndSetAttributes(token.get(), request, response)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        System.out.println( "jwt 토큰 검증 성공 ");
        chain.doFilter(request,response);

    }

    private Optional<String> extractToken(HttpServletRequest request){
        return extractTokenFromHeader(request)
                .or(() -> extractTokenFromCookie(request));
    }

    // 헤더에서 토큰 추출 -> 쿠키에만 사용할 예정이지만 미리 구현
    private Optional<String> extractTokenFromHeader(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("bearer"))
                .map(header -> header.substring(7));
    }

    // 쿠키에서 토큰 추출
    private Optional<String> extractTokenFromCookie(HttpServletRequest request){
        return Optional.ofNullable(request.getCookies())
                .stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }


    // 토큰 검증 + 속성 확인 및 설정
    private boolean validateAndSetAttributes(String token, HttpServletRequest request, HttpServletResponse response){
        try{
            var jws = jwtProvider.parse(token);
            Claims body = jws.getBody();



            request.setAttribute("userId", Long.valueOf(body.getSubject()));
            return true;
        } catch(Exception exception){
            System.out.println("토큰 만료");
            return false;
        }
    }

}
