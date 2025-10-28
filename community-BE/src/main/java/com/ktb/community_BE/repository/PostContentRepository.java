package com.ktb.community_BE.repository;

import com.ktb.community_BE.entity.PostContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostContentRepository extends JpaRepository<PostContent, Long> {
}
