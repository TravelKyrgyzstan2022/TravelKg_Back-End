package com.example.benomad.controller;


import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.service.impl.ArticleServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/v1/articles")
@Tag(name = "Article Resource", description = "The Article API ")
public class ArticleController {

    private final ArticleServiceImpl articleServiceImpl;

    @Operation(summary = "Gets all the articles")
    @GetMapping(value = {"/", ""}, produces = "application/json")
    public ResponseEntity<?> getAllArticles(){
        return ResponseEntity.ok(articleServiceImpl.getAllArticles());
    }

    @Operation(summary = "Finds article by ID")
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getArticleById(@PathVariable Long id){
        return ResponseEntity.ok(articleServiceImpl.getArticleById(id));
    }

    @Operation(summary = "Inserts an article to the database")
    @PostMapping(value = {"/", ""}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertArticle(@RequestBody ArticleDTO articleDTO){
        articleDTO.setUserId(1L); //для тестов, а так, в будущем будем работать с токенами
        return ResponseEntity.status(HttpStatus.CREATED).body(articleServiceImpl.insertArticle(articleDTO));
    }

    @Operation(summary = "Uploads image by article ID",
            description = "Adds new image record to an article by its ID and Image itself.")
    @PutMapping(path = "/uploadImage/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadUserProfileImage(@PathVariable("userId") Long id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(articleServiceImpl.insertImageByArticleId(id,file));
    }

    @Operation(summary = "Gets image by by article ID",
            description = "Adds new rating record to an article by its ID and Image itself.")
    @GetMapping(path = "/getImage/{userId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserProfileImage(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(articleServiceImpl.getImageByArticleId(id));
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