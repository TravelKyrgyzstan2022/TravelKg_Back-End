package com.example.benomad.service;

import com.example.benomad.enums.CommentReference;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.dto.CommentDTO;
import org.springframework.data.domain.PageRequest;

import java.security.Principal;
import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllComments() throws ContentNotFoundException;
    List<CommentDTO> getReferenceCommentsById(Long placeId, CommentReference reference, PageRequest pageRequest);
    CommentDTO getCommentById(Long id) throws ContentNotFoundException;
    CommentDTO likeDislikeComment(Long commentId, Long userId, boolean isDislike) throws ContentNotFoundException;
    CommentDTO insertComment(CommentReference reference, Long referenceId, CommentDTO commentDTO);
    CommentDTO deleteCommentById(Long id) throws ContentNotFoundException;
    CommentDTO updateCommentById(Long id, CommentDTO commentDTO) throws ContentNotFoundException;
}
