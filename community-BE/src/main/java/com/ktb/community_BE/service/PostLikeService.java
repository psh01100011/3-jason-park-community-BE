package com.ktb.community_BE.service;

import com.ktb.community_BE.entity.Post;
import com.ktb.community_BE.entity.PostLike;
import com.ktb.community_BE.entity.User;
import com.ktb.community_BE.repository.PostLikeRepository;
import com.ktb.community_BE.repository.PostRepository;
import com.ktb.community_BE.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final EntityManager entityManager;

    //좋아요
    public void likePost(Long postId, Long userId) {
        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new IllegalStateException("이미 좋아요를 누른 게시물입니다.");
        }

        Post post = entityManager.getReference(Post.class, postId);
        User user = entityManager.getReference(User.class, userId);

        postLikeRepository.save(new PostLike(post, user));
        post.addLikeCount();
    }

    //좋아요 취소
    public void unlikePost(Long postId, Long userId) {
        if (!postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new IllegalStateException("좋아요를 누르지 않은 게시물입니다.");
        }

        postLikeRepository.deleteByPostIdAndUserId(postId, userId);
        Post post = entityManager.getReference(Post.class, postId);
        post.minusLikeCount(); // 좋아요 취소 시 감소
    }
}