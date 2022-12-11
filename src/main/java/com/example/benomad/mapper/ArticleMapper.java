package com.example.benomad.mapper;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.entity.Article;
import com.example.benomad.enums.Content;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleMapper {

    private final UserRepository userRepository;
    private final AuthServiceImpl authService;

    public ArticleDTO entityToDto(Article article){
        return ArticleDTO.builder()
                .id(article.getId())
                .body(article.getBody())
                .imageUrls(article.getImageUrls())
                .title(article.getTitle())
                .userId(article.getUser().getId())
                .build();
    }

    public Article dtoToEntity(ArticleDTO dto){
        Long userId = authService.getCurrentUserId();
        return Article.builder()
                .id(dto.getId())
                .body(dto.getBody())
                .imageUrls(dto.getImageUrls())
                .title(dto.getTitle())
                .user(userRepository.findById(userId).orElseThrow(
                        () -> new ContentNotFoundException(Content.USER,"id",String.valueOf(userId))
                        )
                )
                .build();
    }

    public List<ArticleDTO> entityListToDtoList(List<Article> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
