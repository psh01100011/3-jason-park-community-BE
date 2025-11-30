package com.ktb.community_BE.service;

import com.ktb.community_BE.entity.Post;
import com.ktb.community_BE.entity.User;
import com.ktb.community_BE.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    @Test
    void updatePostStatus() {
        // given
        Long postId = 1L;
        Long correctUserId = 100L;
        Long wrongUserId = 99L;

        User user = Mockito.mock(User.class);
        Mockito.when(user.getId()).thenReturn(correctUserId);

        Post post = Mockito.mock(Post.class);
        Mockito.when(post.getUser()).thenReturn(user);
        Mockito.when(postRepository.findById(postId))
                .thenReturn(Optional.of(post));

        // when & then
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                postService.updatePostStatus(postId, wrongUserId)
        );

    }
}