package com.example.benomad.service.impl;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.entity.Article;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.ArticleMapper;
import com.example.benomad.repository.ArticleRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;
    private final AuthServiceImpl authService;

    @Override
    public List<ArticleDTO> getAllArticles() {
        return articleMapper.entityListToDtoList(articleRepository.findAll());
    }

    @Override
    public ArticleDTO getArticleById(Long articleId) throws ContentNotFoundException {
        return articleMapper.entityToDto(articleRepository.findById(articleId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.ARTICLE, articleId);
                })
        );
    }

    @Override
    public ArticleDTO updateArticleById(Long articleId, ArticleDTO articleDTO) throws ContentNotFoundException {
        if(!articleRepository.existsById(articleId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.ARTICLE, articleId);
        }
        if(!userRepository.existsById(articleDTO.getUserId())){
            throw new ContentNotFoundException(ContentNotFoundEnum.USER, articleDTO.getUserId());
        }
        articleDTO.setId(articleId);
        articleRepository.save(articleMapper.dtoToEntity(articleDTO));
        return articleDTO;
    }

    @Override
    public ArticleDTO insertArticle(ArticleDTO articleDTO) {
        articleDTO.setId(authService.getCurrentUserId());
        articleRepository.save(articleMapper.dtoToEntity(articleDTO));
        articleDTO.setId(articleRepository.getLastValueOfSequence());
        return articleDTO;
    }

    @Override
    public ArticleDTO deleteArticleById(Long articleId) throws ContentNotFoundException {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.ARTICLE, articleId);
                });
        articleRepository.delete(article);
        return articleMapper.entityToDto(article);
    }
}
