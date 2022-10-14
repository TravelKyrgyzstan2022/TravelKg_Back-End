package com.example.travel.mapper;

import com.example.travel.dto.CommentDTO;
import com.example.travel.entity.Comment;

public class CommentMapper {
    public static Comment commentDtoToComment(CommentDTO commentDTO) {
        return Comment.builder()
                .id(commentDTO.getId())
                .place(commentDTO.getPlace())
                .user(commentDTO.getUser())
                .body(commentDTO.getBody())
                .build();
    }

    public static CommentDTO commentToCommentDto(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .place(comment.getPlace())
                .user(comment.getUser())
                .body(comment.getBody())
                .build();
    }
}
