package com.ktb.community_BE.repository;

import com.ktb.community_BE.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {

    boolean existsByEmail(String email);
    UserAuth findByEmail(String email);
}
