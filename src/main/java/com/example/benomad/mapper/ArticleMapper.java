package com.example.benomad.mapper;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.entity.Article;
import com.example.benomad.exception.UserNotFoundException;
import com.example.benomad.repository.UserRepository;

public class ArticleMapper {
    public static ArticleDTO entityToDto(Article article){
        return ArticleDTO.builder()
                .id(article.getId())
                .body(article.getBody())
                .imageUrl(article.getImageUrl())
                .title(article.getTitle())
                .userId(article.getUser().getId())
                .build();
    }

    public static Article dtoToEntity(ArticleDTO dto, UserRepository userRepository){
        return Article.builder()
                .id(dto.getId())
                .body(dto.getBody())
                .imageUrl(dto.getImageUrl())
                .title(dto.getTitle())
                .user(userRepository.findById(dto.getUserId()).orElseThrow(UserNotFoundException::new))
                .build();
    }
}
