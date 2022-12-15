package com.example.benomad.service.impl;

import com.example.benomad.dto.DeletionInfoDTO;
import com.example.benomad.dto.MessageResponse;
import com.example.benomad.entity.DeletionInfo;
import com.example.benomad.entity.User;
import com.example.benomad.enums.CommentReference;
import com.example.benomad.enums.Content;
import com.example.benomad.exception.*;
import com.example.benomad.dto.CommentDTO;
import com.example.benomad.entity.Comment;
import com.example.benomad.mapper.CommentMapper;
import com.example.benomad.mapper.DeletionInfoMapper;
import com.example.benomad.repository.CommentRepository;
import com.example.benomad.security.domain.Role;
import com.example.benomad.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final AuthServiceImpl authService;
    private final UserServiceImpl userService;
    private final PlaceServiceImpl placeService;
    private final BlogServiceImpl blogService;
    private final CommentMapper commentMapper;
    private final DeletionInfoMapper deletionInfoMapper;

    @Override
    public List<CommentDTO> getAllComments() {
        return commentMapper.entityListToDtoList(commentRepository.findAll());
    }

    @Override
    public List<CommentDTO> getReferenceCommentsById(Long referenceId, CommentReference reference,
                                                     PageRequest pageRequest) {
        Page<Comment> page;
        if (reference == CommentReference.BLOG){
            blogService.getBlogEntityById(referenceId);
            page = commentRepository.getBlogCommentsById(referenceId, pageRequest);
        } else {
            placeService.getPlaceEntityById(referenceId);
            page = commentRepository.getPlaceCommentsById(referenceId, pageRequest);
        }
        return commentMapper.entityListToDtoList(page.stream().collect(Collectors.toList()));
    }

    @Override
    public CommentDTO deleteCommentById(Long commentId, DeletionInfoDTO infoDTO) {
        checkComment(commentId);
        Comment comment = getCommentEntityById(commentId);
        comment.setIsDeleted(true);
        infoDTO = infoDTO != null ? infoDTO :
                new DeletionInfoDTO(null, "Comment was deleted by author", null, null);
        DeletionInfo info = deletionInfoMapper.dtoToEntity(infoDTO);
        info.setDeletionDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        info.setResponsibleUser(userService.getUserEntityById(authService.getCurrentUserId()));
        comment.setDeletionInfo(deletionInfoMapper.dtoToEntity(infoDTO));
        return commentMapper.entityToDto(comment);
    }

    @Override
    public CommentDTO updateCommentById(Long commentId, CommentDTO commentDTO) {
        checkComment(commentId);
        Comment comment = commentMapper.dtoToEntity(commentDTO);
        comment.setUpdateDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        comment.setId(commentId);
        comment.setUser(userService.getUserEntityById(authService.getCurrentUserId()));
        commentDTO = commentMapper.entityToDto(commentRepository.save(comment));
        return commentDTO;
    }

    @Override
    public CommentDTO getCommentById(Long commentId) {
        return commentMapper.entityToDto(getCommentEntityById(commentId));
    }

    @Override
    public MessageResponse likeDislikeComment(Long commentId, CommentReference reference, Long referenceId, boolean isDislike) {
        Long userId = authService.getCurrentUserId();
        User user = userService.getUserEntityById(userId);
        Comment comment = getCommentEntityById(commentId);
        checkReference(reference, referenceId, comment);
        Set<User> likedUsers = comment.getLikedUsers();
        boolean isAlreadyLiked = likedUsers.contains(user);
        String message;
        if (isDislike) {
            if (!isAlreadyLiked) {
                throw new ContentIsNotLikedException(Content.COMMENT);
            }
            likedUsers.remove(user);
            message = String.format("Like has been successfully removed from comment with id = {%d}!", commentId);
        } else {
            if (isAlreadyLiked) {
                throw new ContentIsAlreadyLikedException(Content.COMMENT);
            }
            likedUsers.add(user);
            message = String.format("Like has been successfully added to the comment with id = {%d}!", commentId);
        }
        comment.setLikedUsers(likedUsers);
        commentRepository.save(comment);
        return new MessageResponse(message, 200);
    }

    @Override
    public CommentDTO insertComment(CommentReference reference, Long referenceId, CommentDTO commentDTO) {
        Comment comment = commentMapper.dtoToEntity(commentDTO);
        comment.setCreationDate(LocalDate.now(ZoneId.of("Asia/Bishkek")));
        comment.setIsDeleted(false);
        comment.setUser(userService.getUserEntityById(authService.getCurrentUserId()));
        comment = commentRepository.save(comment);
        commentDTO = commentMapper.entityToDto(comment);

        if (reference == CommentReference.PLACE) {
            placeService.addComment(referenceId, comment);
        }else if (reference == CommentReference.BLOG) {
            blogService.addComment(referenceId, comment);
        }
        return commentDTO;
    }

    @Override
    public Comment getCommentEntityById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> {
                    throw new ContentNotFoundException(Content.COMMENT, "id", String.valueOf(commentId));
                });
    }

    private void checkComment(Long commentId) {
        User user = userService.getUserEntityById(authService.getCurrentUserId());
        Comment comment = getCommentEntityById(commentId);
        if (!comment.getUser().getId().equals(user.getId())
                && !user.getRoles().contains(Role.ROLE_ADMIN)
                && !user.getRoles().contains(Role.ROLE_SUPERADMIN)) {
            throw new NoAccessException();
        }
    }

    private void checkReference(CommentReference reference, Long referenceId, Comment comment){
        if (reference == CommentReference.BLOG) {
            if (!blogService.getBlogEntityById(referenceId).getComments().contains(comment)) {
                throw new CommentReferenceException(CommentReference.BLOG);
            }
        } else if (reference == CommentReference.PLACE) {
            if (!placeService.getPlaceEntityById(referenceId).getComments().contains(comment)) {
                throw new CommentReferenceException(CommentReference.PLACE);
            }
        }
    }
}
