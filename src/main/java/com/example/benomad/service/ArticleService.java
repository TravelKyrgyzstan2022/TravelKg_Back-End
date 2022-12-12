package com.example.benomad.service;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.dto.ImageDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.entity.Article;
import com.example.benomad.exception.ContentNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {
    List<ArticleDTO> getAllArticles();
    ArticleDTO getArticleById(Long id);
    ArticleDTO updateArticleById(Long id, ArticleDTO articleDTO);
    Long insertArticle(ArticleDTO articleDTO);
    ArticleDTO deleteArticleById(Long id);
    MessageResponse insertImagesByArticleId(Long articleId, MultipartFile[] files);
    List<String> getImagesById(Long id);
    ArticleDTO insertArticleWithImages(ArticleDTO articleDTO, MultipartFile[] files);
    MessageResponse insertImages64ByArticleId(Long id, ImageDTO[] files);
    Article getArticleEntityById(Long articleId);
}
