package com.example.benomad.service.impl;

import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.entity.Article;
import com.example.benomad.exception.ArticleNotFoundException;
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

    @Override
    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleDTO> articleDTOS = new ArrayList<>();
        for (Article a : articles){
            articleDTOS.add(articleMapper.entityToDto(a));
        }
        return articleDTOS;
    }

    @Override
    public ArticleDTO getArticleById(Long id) throws ArticleNotFoundException {
        return articleMapper.entityToDto(articleRepository.findById(id).orElseThrow(
                ArticleNotFoundException::new));
    }

    @Override
    public ArticleDTO updateArticleById(Long id, ArticleDTO articleDTO) throws ArticleNotFoundException {
        articleRepository.findById(id).orElseThrow(
                ArticleNotFoundException::new);
        articleDTO.setId(id);
        articleRepository.save(articleMapper.dtoToEntity(articleDTO));
        return articleDTO;
    }

    @Override
    public ArticleDTO insertArticle(ArticleDTO articleDTO) {
        articleDTO.setId(null);
        articleRepository.save(articleMapper.dtoToEntity(articleDTO));
        articleDTO.setId(articleRepository.getLastValueOfArticleSequence());
        return articleDTO;
    }

    @Override
    public ArticleDTO deleteArticleById(Long id) throws ArticleNotFoundException {
        Article article = articleRepository.findById(id).orElseThrow(
                ArticleNotFoundException::new);
        articleRepository.delete(article);
        return articleMapper.entityToDto(article);
    }
}
