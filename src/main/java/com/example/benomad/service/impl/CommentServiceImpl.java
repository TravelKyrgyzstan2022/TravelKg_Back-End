package com.example.benomad.service.impl;

import com.example.benomad.enums.CommentReference;
import com.example.benomad.enums.ContentNotFoundEnum;
import com.example.benomad.exception.*;
import com.example.benomad.dto.CommentDTO;
import com.example.benomad.entity.Comment;
import com.example.benomad.mapper.CommentMapper;
import com.example.benomad.repository.BlogRepository;
import com.example.benomad.repository.CommentRepository;
import com.example.benomad.repository.PlaceRepository;
import com.example.benomad.repository.UserRepository;
import com.example.benomad.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthServiceImpl authService;
    private final PlaceRepository placeRepository;
    private final BlogRepository blogRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentDTO> getAllComments() {
        Long userId = authService.getCurrentUserId();
        return commentMapper.entityListToDtoList(commentRepository.findAll(), userId);
    }

    @Override
    public List<CommentDTO> getReferenceCommentsById(Long referenceId, CommentReference reference, PageRequest pageRequest) {
        Page<Comment> page;
        Long userId = authService.getCurrentUserId();
        if(reference == CommentReference.BLOG){
            if(!blogRepository.existsById(referenceId)){
                throw new ContentNotFoundException(ContentNotFoundEnum.BLOG, "id", String.valueOf(referenceId));
            }
            page = commentRepository.getBlogCommentsById(referenceId, pageRequest);
        }else{
            if(!placeRepository.existsById(referenceId)){
                throw new ContentNotFoundException(ContentNotFoundEnum.PLACE, "id", String.valueOf(referenceId));
            }
            page = commentRepository.getPlaceCommentsById(referenceId, pageRequest);
        }
        return commentMapper.entityListToDtoList(page.stream().collect(Collectors.toList()), userId);
    }

    @Override
    public CommentDTO getCommentById(Long commentId) throws ContentNotFoundException {
        Long userId = authService.getCurrentUserId();
        return commentMapper.entityToDto(commentRepository.findById(commentId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.COMMENT, "id", String.valueOf(commentId));
                }), userId
        );
    }

    @Override
    public CommentDTO likeDislikeComment(Long commentId, boolean isDislike) throws ContentNotFoundException{
        Long userId = authService.getCurrentUserId();

        if(!commentRepository.existsById(commentId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.COMMENT, "id", String.valueOf(commentId));
        }
        if(!userRepository.existsById(userId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.USER, "id", String.valueOf(userId));
        }

        boolean isAlreadyLiked = commentRepository.isCommentLikedByUser(commentId, userId);

        if(isDislike){
            if(!isAlreadyLiked){
                throw new ContentIsNotLikedException(ContentNotFoundEnum.COMMENT);
            }
            commentRepository.dislikeCommentById(commentId, userId);
        }else{
            if(isAlreadyLiked){
                throw new ContentIsAlreadyLikedException(ContentNotFoundEnum.COMMENT);
            }
            commentRepository.likeCommentById(commentId, userId);
        }

        return commentMapper.entityToDto(
                commentRepository.findById(commentId).orElseThrow(
                        () -> {
                            throw new ContentNotFoundException(ContentNotFoundEnum.COMMENT, "id", String.valueOf(commentId));
                        }), userId
        );
    }

    @Override
    public CommentDTO insertComment(CommentReference reference, Long referenceId, CommentDTO commentDTO) {
        commentDTO.setId(null);
        commentDTO.setReference(reference);
        commentDTO.setReferenceId(referenceId);

        Comment comment = commentMapper.dtoToEntity(commentDTO);
        comment.setCreationDate(LocalDate.now());
        commentRepository.save(comment);
        commentDTO.setId(commentRepository.getLastValueOfSequence());

        if(reference == CommentReference.PLACE){
            commentRepository.insertPlaceComment(commentDTO.getId(), referenceId);
        }else if(reference == CommentReference.BLOG){
            commentRepository.insertBlogComment(commentDTO.getId(), referenceId);
        }
        return commentDTO;
    }

    @Override
    public CommentDTO deleteCommentById(Long commentId) throws ContentNotFoundException {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(ContentNotFoundEnum.COMMENT, "id", String.valueOf(commentId));
                });
        commentRepository.deleteById(commentId);
        return commentMapper.entityToDto(comment, null);
    }

    @Override
    public CommentDTO updateCommentById(Long commentId, CommentDTO commentDTO) throws ContentNotFoundException {
        if(!commentRepository.existsById(commentId)){
            throw new ContentNotFoundException(ContentNotFoundEnum.COMMENT, "id", String.valueOf(commentId));
        }
        commentDTO.setId(commentId);
        commentDTO.setCreationDate(null);
        commentRepository.save(commentMapper.dtoToEntity(commentDTO));
        return commentDTO;
    }
}
