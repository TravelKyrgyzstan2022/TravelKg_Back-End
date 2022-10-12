package com.example.benomad.mapper;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.entity.Article;

public class ArticleMapper {
    public static ArticleDTO articleToArticleDto(Article article){
        return ArticleDTO.builder()
                .id(article.getId())
                .body(article.getBody())
                .imageUrl(article.getImageUrl())
                .title(article.getTitle())
                .userDTO(UserMapper.userToUserDto(article.getUser()))
                .build();
    }

    public static Article articleDtoToArticle(ArticleDTO dto){
        return Article.builder()
                .id(dto.getId())
                .body(dto.getBody())
                .imageUrl(dto.getImageUrl())
                .title(dto.getTitle())
                .user(UserMapper.userDtoToUser(dto.getUserDTO()))
                .build();
    }
}
