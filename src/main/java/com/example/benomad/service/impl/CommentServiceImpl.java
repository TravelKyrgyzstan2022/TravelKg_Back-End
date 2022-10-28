package com.example.benomad.service.impl;

import com.example.benomad.exception.CommentNotFoundException;
import com.example.benomad.dto.CommentDTO;
import com.example.benomad.dto.PlaceDTO;
import com.example.benomad.entity.Comment;
import com.example.benomad.entity.Place;
import com.example.benomad.mapper.CommentMapper;
import com.example.benomad.repository.CommentRepository;
import com.example.benomad.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    @Override
    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for (Comment comment : comments) {
            commentDTOS.add(CommentMapper.entityToDto(comment));
        }
        return commentDTOS;
    }

    @Override
    public CommentDTO getCommentById(Long id) throws CommentNotFoundException {
        return CommentMapper.entityToDto(commentRepository.findById(id).orElseThrow(CommentNotFoundException::new));
    }

    @Override
    public CommentDTO insertComment(CommentDTO commentDTO) {
        commentRepository.save(CommentMapper.dtoToEntity(commentDTO));
        return commentDTO;
    }

    @Override
    public CommentDTO deleteCommentById(Long id) throws CommentNotFoundException {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        commentRepository.delete(comment);
        return CommentMapper.entityToDto(comment);
    }

    @Override
    public CommentDTO updateCommentById(Long id, CommentDTO commentDTO) throws CommentNotFoundException {
        commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        commentRepository.save(CommentMapper.dtoToEntity(commentDTO));
        return commentDTO;
    }
}
