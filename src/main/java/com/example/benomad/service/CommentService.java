package com.example.benomad.service;

import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.entity.Comment;
import com.example.benomad.enums.CommentReference;
import com.example.benomad.exception.ContentNotFoundException;
import com.example.benomad.dto.CommentDTO;
import org.springframework.data.domain.PageRequest;

import java.security.Principal;
import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllComments();
    List<CommentDTO> getReferenceCommentsById(Long placeId, CommentReference reference, PageRequest pageRequest);
    CommentDTO getCommentById(Long id);
    MessageResponse likeDislikeComment(Long commentId, CommentReference reference, Long referenceId, boolean isDislike);
    CommentDTO insertComment(CommentReference reference, Long referenceId, CommentDTO commentDTO);
    CommentDTO deleteCommentById(Long commentId, DeletionInfoDTO infoDTO);
    CommentDTO updateCommentById(Long commentId, CommentDTO commentDTO);
    Comment getCommentEntityById(Long commentId);
}
