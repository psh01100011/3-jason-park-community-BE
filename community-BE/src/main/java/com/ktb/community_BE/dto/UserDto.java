package com.ktb.community_BE.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileImage;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    public UserDto(Long id, String email, String nickname, String profileImage, String password){
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.password = password;
    }

}
