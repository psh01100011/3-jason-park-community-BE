package com.ktb.community_BE.repository;

import com.ktb.community_BE.entity.Post;
import com.ktb.community_BE.entity.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPostStatusAndIdLessThanOrderByIdDesc(
            PostStatus status,
            Long lastPostId,
            Pageable pageable
    );

    List<Post> findByPostStatusOrderByIdDesc(
            PostStatus status,
            Pageable pageable
    );
}
