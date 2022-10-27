package com.example.benomad.mapper;

import com.example.benomad.dto.CommentDTO;
import com.example.benomad.entity.Comment;


public class CommentMapper {
    public static Comment commentDtoToComment(CommentDTO commentDTO) {
        return Comment.builder()
                .id(commentDTO.getId())
                .place(PlaceMapper.dtoToEntity(commentDTO.getPlaceDTO()))
                .user(UserMapper.dtoToEntity(commentDTO.getUserDTO()))
                .body(commentDTO.getBody())
                .build();
    }

    public static CommentDTO commentToCommentDto(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .placeDTO(PlaceMapper.entityToDto(comment.getPlace()))
                .userDTO(UserMapper.entityToDto(comment.getUser()))
                .body(comment.getBody())
                .build();
    }
}
