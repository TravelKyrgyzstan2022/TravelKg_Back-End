package com.example.benomad.service;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.exception.ArticleNotFoundException;

import java.util.List;

public interface ArticleService {
    List<ArticleDTO> getAllArticles();
    ArticleDTO getArticleById(Long id) throws ArticleNotFoundException;
    ArticleDTO updateArticleById(Long id, ArticleDTO articleDTO) throws ArticleNotFoundException;
    ArticleDTO insertArticle(ArticleDTO articleDTO);
    ArticleDTO deleteArticleById(Long id) throws ArticleNotFoundException;
}
