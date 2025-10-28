package com.ktb.community_BE.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthDto {
    private String email;
    private String password;
}
