package com.example.benomad.mapper;

import com.example.benomad.dto.CommentDTO;
import com.example.benomad.entity.Comment;

public class CommentMapper {
    public static Comment commentDtoToComment(CommentDTO commentDTO) {
        return Comment.builder()
                .id(commentDTO.getId())
                .place(PlaceMapper.placeDtoToPlace(commentDTO.getPlaceDTO()))
                .user(UserMapper.userDtoToUser(commentDTO.getUserDTO()))
                .body(commentDTO.getBody())
                .build();
    }

    public static CommentDTO commentToCommentDto(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .placeDTO(PlaceMapper.placeToPlaceDto(comment.getPlace()))
                .userDTO(UserMapper.userToUserDto(comment.getUser()))
                .body(comment.getBody())
                .build();
    }
}
