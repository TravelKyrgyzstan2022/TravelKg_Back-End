package com.example.benomad.mapper;

import com.example.benomad.dto.CommentDTO;
import com.example.benomad.entity.Comment;
import com.example.benomad.exception.PlaceNotFoundException;
import com.example.benomad.exception.UserNotFoundException;
import com.example.benomad.repository.PlaceRepository;
import com.example.benomad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentMapper {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public Comment dtoToEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .id(commentDTO.getId())
                .place(placeRepository.findById(commentDTO.getPlaceId()).orElseThrow(PlaceNotFoundException::new))
                .user(userRepository.findById(commentDTO.getUserId()).orElseThrow(UserNotFoundException::new))
                .body(commentDTO.getBody())
                .build();
    }

    public CommentDTO entityToDto(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .placeId(comment.getPlace().getId())
                .userId(comment.getUser().getId())
                .body(comment.getBody())
                .build();
    }
}
