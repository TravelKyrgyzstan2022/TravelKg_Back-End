package com.example.benomad.service.impl;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.entity.Article;
import com.example.benomad.entity.Place;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.enums.ImagePath;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.logger.LogWriterServiceImpl;
import com.example.benomad.mapper.ArticleMapper;
import com.example.benomad.repository.ArticleRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;
    private final AuthServiceImpl authService;
    private final ImageServiceImpl imageService;
    private final LogWriterServiceImpl logWriter;

    @Override
    public List<ArticleDTO> getAllArticles() {
        List<ArticleDTO> dtos = articleMapper.entityListToDtoList(articleRepository.findAll());
        logWriter.get(String.format("%s - Returned %d articles", authService.getCurrentEmail(), dtos.size()));
        return dtos;
    }

    @Override
    public ArticleDTO getArticleById(Long articleId) throws ContentNotFoundException {
        logWriter.get(String.format("%s - Returned article with id = %d", authService.getCurrentEmail(), articleId));
        return articleMapper.entityToDto(articleRepository.findById(articleId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.ARTICLE, "id", String.valueOf(articleId));
                })
        );
    }

    @Override
    public ArticleDTO updateArticleById(Long articleId, ArticleDTO articleDTO) throws ContentNotFoundException {
        if(!articleRepository.existsById(articleId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.ARTICLE, "id", String.valueOf(articleId));
        }
        if(!userRepository.existsById(articleDTO.getUserId())){
            throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(articleId));
        }
        articleDTO.setId(articleId);
        articleRepository.save(articleMapper.dtoToEntity(articleDTO));
        logWriter.update(String.format("%s - Updated article with id = %d", authService.getCurrentEmail(), articleId));
        return articleDTO;
    }

    @Override
    public ArticleDTO insertArticle(ArticleDTO articleDTO) {
        articleDTO.setId(null);
        articleDTO.setUserId(authService.getCurrentUserId());
        articleDTO.setId(articleRepository.save(articleMapper.dtoToEntity(articleDTO)).getId());
        logWriter.insert(String.format("%s - Inserted article with id = %d", authService.getCurrentEmail(), articleDTO.getId()));
        return articleDTO;
    }

    @Override
    public ArticleDTO deleteArticleById(Long articleId) throws ContentNotFoundException {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.ARTICLE, "id", String.valueOf(articleId));
                });
        articleRepository.delete(article);
        logWriter.delete(String.format("%s - Deleted article with id = %d", authService.getCurrentEmail(), articleId));
        return articleMapper.entityToDto(article);
    }

    @Override
    public boolean insertImagesByArticleId(Long articleId, MultipartFile[] files) throws ContentNotFoundException {
        Article article = articleRepository
                .findById(articleId).orElseThrow(
                        () -> new ContentNotFoundException(ContentNotFoundEnum.ARTICLE,"id",String.valueOf(articleId))
                );
        List<String> placeImageUrls = article.getImageUrls();
        placeImageUrls.addAll(imageService.uploadImages(files, ImagePath.ARTICLE));
        articleRepository.save(article);
        return true;
    }

    @Override
    public List<String> getImagesById(Long id) throws ContentNotFoundException {
        Article article = articleRepository.findById(id)
                .orElseThrow(
                        () -> new ContentNotFoundException(ContentNotFoundEnum.ARTICLE,"id",String.valueOf(id))
                );
        return article.getImageUrls();
    }


}
