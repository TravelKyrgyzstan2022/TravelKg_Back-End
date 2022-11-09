package com.example.benomad.repository;

import com.example.benomad.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository <Article, Long> {
    @Query(value = "SELECT last_value FROM articles_id_seq", nativeQuery = true)
    Long getLastValueOfSequence();
}
