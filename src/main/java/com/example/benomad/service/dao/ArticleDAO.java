package com.example.benomad.service.dao;

import com.example.benomad.dto.ArticleDTO;

import java.util.List;

public interface ArticleDAO {
    public List<ArticleDTO> getAllArticles();
    public ArticleDTO getArticleById(Long id) throws Exception;
    public void updateArticleById(Long id, ArticleDTO articleDTO);
    public void insertArticleById(Long id, ArticleDTO articleDTO);
    public void deleteArticleById(Long id);
}
