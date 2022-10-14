package com.example.benomad.service.dao;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.exception.ContentNotFoundException;

import java.util.List;

public interface ArticleDAO {
    public List<ArticleDTO> getAllArticles();
    public ArticleDTO getArticleById(Long id) throws ContentNotFoundException;
    public ArticleDTO updateArticleById(Long id, ArticleDTO articleDTO) throws ContentNotFoundException;
    public ArticleDTO insertArticle(ArticleDTO articleDTO);
    public ArticleDTO deleteArticleById(Long id) throws ContentNotFoundException;
}
