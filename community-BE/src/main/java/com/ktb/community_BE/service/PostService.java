package com.ktb.community_BE.service;


import com.ktb.community_BE.dto.PostDto;
import com.ktb.community_BE.entity.Post;
import com.ktb.community_BE.entity.PostContent;
import com.ktb.community_BE.entity.PostStatus;
import com.ktb.community_BE.entity.User;
import com.ktb.community_BE.repository.PostContentRepository;
import com.ktb.community_BE.repository.PostRepository;
import com.ktb.community_BE.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostContentRepository postContentRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    //게시물 저장 : 프록시 객체로 최적화
    @Transactional
    public PostDto createPost(PostDto postRequest) {
        //프록시 객체로 삽입
        User user = entityManager.getReference(User.class, postRequest.getUserId());
        Post post = postRepository.save(new Post(
                user,
                postRequest.getTitle()
        ));

        PostContent postContent = postContentRepository.save(new PostContent(
                post,
                postRequest.getContent(),
                postRequest.getImage()
        ));

        return new PostDto(post.getId(),
                user.getId(), null, post.getTitle(),
                postContent.getContent(), postContent.getImage(),0,0,0,
                LocalDateTime.now(), LocalDateTime.now());
    }

    //게시물 상세조회
    public PostDto getPostDetail(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        //게시물 비활성화 상태
        if(post.getPostStatus() != PostStatus.ACTIVE){
            return new PostDto(id);
        }

        //활성화 상태면 게시물 정보 가져와서 전달
        PostContent postContent = postContentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("PostContent not found"));
        User user = userRepository.findById(post.getUser().getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        post.addViewCount();
        return new PostDto(
                post.getId(), user.getId(), user.getNickname(),
                post.getTitle(), postContent.getContent(), postContent.getImage(),
                post.getViewCount(),post.getCommentCount(),post.getLikeCount(),
                post.getCreatedAt(), post.getUpdatedAt()
        );
    }

    //게시물 수정
    @Transactional
    public PostDto updatePost(PostDto postRequest){
        Post post = postRepository.findById(postRequest.getId()).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        PostContent postContent = postContentRepository.findById(postRequest.getId()).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (postRequest.getTitle() != null){
            post.changeTitle(postRequest.getTitle());
        }
        if (postRequest.getImage() != null){
            postContent.changeImage(postRequest.getImage());
        }
        if (postRequest.getContent() != null){
            postContent.changeContent(postRequest.getContent());
        }
        post.setUpdatedAt(LocalDateTime.now());

        return new PostDto(post.getId(),
                post.getUser().getId(),null, post.getTitle(),
                postContent.getContent(), postContent.getImage(),
                post.getViewCount(),post.getCommentCount(),post.getLikeCount(),
                post.getCreatedAt(), post.getUpdatedAt());
    }

    //게시물 소프트 삭제
    @Transactional
    public void updatePostStatus(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        post.setPostStatusDeleted();
        post.setDeletedAt(LocalDateTime.now());
    }

    //게시물 삭제
    @Transactional
    public void deletePost(Long id){
        postRepository.deleteById(id);
        postContentRepository.deleteById(id);
    }

    //게시물 목록 조회 : 이건 진짜 모르겠다 repository도 아직 이해 못함 나중에 공부할 것
    public List<PostDto> getPostList(Long lastPostId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        List<Post> posts;

        if (lastPostId == null) {
            // 처음 로딩
            posts = postRepository.findByPostStatusOrderByIdDesc(PostStatus.ACTIVE, pageable);
        } else {
            // 무한스크롤 로딩
            posts = postRepository.findByPostStatusAndIdLessThanOrderByIdDesc(PostStatus.ACTIVE, lastPostId, pageable);
        }

        // PostContent를 조인해서 가져오지 않았으니 각 게시물에 맞는 content도 조회
        return posts.stream()
                .map(post -> {
                    User user = userRepository.findById(post.getUser().getId())
                            .orElseThrow(() -> new IllegalArgumentException("User not found"));

                    return new PostDto(
                            post.getId(),
                            user.getId(),
                            user.getNickname(),
                            post.getTitle(),
                            null,
                            null,
                            post.getViewCount(),post.getCommentCount(),post.getLikeCount(),
                            post.getCreatedAt(),
                            post.getUpdatedAt()
                    );
                })
                .toList();
    }

}
