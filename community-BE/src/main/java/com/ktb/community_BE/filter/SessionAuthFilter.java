package com.ktb.community_BE.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class SessionAuthFilter extends OncePerRequestFilter {
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
        System.out.println(method+" : "+ path);
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
        //세션 가져오기 : 없을 때 생성하지 않도록 false 처리
        HttpSession session = request.getSession(false);

        if(session != null){
            Object userId = session.getAttribute("userId");
            request.setAttribute("userId", session.getAttribute("userId"));
        }
        else{
            System.out.println("세션 만료");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        chain.doFilter(request,response);
    }
}
