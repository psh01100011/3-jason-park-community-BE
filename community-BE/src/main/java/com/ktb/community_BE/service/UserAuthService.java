package com.ktb.community_BE.service;

import com.ktb.community_BE.entity.User;
import com.ktb.community_BE.entity.UserAuth;
import com.ktb.community_BE.repository.UserAuthRepository;
import com.ktb.community_BE.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {

    /*
    Todo :
        비밀번호 해싱 구현 ->  UserAuthService(login 추가)
        세션 확인 위치 조정(getSessionId) 여기보다 다른 곳에 있는 게 더 좋을 듯 함.
     */

    private final UserAuthRepository userAuthRepository;

    public Long login(String email, String password) {
        UserAuth userAuth = userAuthRepository.findByEmail(email);

        // Password해싱 학습 후 수정
        String passwordHash = password;

        if (!userAuth.getPassword().equals(passwordHash)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return userAuth.getUser().getId();
    }


    //세션 만료 확인 : 여기 있는 것보다 따로 분리하는 게 더 좋을 것 같긴 함 -> 일단 여기
    public Long getSessionId(HttpSession session){
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null){
            throw new IllegalStateException("로그인되지 않은 사용자입니다.");
        }
        return userId;
    }

}
