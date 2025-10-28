package com.ktb.community_BE.service;

import com.ktb.community_BE.dto.UserDto;
import com.ktb.community_BE.entity.User;
import com.ktb.community_BE.entity.UserAuth;
import com.ktb.community_BE.entity.UserStatus;
import com.ktb.community_BE.repository.UserAuthRepository;
import com.ktb.community_BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    /*
    Todo :
        비밀번호 해싱 구현 -> 현재 주석처리만 함(createUser, updateAuth)
        + UserAuthService(login 추가)
        이걸 여기서 하는게 맞는진 모르겠지만 이미지 파일 -> Url 처리 필요함
        user의 글 목록 보기
        user의 댓글 목록 보기
        user의 좋아요 목록 보기
     */

    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;

    //이메일 중복 체크 : 중복 시 false
    public boolean checkEmail(String email){
        return !userAuthRepository.existsByEmail(email);
    }
    //닉네임 중복 체크 : 중복 시 false
    public boolean checkNickname(String nickname){
        return !userRepository.existsByNickname(nickname);
    }

    //회원 정보 저장 :
    @Transactional
    public UserDto createUser(UserDto userRequest){
        // password 해싱 일단 스킵
        String passwordHash = userRequest.getPassword();

        // 유저 정보 저장 user + userAuth
        User user = userRepository.save(new User(
                userRequest.getNickname(),
                userRequest.getProfileImage(),
                LocalDateTime.now()
        ));

        UserAuth userAuth = userAuthRepository.save(new UserAuth(
                user,
                userRequest.getEmail(),
                passwordHash,
                LocalDateTime.now()
        ));

        return new UserDto(user.getId(),
                userAuth.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                null);
    }

    //내 정보 조회 : 이메일, 닉네임, 프로필
    public UserDto getMyInfo(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserAuth userAuth = userAuthRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Auth not found"));

        return new UserDto(
                user.getId(),
                userAuth.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                null);
    }

    // 내 정보 업데이트 : 프로필 정보만 업데이트, 인증 정보 제외
    @Transactional
    public UserDto updateInfo(Long id, UserDto userRequest){
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        //중복 닉네임 검증 작업이 필요할 것으로 보임
        if (userRequest.getNickname() != null) user.changeNickname(userRequest.getNickname());


        if (userRequest.getProfileImage() != null) user.changeProfileImage(userRequest.getProfileImage());
        user.setUpdatedAt(LocalDateTime.now());

        return new UserDto(
                null,
                null,
                user.getNickname(),
                user.getProfileImage(),
                null);
    }

    // 내 인증정보(비밀번호 업데이트)
    @Transactional
    public boolean updateAuth(Long id, UserDto userRequest){
        UserAuth userAuth = userAuthRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        //비밀번호 해싱 아직 미구현
        String passwordHash = userRequest.getPassword();
        if (userAuth.getPassword().equals(passwordHash)){
            return false;
        }
        userAuth.changePassword(passwordHash);
        userAuth.setUpdatedAt(LocalDateTime.now());
        return true;
    }


    //유저 탈퇴 처리 : 유저가 작성한 게시물과 댓글은 관리자에게 넘김 -> 탈퇴한 사용자 익명처리용 -> 생각해보니까 이상함
    //바로 삭제 안하는 이유가 데이터를 보존하기 위함인데, 게시물 주인을 관리자로 돌리면 원작자를 찾기 힘들어짐 -> 구조 변경이 필요할 것 같음.
    @Transactional
    public void withdrawUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.changeStatus(UserStatus.DELETED);
        user.setDeletedAt(LocalDateTime.now());
    }

    //실제 데이터베이스에서 삭제 구현 시 사용
    @Transactional
    public void deleteUser(Long id){
        userRepository.deleteById(id);
        userAuthRepository.deleteById(id);
    }

}
