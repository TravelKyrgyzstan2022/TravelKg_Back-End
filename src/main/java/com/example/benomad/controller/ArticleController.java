package com.example.benomad.controller;


import com.example.benomad.dto.ArticleDTO;
import com.example.benomad.dto.UserDTO;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.mapper.UserMapper;
import com.example.benomad.service.impl.ArticleServiceImpl;
import com.example.benomad.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articles/")
@RequiredArgsConstructor
@Tag(name = "Article Resource", description = "Everything needed to work with articles :)")
public class ArticleController {

    private final ArticleServiceImpl articleServiceImpl;

    @Operation(description = "Gets all the articles")
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<?> getAllArticles(){
        try{
            return ResponseEntity.ok(articleServiceImpl.getAllArticles());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> getArticleById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(articleServiceImpl.getArticleById(id));
        }catch (ContentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> insertArticle(@RequestBody ArticleDTO articleDTO){
        try{
            articleDTO.setUserDTO(UserDTO.builder()
                    .id(1L)
                    .build()); //для тестов, а так, в будущем будем работать с токенами
            return ResponseEntity.status(HttpStatus.CREATED).body(articleServiceImpl.insertArticle(articleDTO));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> deleteArticleById(@PathVariable Long id){
        try{
            return ResponseEntity.ok(articleServiceImpl.deleteArticleById(id));
        }catch (ContentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateArticleById(@PathVariable Long id, @RequestBody ArticleDTO articleDTO){
        try{
            return ResponseEntity.ok(articleServiceImpl.updateArticleById(id, articleDTO));
        }catch (ContentNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
