package com.example.benomad.controller;


import com.example.benomad.service.impl.ArticleDAOImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article/")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleDAOImpl articleDAOImpl;

    @GetMapping(value = "/all", produces = "application/json")
    public ResponseEntity<?> getAllArticles(){
        return ResponseEntity.ok(articleDAOImpl.getAllArticles());
    }
}
