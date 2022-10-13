package com.example.benomad.controller;


import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.service.impl.ArticleDAOImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articles/")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleDAOImpl articleDAOImpl;

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getAllArticles(){
        try{
            return ResponseEntity.ok(articleDAOImpl.getAllArticles());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getArticleById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(articleDAOImpl.getArticleById(id));
        }catch (ContentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertArticle(@RequestBody ArticleDTO articleDTO){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(articleDAOImpl.insertArticle(articleDTO));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteArticleById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(articleDAOImpl.deleteArticleById(id));
        }catch (ContentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateArticleById(@PathVariable Long id, @RequestBody ArticleDTO articleDTO){
        try{
            return ResponseEntity.ok(articleDAOImpl.updateArticleById(id, articleDTO));
        }catch (ContentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
