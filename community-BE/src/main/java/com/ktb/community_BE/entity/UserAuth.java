package com.ktb.community_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "user_auth")
public class UserAuth {



    @Id
    @Column(name = "userId")
    private Long id;

    // mapsId 사용 이유 : 둘은 동시에 존재해야 함 -> 생명주기 통일?
    @MapsId
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 70)
    private String password;

    private LocalDateTime updatedAt;

    protected UserAuth() {};

    public UserAuth(User user, String email, String password,LocalDateTime updatedAt){
        this.user = user;
        this.email = email;
        this.password = password;
        this.updatedAt = updatedAt;
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void setUpdatedAt(LocalDateTime updatedAt){
        this.updatedAt = updatedAt;
    }
}
