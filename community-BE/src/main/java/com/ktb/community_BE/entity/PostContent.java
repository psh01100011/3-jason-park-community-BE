package com.ktb.community_BE.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "post_content")
public class PostContent {

    @Id
    @Column(name ="postId")
    private Long id;

    // mapsId 사용 이유 : 둘은 동시에 존재해야 함 -> 생명주기 통일?
    @MapsId
    @OneToOne
    @JoinColumn(name = "postId")
    private Post post;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(length =255)
    private String image;

    protected PostContent(){};

    public PostContent(Post post, String content, String image){
        this.post = post;
        this.content = content;
        this.image = image;
    }

    public void changeContent(String content) { this.content = content; }

    public void changeImage(String image) { this.image = image; }


}
