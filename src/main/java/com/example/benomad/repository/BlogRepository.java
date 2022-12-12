package com.example.benomad.repository;

import com.example.benomad.entity.Blog;
import com.example.benomad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogRepository extends JpaRepository <Blog, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM blog_likes t " +
            "WHERE t.blog_id = :blogId " +
            "and t.user_id = :userId", nativeQuery = true)
    boolean isBlogLikedByUser(@Param("blogId") Long blogId, @Param("userId") Long userId);

//    @Query(value = "SELECT * FROM blogs WHERE id IN (SELECT blog_id FROM blog_likes WHERE user_id = :userId)",
//            nativeQuery = true)
//    List<Blog> findLikedBlogsOfUser(@Param("userId") Long userId);

    @Query(value = "SELECT COUNT(t) FROM blog_likes t WHERE t.blog_id = :blogId", nativeQuery = true)
    Integer getLikesNumberById(@Param("blogId") Long blogId);

    List<Blog> findByAuthor(User author);
}
