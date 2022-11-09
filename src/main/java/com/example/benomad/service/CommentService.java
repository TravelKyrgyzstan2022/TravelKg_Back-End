package com.example.benomad.service;

import com.example.benomad.exception.CommentNotFoundException;
import com.example.benomad.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllComments();
    CommentDTO getCommentById(Long id) throws CommentNotFoundException;
    CommentDTO insertComment(CommentDTO commentDTO);
    CommentDTO deleteCommentById(Long id) throws CommentNotFoundException;
    CommentDTO updateCommentById(Long id,CommentDTO commentDTO) throws CommentNotFoundException;
}
