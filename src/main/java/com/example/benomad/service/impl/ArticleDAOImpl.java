package com.example.benomad.service.impl;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.entity.Article;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.ArticleMapper;
import com.example.benomad.mapper.UserMapper;
import com.example.benomad.repository.ArticleRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.dao.ArticleDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleDAOImpl implements ArticleDAO {

    private final ArticleRepository articleRepository;

    @Override
    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleDTO> articleDTOS = new ArrayList<>();
        for (Article a : articles){
            articleDTOS.add(ArticleMapper.entityToDto(a));
        }
        return articleDTOS;
    }

    @Override
    public ArticleDTO getArticleById(Long id) throws ContentNotFoundException {
        return ArticleMapper.entityToDto(articleRepository.findById(id).orElseThrow(
                ContentNotFoundException::new));
    }

    @Override
    public ArticleDTO updateArticleById(Long id, ArticleDTO articleDTO) throws ContentNotFoundException {
        articleRepository.findById(id).orElseThrow(
                ContentNotFoundException::new
        );
        articleRepository.save(ArticleMapper.dtoToEntity(articleDTO));
        return articleDTO;
    }

    @Override
    public ArticleDTO insertArticle(ArticleDTO articleDTO) {
        articleRepository.save(ArticleMapper.dtoToEntity(articleDTO));
        return articleDTO;
    }

    @Override
    public ArticleDTO deleteArticleById(Long id) throws ContentNotFoundException {
        Article article = articleRepository.findById(id).orElseThrow(
                ContentNotFoundException::new
        );
        articleRepository.delete(article);
        return ArticleMapper.entityToDto(article);
    }
}
