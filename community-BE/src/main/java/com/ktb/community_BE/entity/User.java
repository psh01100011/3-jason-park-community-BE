package com.ktb.community_BE.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "user")
public class User {

    //id 생성 규칙은 뭐로 할까? ->  일단 기본 값
    // int가 아니라 Integer? -> 하이버네이트에서 null값을 가질 수 있도록 권장한다고 함.(아직 이유는 모름)
    // -> 최종적으로 Long으로 바꿈 -> 다른 id들과 같이 통일성 있게 관리
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userId")
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String nickname;

    private String profileImage;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;


    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    // post, comment, like 구현 필요
    /*

    @OneToMany(mappedBy = "user")
    private List<PostLike> likes = new ArrayList<>();

    */

    protected User(){};

    public User(String nickname, String profileImage, LocalDateTime createdAt){
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }
    public void changeProfileImage(String profileImage){
        this.profileImage = profileImage;
    }
    public void changeStatus(UserStatus userStatus){
        this.userStatus= userStatus;
    }
    public void setUpdatedAt(LocalDateTime updatedAt){
        this.updatedAt = updatedAt;
    }
    public void setDeletedAt(LocalDateTime deletedAt){
        this.deletedAt = deletedAt;
    }

}
