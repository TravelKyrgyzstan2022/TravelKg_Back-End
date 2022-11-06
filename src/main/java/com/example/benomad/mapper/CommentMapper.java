package com.example.benomad.mapper;

import com.example.benomad.dto.CommentDTO;
import com.example.benomad.entity.Comment;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentMapper {

    private final UserRepository userRepository;
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

    public CommentDTO entityToDto(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .creationDate(formatter.format(comment.getCreationDate()))
                .userId(comment.getUser().getId())
                .body(comment.getBody())
                .build();
    }

    public List<CommentDTO> entityListToDtoList(List<Comment> entities){
        List<CommentDTO> dtos = new ArrayList<>();
        for(Comment c : entities){
            dtos.add(entityToDto(c));
        }
        return dtos;
    }

    public List<Comment> dtoListToEntityList(List<CommentDTO> dtos){
        List<Comment> entities = new ArrayList<>();
        for(CommentDTO c : dtos){
            entities.add(dtoToEntity(c));
        }
        return entities;
    }
}
