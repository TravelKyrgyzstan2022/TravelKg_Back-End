package com.example.benomad.mapper;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.entity.Article;
import com.example.benomad.exception.UserNotFoundException;
import com.example.benomad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleMapper {

    private final UserRepository userRepository;

    public ArticleDTO entityToDto(Article article){
        return ArticleDTO.builder()
                .id(article.getId())
                .body(article.getBody())
                .imageUrl(article.getImageUrl())
                .title(article.getTitle())
                .userId(article.getUser().getId())
                .build();
    }

    public Article dtoToEntity(ArticleDTO dto){
        return Article.builder()
                .id(dto.getId())
                .body(dto.getBody())
                .imageUrl(dto.getImageUrl())
                .title(dto.getTitle())
                .user(userRepository.findById(dto.getUserId()).orElseThrow(UserNotFoundException::new))
                .build();
    }
}
