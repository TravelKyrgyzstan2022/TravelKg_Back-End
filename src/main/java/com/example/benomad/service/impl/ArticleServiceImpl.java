package com.example.benomad.service.impl;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.entity.Article;
import com.example.benomad.enums.AwsBucket;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.enums.ImagePath;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.exception.FailedWhileUploadingException;
import com.example.benomad.logger.LogWriter;
import com.example.benomad.mapper.ArticleMapper;
import com.example.benomad.repository.ArticleRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleMapper articleMapper;
    private final AuthServiceImpl authService;
    private final ImageServiceImpl imageService;

    @Override
    public List<ArticleDTO> getAllArticles() {
        List<ArticleDTO> dtos = articleMapper.entityListToDtoList(articleRepository.findAll());
        LogWriter.get(String.format("%s - Returned %d articles", authService.getName(), dtos.size()));
        return dtos;
    }

    @Override
    public ArticleDTO getArticleById(Long articleId) throws ContentNotFoundException {
        LogWriter.get(String.format("%s - Returned article with id = %d", authService.getName(), articleId));
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
        LogWriter.update(String.format("%s - Updated article with id = %d", authService.getName(), articleId));
        return articleDTO;
    }

    @Override
    public ArticleDTO insertArticle(ArticleDTO articleDTO) {
        articleDTO.setId(null);
        articleDTO.setUserId(authService.getCurrentUserId());
        articleDTO.setId(articleRepository.save(articleMapper.dtoToEntity(articleDTO)).getId());
        LogWriter.insert(String.format("%s - Inserted article with id = %d", authService.getName(), articleDTO.getId()));
        return articleDTO;
    }

    @Override
    public ArticleDTO deleteArticleById(Long articleId) throws ContentNotFoundException {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.ARTICLE, "id", String.valueOf(articleId));
                });
        articleRepository.delete(article);
        LogWriter.delete(String.format("%s - Deleted article with id = %d", authService.getName(), articleId));
        return articleMapper.entityToDto(article);
    }

    @Override
    public Long insertImageByArticleId(Long id, MultipartFile file) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(ContentNotFoundEnum.ARTICLE,"id",String.valueOf(id)));
        imageService.checkIsNotEmpty(file);
        imageService.checkIsImage(file);
        Map<String, String> metadata = imageService.getMetaData(file);
        String pathToFile = String.format("%s/%s", AwsBucket.MAIN_BUCKET.getBucketName(), ImagePath.ARTICLE.getPathToImage());
        String uniqueFileName = String.format("%s-%s",file.getOriginalFilename(), UUID.randomUUID());
        try {
            imageService.saveImageAws(pathToFile,uniqueFileName, Optional.of(metadata),file.getInputStream());
            article.setImageUrl(uniqueFileName);
            articleRepository.save(article);
            return article.getId();
        } catch (IOException e) {
            throw new FailedWhileUploadingException();
        }
    }

    @Override
    public byte[] getImageByArticleId(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(ContentNotFoundEnum.ARTICLE,"id",String.valueOf(id)));
        String pathToFile = String.format("%s/%s", AwsBucket.MAIN_BUCKET.getBucketName(), ImagePath.ARTICLE.getPathToImage());
        return article.getImageUrl().map(x -> imageService.getAwsImageByPathAndKey(pathToFile,x)).orElse(new byte[0]);
    }
}
