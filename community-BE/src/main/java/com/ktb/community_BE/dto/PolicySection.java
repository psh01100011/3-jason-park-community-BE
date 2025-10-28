package com.ktb.community_BE.dto;

public class PolicySection {
    private String article;
    private String title;
    private String content;

    public PolicySection() {} // Jackson 기본 생성자 필요

    public String getArticle(){
        return article;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
