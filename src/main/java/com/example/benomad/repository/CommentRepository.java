package com.example.benomad.repository;

import com.example.benomad.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * FROM comments c " +
            "WHERE c.id IN (" +
            "SELECT comment_id FROM place_comments WHERE place_id = :place_id) " +
            "AND c.is_deleted = false",
            nativeQuery = true,
            countQuery = "SELECT COUNT(*) FROM comments c " +
                    "WHERE c.id IN (" +
                    "SELECT comment_id FROM place_comments WHERE place_id = :place_id) " +
                    "AND c.is_deleted = false"
    )
    Page<Comment> getPlaceCommentsById(@Param("place_id")Long placeId, Pageable pageable);

    @Query(value = "SELECT * FROM comments c " +
            "WHERE c.id IN(SELECT comment_id FROM blog_comments WHERE blog_id=:blog_id) " +
            "AND c.is_deleted = false",
            nativeQuery = true,
            countQuery = "SELECT COUNT(*) FROM comments c " +
                    "WHERE c.id IN (SELECT comment_id FROM blog_comments WHERE blog_id = :blog_id) " +
                    "AND c.is_deleted = false")
    Page<Comment> getBlogCommentsById(@Param("blog_id")Long blogId, Pageable pageable);

    @Query(value = "SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM comment_likes t " +
            "WHERE t.comment_id = :comment_id " +
            "and t.liked_users_id = :userId", nativeQuery = true)
    boolean isCommentLikedByUser(@Param("comment_id") Long commentId, @Param("userId") Long userId);

    @Query(value = "SELECT COUNT(t) FROM comment_likes t WHERE t.comment_id = :comment_id", nativeQuery = true)
    Integer getLikesNumberById(@Param("comment_id") Long commentId);
}
