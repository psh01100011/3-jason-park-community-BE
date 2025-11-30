package com.ktb.community_BE.service;

import com.ktb.community_BE.repository.UserAuthRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserAuthRepository userAuthRepository;

    @InjectMocks
    UserService userService;
    @Test
    void checkEmail() {
        String email = "test1@test.com";
        Mockito.when(userAuthRepository.existsByEmail(email)).thenReturn(true);

        // when
        boolean result = userService.checkEmail(email);

        // then
        Assertions.assertFalse(result);
        Mockito.verify(userAuthRepository).existsByEmail(email);
    }
}