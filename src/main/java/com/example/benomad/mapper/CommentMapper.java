package com.example.benomad.mapper;

import com.example.benomad.dto.CommentDTO;
import com.example.benomad.entity.Comment;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.repository.CommentRepository;
import com.example.benomad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentMapper {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Comment dtoToEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .id(commentDTO.getId())
                .user(userRepository.findById(commentDTO.getUserId()).orElseThrow(
                        () -> {
                            throw new ContentNotFoundException(ContentNotFoundEnum.USER, commentDTO.getUserId());
                        }
                ))
                .body(commentDTO.getBody())
                .build();
    }

    public CommentDTO entityToDto(Comment comment, Long userId) {
        return CommentDTO.builder()
                .id(comment.getId())
                .creationDate(formatter.format(comment.getCreationDate()))
                .userId(comment.getUser().getId())
                .body(comment.getBody())
                .likeCount(commentRepository.getLikesNumberById(comment.getId()))
                .isLikedByCurrentUser(
                        userId != null ?
                                commentRepository.isCommentLikedByUser(comment.getId(), userId) : null)
                .build();
    }

    public List<CommentDTO> entityListToDtoList(List<Comment> entities, Long userId){
        return entities.stream().map(entity -> entityToDto(entity, userId)).collect(Collectors.toList());
    }
}
