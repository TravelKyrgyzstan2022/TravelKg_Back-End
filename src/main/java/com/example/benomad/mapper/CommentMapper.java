package com.example.benomad.mapper;

import com.example.benomad.dto.CommentDTO;
import com.example.benomad.entity.Comment;
import com.example.benomad.repository.CommentRepository;
import com.example.benomad.service.impl.AuthServiceImpl;
import com.example.benomad.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentMapper {

    private final CommentRepository commentRepository;
    private final AuthServiceImpl authService;
    private final UserMapper userMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Comment dtoToEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .body(commentDTO.getBody())
                .build();
    }

    public CommentDTO entityToDto(Comment comment) {
        Long userId = authService.getCurrentUserId();
        return CommentDTO.builder()
                .id(comment.getId())
                .creationDate(formatter.format(comment.getCreationDate()))
                .updateDate(comment.getUpdateDate() != null ?
                        formatter.format(comment.getUpdateDate()) : null)
                .user(userMapper.entityToDto(comment.getUser()))
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
