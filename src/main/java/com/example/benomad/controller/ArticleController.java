package com.example.benomad.controller;


import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.dto.UserDTO;

import com.example.benomad.service.impl.ArticleServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@Tag(name = "Article Resource", description = "The Article API ")
public class ArticleController {

    private final ArticleServiceImpl articleServiceImpl;

    @GetMapping("")
    void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/v1/articles/");
    }

    @Operation(summary = "Gets all the articles")
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getAllArticles(){
        return ResponseEntity.ok(articleServiceImpl.getAllArticles());
    }

    @Operation(summary = "Finds article by ID")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getArticleById(@PathVariable Long id){
        return ResponseEntity.ok(articleServiceImpl.getArticleById(id));
    }

    @Operation(summary = "Inserts article to the database")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertArticle(@RequestBody ArticleDTO articleDTO){
        articleDTO.setUserDTO(UserDTO.builder()
                .id(1L)
                .build()); //для тестов, а так, в будущем будем работать с токенами
        return ResponseEntity.status(HttpStatus.CREATED).body(articleServiceImpl.insertArticle(articleDTO));
    }

    @Operation(summary = "Deletes article by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteArticleById(@PathVariable Long id){
        return ResponseEntity.ok(articleServiceImpl.deleteArticleById(id));
    }

    @Operation(summary = "Updates article by ID")
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateArticleById(@PathVariable Long id, @RequestBody ArticleDTO articleDTO){
        return ResponseEntity.ok(articleServiceImpl.updateArticleById(id, articleDTO));
    }
}
