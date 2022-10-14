package com.example.benomad.service;

import com.example.benomad.exception.NotFoundException;
import com.example.benomad.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllComments();
    CommentDTO getCommentById(Long id) throws NotFoundException;
    CommentDTO insertComment(CommentDTO commentDTO);
    CommentDTO deleteCommentById(Long id) throws NotFoundException;
    CommentDTO updateCommentById(Long id,CommentDTO commentDTO) throws NotFoundException;
}
