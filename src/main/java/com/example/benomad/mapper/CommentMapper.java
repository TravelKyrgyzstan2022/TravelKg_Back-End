package com.example.benomad.mapper;

import com.example.benomad.dto.CommentDTO;
import com.example.benomad.entity.Comment;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.repository.CommentRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.impl.AuthServiceImpl;
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
    private final AuthServiceImpl authService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Comment dtoToEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .id(commentDTO.getId())
                .user(userRepository.findById(commentDTO.getUserId()).orElseThrow(
                        () -> {
                            throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(commentDTO.getUserId()));
                        }
                ))
                .body(commentDTO.getBody())
                .build();
    }

    public CommentDTO entityToDto(Comment comment) {
        Long userId = authService.getCurrentUserId();
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

    public List<CommentDTO> entityListToDtoList(List<Comment> entities){
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
