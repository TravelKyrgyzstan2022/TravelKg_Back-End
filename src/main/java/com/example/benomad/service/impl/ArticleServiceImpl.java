package com.example.benomad.service.impl;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.entity.Article;
import com.example.benomad.enums.Content;
import com.example.benomad.enums.ImagePath;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.ArticleMapper;
import com.example.benomad.repository.ArticleRepository;
import com.example.benomad.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final AuthServiceImpl authService;
    private final UserServiceImpl userService;
    private final ImageServiceImpl imageService;

    @Override
    public List<ArticleDTO> getAllArticles() {
        return articleMapper.entityListToDtoList(articleRepository.findAll());
    }

    @Override
    public ArticleDTO getArticleById(Long articleId) throws ContentNotFoundException {
        return articleMapper.entityToDto(getArticleEntityById(articleId));
    }

    @Override
    public ArticleDTO updateArticleById(Long articleId, ArticleDTO articleDTO) throws ContentNotFoundException {
        getArticleEntityById(articleId);
        articleDTO.setId(articleId);
        articleRepository.save(articleMapper.dtoToEntity(articleDTO));
        return articleDTO;
    }

    @Override
    public ArticleDTO insertArticle(ArticleDTO articleDTO) {
        articleDTO.setId(null);
        Article article = articleMapper.dtoToEntity(articleDTO);
        article.setUser(userService.getUserEntityById(authService.getCurrentUserId()));
        articleDTO.setId(articleRepository.save(article).getId());
        return articleDTO;
    }

    @Override
    public ArticleDTO deleteArticleById(Long articleId) throws ContentNotFoundException {
        Article article = getArticleEntityById(articleId);
        articleRepository.delete(article);
        return articleMapper.entityToDto(article);
    }

    @Override
    public MessageResponse insertImagesByArticleId(Long articleId, MultipartFile[] files) throws ContentNotFoundException {
        Article article = getArticleEntityById(articleId);
        article.setImageUrls(imageService.uploadImages(files, ImagePath.ARTICLE));
        articleRepository.save(article);
        return new MessageResponse("Images have been successfully added to the article!", 200);
    }

    @Override
    public List<String> getImagesById(Long articleId) throws ContentNotFoundException {
        Article article = getArticleEntityById(articleId);
        return article.getImageUrls();
    }

    @Override
    public ArticleDTO insertArticleWithImages(ArticleDTO articleDTO, MultipartFile[] files) {
        articleDTO.setId(null);
        articleDTO.setImageUrls(imageService.uploadImages(files, ImagePath.ARTICLE));
        Article article = articleMapper.dtoToEntity(articleDTO);
        article.setUser(userService.getUserEntityById(authService.getCurrentUserId()));
        return articleMapper.entityToDto(articleRepository.save(article));
    }

    public Article getArticleEntityById(Long articleId){
        return articleRepository.findById(articleId)
                .orElseThrow(
                        () -> new ContentNotFoundException(Content.ARTICLE,"id",String.valueOf(articleId))
                );
    }
}
