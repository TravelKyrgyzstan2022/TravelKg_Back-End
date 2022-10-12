package com.example.benomad.service.impl;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.entity.Article;
import com.example.benomad.mapper.ArticleMapper;
import com.example.benomad.repository.ArticleRepository;
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
            articleDTOS.add(ArticleMapper.articleToArticleDto(a));
        }
        return articleDTOS;
    }

    @Override
    public ArticleDTO getArticleById(Long id) throws Exception {
        return ArticleMapper.articleToArticleDto(articleRepository.findById(id).orElseThrow(
                () -> new Exception("Article not found.")));
    }

    @Override
    public void updateArticleById(Long id, ArticleDTO articleDTO) {

    }

    @Override
    public void insertArticleById(Long id, ArticleDTO articleDTO) {

    }

    @Override
    public void deleteArticleById(Long id) {

    }
}
